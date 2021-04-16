package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Class representing LexerException that will be thrown when an error occurs while doing lexical analysis.
 *
 * @author Korina Šimičević
 */
public class LexerException extends RuntimeException{

    public LexerException(String msg) {
        super(msg);
    }
}
