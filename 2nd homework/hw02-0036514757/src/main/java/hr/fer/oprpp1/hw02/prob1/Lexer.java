package hr.fer.oprpp1.hw02.prob1;

/**
 * Class representing a model of lexer playing a main role in lexical analysis.
 *
 * @author Korina Šimičević
 */
public class Lexer {
    private char[] data;
    private Token token;
    private int currentIndex;
    private LexerState state;

    /**
     * Creates a new lexer to do lexical analysis on given text.
     *
     * @param text string that lexer will do lexical analysis on
     */
    public Lexer(String text) {
        this.data = text.toCharArray();
        this.currentIndex = 0;
        this.state = LexerState.BASIC;
    }

    /**
     * Generates and returns a new token in lexical analysis.
     *
     * @return new generated token
     * @throws LexerException if there's any error while analysing the characters
     */
    public Token nextToken() {
        if (currentIndex == this.data.length) {
            this.token = new Token(TokenType.EOF, null);
            currentIndex++;
            return this.token;
        }

        if (currentIndex > this.data.length) {
            throw new LexerException("There are no more tokens to be generated.");
        }

        if (this.state == LexerState.BASIC) {
            char currChar = data[currentIndex];

            if (currChar == '#') {
                this.state = LexerState.EXTENDED;
            }

            if (Character.isWhitespace(currChar)) {
                currentIndex++;
                return nextToken();
            } else if (Character.isLetter(currChar) || currChar == '\\') {
                this.token = getNextWord();
            } else if (Character.isDigit(currChar)) {
                this.token = getNextNumber();
            } else {
                this.token = new Token(TokenType.SYMBOL, data[currentIndex++]);
            }

        } else {
            this.token = getExtendedWord();
        }

        return this.token;
    }

    /**
     * Returns the last generated token. Does not generate a new one.
     *
     * @return most recently generated token
     */
    public Token getToken() {
        return token;
    }

    /**
     * If lexer is in EXTENDED state, this method will be called to generate new tokens until '#' is found and lexer is returned to BASIC state.
     *
     * @return new generated token of WORD type
     */
    private Token getExtendedWord() {
        StringBuilder sb = new StringBuilder();

        while (currentIndex < this.data.length) {
            if (!Character.isWhitespace(data[currentIndex])) {
                break;
            }
            currentIndex++;
        }

        if (currentIndex == this.data.length) {
            currentIndex++;
            return new Token(TokenType.EOF, null);
        }

        while (currentIndex < this.data.length) {
            char currChar = data[currentIndex];

            if (Character.isWhitespace(currChar)) {
                break;
            }

            if (currChar == '#') {
                this.state = LexerState.BASIC;

                if (sb.toString().isEmpty()) {
                    currentIndex++;
                    return new Token(TokenType.SYMBOL, currChar);
                }
                break;
            }

            sb.append(currChar);
            currentIndex++;
        }

        return new Token(TokenType.WORD, sb.toString());
    }

    /**
     * This method is called when lexer is in BASIC state and a digit is found. It will try to generate a number token.
     *
     * @return new generated token of NUMBER type
     * @throws LexerException if method fails to generate a token of NUMBER type
     */
    private Token getNextNumber() {
        StringBuilder sb = new StringBuilder();

        while (currentIndex < this.data.length) {
            char currChar = data[currentIndex];

            if (Character.isDigit(currChar)) {
                sb.append(currChar);
                currentIndex++;
            } else {
                break;
            }
        }

        long number;
        try {
            number = Integer.parseInt(sb.toString());
        } catch (NumberFormatException ex) {
            throw new LexerException("Couldn't parse to number.");
        }

        return new Token(TokenType.NUMBER, number);
    }

    /**
     * This method is called when lexer is in BASIC state and a letter or escape character is found. It will try to generate a word token.
     *
     * @return new generated token of WORD type
     * @throws LexerException if methods fails to generate a token of WORD type
     */
    private Token getNextWord() {
        StringBuilder sb = new StringBuilder();

        while (currentIndex < this.data.length) {
            char currChar = data[currentIndex];

            if (Character.isLetter(currChar)) {
                sb.append(currChar);
                currentIndex++;
            } else if (currChar == '\\') {
                char nextChar;

                try {
                    nextChar = data[currentIndex + 1];
                } catch (IndexOutOfBoundsException ex) {
                    throw new LexerException("There's no character to escape");
                }

                if (Character.isDigit(nextChar)) {
                    sb.append(nextChar);
                    currentIndex += 2;
                } else if (nextChar == '\\') {
                    sb.append(nextChar);
                    currentIndex += 2;
                } else {
                    throw new LexerException("Invalid escape character");
                }

            } else {
                break;
            }
        }

        return new Token(TokenType.WORD, sb.toString());
    }

    /**
     * Manually sets the state of lexer. Allowed states are EXTENDED and BASIC.
     *
     * @param state new state of lexer
     * @throws NullPointerException if given state is null
     */
    public void setState(LexerState state) {
        if (state == null) {
            throw new NullPointerException("State cannot be null");
        }

        this.state = state;
    }

 /* public void execute() {
        while (currentIndex <= this.data.length) {
            nextToken();
            printNextToken();
        }
    }

    public void printNextToken() {
        System.out.println("(" + this.token.getType() + ", " + this.token.getValue() + ")");
    }

    public static void main(String... args) {
        String s = "  -.? \r\n\t ##   ";
        Lexer l = new Lexer(s);
        l.execute();
    }

    */
}
