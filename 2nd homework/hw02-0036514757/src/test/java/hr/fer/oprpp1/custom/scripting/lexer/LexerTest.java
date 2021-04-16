package hr.fer.oprpp1.custom.scripting.lexer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexerTest {

    @Test
    public void testForTag() {
        String s = "{$FOR i 0 10.1 -2 $}";
        Lexer l = new Lexer(s);

        Token[] correctTokens = {
                new Token(TokenType.OPEN_TAG, "{$"),
                new Token(TokenType.TAG_NAME, "FOR"),
                new Token(TokenType.VARIABLE, "i"),
                new Token(TokenType.INTEGER, 0),
                new Token(TokenType.DOUBLE, 10.1),
                new Token(TokenType.INTEGER, -2),
                new Token(TokenType.CLOSE_TAG, "$}"),
                new Token(TokenType.EOF, null),
        };

        checkTokenStream(l, correctTokens);
    }

    @Test
    public void testCombined() {
        String s = "Some string here\n {$FOR i 0 10.1 -2 $}";
        Lexer l = new Lexer(s);

        Token[] correctTokens = {
                new Token(TokenType.TEXT, "Some string here\n "),
                new Token(TokenType.OPEN_TAG, "{$"),
                new Token(TokenType.TAG_NAME, "FOR"),
                new Token(TokenType.VARIABLE, "i"),
                new Token(TokenType.INTEGER, 0),
                new Token(TokenType.DOUBLE, 10.1),
                new Token(TokenType.INTEGER, -2),
                new Token(TokenType.CLOSE_TAG, "$}"),
                new Token(TokenType.EOF, null),
        };

        checkTokenStream(l, correctTokens);
    }

    @Test
    public void testCombinedEcho() {
        String s = "Some string here\n {$= ay \"Noa\" 98 @sin -12.2 $}";
        Lexer l = new Lexer(s);

        Token[] correctTokens = {
                new Token(TokenType.TEXT, "Some string here\n "),
                new Token(TokenType.OPEN_TAG, "{$"),
                new Token(TokenType.TAG_NAME, "="),
                new Token(TokenType.VARIABLE, "ay"),
                new Token(TokenType.STRING, "\"Noa\""),
                new Token(TokenType.INTEGER, 98),
                new Token(TokenType.FUNCTION, "sin"),
                new Token(TokenType.DOUBLE, -12.2),
                new Token(TokenType.CLOSE_TAG, "$}"),
                new Token(TokenType.EOF, null),
        };

        checkTokenStream(l, correctTokens);
    }

    @Test
    public void testStringInsideTag(){
        String s = "A tag follows {$= \"Joe \\\"Long\\\" Smith\"$}.";
        Lexer l = new Lexer(s);

        Token[] correctTokens = {
                new Token(TokenType.TEXT, "A tag follows "),
                new Token(TokenType.OPEN_TAG, "{$"),
                new Token(TokenType.TAG_NAME, "="),
                new Token(TokenType.STRING, "\"Joe \"Long\" Smith\""),
                new Token(TokenType.CLOSE_TAG, "$}"),
                new Token(TokenType.TEXT,"."),
                new Token(TokenType.EOF, null),
        };

        checkTokenStream(l, correctTokens);
    }


    private void checkToken(Token actual, Token expected) {
        String msg = "Token are not equal.";
        assertEquals(expected.getType(), actual.getType(), msg);
        assertEquals(expected.getValue(), actual.getValue(), msg);
    }

    private void checkTokenStream(Lexer lexer, Token[] correctData) {
        int counter = 0;
        for(Token expected : correctData) {
            Token actual = lexer.nextToken();
            String msg = "Checking token "+counter + ":";
            assertEquals(expected.getType(), actual.getType(), msg);
            assertEquals(expected.getValue(), actual.getValue(), msg);
            counter++;
        }
    }
}
