package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * This class represents a lexer that will be used for lexical analysis.
 */
public class Lexer {

    private final char[] data;
    private Token token;
    private int currentIndex;
    private LexerState state;

    /**
     * Creates a new lexer that will analyse given string.
     *
     * @param text to be be analysed by lexer
     */
    public Lexer(String text) {
        this.data = text.toCharArray();
        this.currentIndex = 0;
        this.state = LexerState.TEXT;
    }

    /**
     * Finds, creates and returns the next token in the lexical analysis.
     *
     * @return new token created
     * @throws LexerException in case there arent any more valid tokens to be generated
     */
    public Token nextToken() {
        if (currentIndex == this.data.length) {
            this.token = new Token(TokenType.EOF, null);
            currentIndex++;
            return this.token;
        }

        if (currentIndex > this.data.length) {
            throw new LexerException("There aren't any more tokens to be generated");
        }

        char currChar = data[currentIndex];

        if (currChar == '{') {
            if (data[currentIndex + 1] == '$') {
                currentIndex += 2;
                this.state = LexerState.TAG_NAME;
                this.token = new Token(TokenType.OPEN_TAG, "{$");
            } else {
                throw new LexerException("Expected $, but didn't find it");
            }
        } else if (this.state == LexerState.TAG_NAME) {
            this.token = getTagName();
            this.state = LexerState.TAG_ELEMENTS;
        } else if (this.state == LexerState.TAG_ELEMENTS) {
            this.token = getTagElement();
        } else if (this.state == LexerState.TEXT) {
            this.token = getNextText();
        }


        return this.token;
    }

    /**
     * Finds the next string that isn't in tags.
     *
     * @return new text token created from found string in the text
     * @throws LexerException if method has found an invalid sequence starting with escape character
     */
    private Token getNextText() {
        StringBuilder sb = new StringBuilder();

        while (currentIndex < this.data.length) {
            char currChar = this.data[currentIndex];

            if (currChar == '\\') {
                char nextChar = this.data[currentIndex + 1];

                if (nextChar == '\\' || nextChar == '{') {
                    sb.append(nextChar);
                    currentIndex += 2;
                } else {
                    throw new LexerException("Invalid sequence starting with escape character");
                }

            } else if (currChar == '{' && this.data[currentIndex + 1] == '$') {
                break;
            } else {
                sb.append(currChar);
                currentIndex++;
            }
        }

        return new Token(TokenType.TEXT, sb.toString());
    }

    /**
     * Finds and forms a new token contain the tag name.
     *
     * @return new tag name token containing the name of the tag
     * @throws LexerException in case the found tag name is invalid
     */
    private Token getTagName() {
        skipBlankSpaces();
        StringBuilder sb = new StringBuilder();

        if (this.data[currentIndex] == '=') {
            currentIndex++;
            return new Token(TokenType.TAG_NAME, "=");
        }

        while (currentIndex < this.data.length) {
            char currChar = this.data[currentIndex];

            if (!Character.isWhitespace(currChar) && currChar != '$') {
                sb.append(currChar);
                currentIndex++;
            } else {
                break;
            }
        }
        String tagName = sb.toString();

        if (isValidTagName(tagName)) {
            return new Token(TokenType.TAG_NAME, sb.toString());
        } else {
            throw new LexerException("Tag name is invalid");
        }
    }

    /**
     * Checks if the name of current tag is valid. Valid tag names are '=' and variable names.
     *
     * @param s tag name to be checked
     * @return true if tag name is valid
     */
    private boolean isValidTagName(String s) {
        if (s.equals("=")) {
            return true;
        }

        char[] array = s.toCharArray();
        if (Character.isLetter(array[0])) {
            for (int i = 1; i < array.length; i++) {
                if (!(Character.isLetterOrDigit(array[i]) || array[i] == '_')) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    /**
     * Finds and creates a next token representing a element inside the tag.
     *
     * @return new token representing an element inside the tag
     * @throws LexerException should method fail to find the closing tag sequence
     */
    private Token getTagElement() {
        skipBlankSpaces();
        char currChar = this.data[currentIndex];

        if (Character.isDigit(currChar) || (currChar == '-' && Character.isDigit(this.data[currentIndex + 1]))) {
            return getNextNumber();
        } else if (Character.isLetter(currChar)) {
            return getNextVariableName();
        } else if (currChar == '\"') {
            return getNextString();
        } else if (currChar == '@') {
            return getNextFunctionName();
        } else if (currChar == '$') {
            if (this.data[currentIndex + 1] == '}') {
                currentIndex += 2;
                this.state = LexerState.TEXT;
                return new Token(TokenType.CLOSE_TAG, "$}");
            } else {
                throw new LexerException("Expected }, but didn't find it.");
            }
        } else {
            return getNextOperator();
        }
    }

    /**
     * Finds and creates a new token representing an operator. Valid operators are +, - , *, / and ^.
     *
     * @return new operator token
     * @throws LexerException if found symbol isn't one of the allowed operators
     */
    private Token getNextOperator() {
        char currChar = this.data[currentIndex];

        if (currChar == '+' || currChar == '-' || currChar == '*' || currChar == '/' || currChar == '^') {
            currentIndex++;
            return new Token(TokenType.OPERATOR, currChar);
        } else {
            throw new LexerException("Invalid character in tags");
        }
    }

    /**
     * Finds and creates a new token representing a number. Number can be either a double or an integer.
     *
     * @return new number token
     * @throws LexerException in case method fails to create a new number
     */
    private Token getNextNumber() {
        StringBuilder sb = new StringBuilder();
        boolean isNegative = false;
        boolean isDouble = false;

        if (data[currentIndex] == '-') {
            isNegative = true;
            currentIndex++;
        }

        while (currentIndex < this.data.length) {
            char currChar = data[currentIndex];

            if (currChar == '.') {
                isDouble = true;
                sb.append(currChar);
                currentIndex++;
                continue;
            }

            if (Character.isDigit(currChar)) {
                sb.append(currChar);
                currentIndex++;
            } else {
                break;
            }
        }

        if (isDouble) {
            double number;

            try {
                number = Double.parseDouble(sb.toString());
            } catch (NumberFormatException ex) {
                throw new LexerException("Couldn't parse to double");
            }
            number = isNegative ? -number : number;

            return new Token(TokenType.DOUBLE, number);
        } else {
            int number;

            try {
                number = Integer.parseInt(sb.toString());
            } catch (NumberFormatException ex) {
                throw new LexerException("Couldn't parse to integer");
            }
            number = isNegative ? -number : number;

            return new Token(TokenType.INTEGER, number);
        }


    }

    /**
     * Skips blank spaces until any character is found.
     */
    private void skipBlankSpaces() {
        while (currentIndex < this.data.length) {
            if (!Character.isWhitespace(data[currentIndex])) {
                break;
            }
            currentIndex++;
        }
    }

    /**
     * Finds and creates a new token representing a variable.
     * This method will be called after first character is already determined to be a letter, so the method doesn't check for that.
     * This way it is also guaranteed that the method will always find a variable no matter what character is found after the initial one.
     *
     * @return new variable token
     */
    private Token getNextVariableName() {
        StringBuilder sb = new StringBuilder();

        while (currentIndex < this.data.length) {
            char currChar = this.data[currentIndex];

            if (Character.isLetterOrDigit(currChar) || currChar == '_') {
                sb.append(currChar);
                currentIndex++;
            } else {
                break;
            }
        }

        return new Token(TokenType.VARIABLE, sb.toString());
    }

    /**
     * Finds and creates a new function token.
     * This method will be called after @ is detected, so the method doesn't check for that.
     *
     * @return new function token
     */
    private Token getNextFunctionName() {
        StringBuilder sb = new StringBuilder();

        if (Character.isLetter(data[++currentIndex])) {
            sb.append(data[currentIndex++]);

            while (currentIndex < this.data.length) {
                char currChar = this.data[currentIndex];

                if (Character.isLetterOrDigit(currChar) || currChar == '_') {
                    sb.append(currChar);
                    currentIndex++;
                } else {
                    break;
                }
            }
        }

        return new Token(TokenType.FUNCTION, sb.toString());
    }

    /**
     * Finds and creates a new string inside the tag brackets.
     *
     * @return new string token
     * @throws LexerException if escape character is used with invalid sequence
     */
    private Token getNextString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.data[currentIndex++]);

        while (currentIndex < this.data.length) {
            char currChar = this.data[currentIndex];

            if (currChar == '\\') {
                char nextChar = this.data[currentIndex + 1];

                if (nextChar == '\"') {
                    sb.append("\"");
                    currentIndex += 2;
                } else if (nextChar == 'r' || nextChar == 't' || nextChar == 'n') {
                    sb.append(currChar).append(nextChar);
                    currentIndex += 2;
                } else {
                    throw new LexerException("Invalid sequence starting with \\");
                }

                continue;
            }

            sb.append(currChar);
            currentIndex++;

            if (currChar == '\"') {
                break;
            }
        }

        return new Token(TokenType.STRING, sb.toString());
    }

    /**
     * Returns the last token this lexer has generated.
     *
     * @return last generated token
     */
    public Token getToken() {
        return this.token;
    }

 /*  public void execute() {
        while (currentIndex <= this.data.length) {
            nextToken();
            printNextToken();
        }
    }

    public void printNextToken() {
        System.out.println("(" + this.token.getType() + ", " + this.token.getValue() + ")");
    }

    public static void main(String... args) {
        //String s = "This is some text and then wild tags show up {$ FOR i 2213 $} lets try this";
        String s = "This is sample text.\n" +
                "{$ FOR i 1 10.3 -2.3 3 -2 1 $}\n" +
                " This is {$= i $}-th time this message is generated.\n" +
                "{$END$}\n" +
                "{$FOR i 0 10 2 $}\n" +
                " sin({$=i$}^2) = {$= i i * @sin \"0.000\" @decfmt $}\n" +
                "{$END$}";
        String s1 = " sin({$=i$}^2) = {$= i i * @sin \"0.000\" @decfmt $}\n";
        Lexer l = new Lexer(s);
        l.execute();
    }
    */
}
