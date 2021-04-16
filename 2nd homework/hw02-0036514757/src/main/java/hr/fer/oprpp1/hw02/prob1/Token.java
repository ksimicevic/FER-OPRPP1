package hr.fer.oprpp1.hw02.prob1;

/**
 * This class represents a token in lexical analysis.
 *
 * @author Korina Šimičević
 */
public class Token {
    private final TokenType type;
    private final Object value;

    /**
     * Creates a token.
     *
     * @param type  of token
     * @param value of token
     */
    public Token(TokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Returns the value of token.
     *
     * @return value
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Returns the type of token.
     *
     * @return token type
     */
    public TokenType getType() {
        return this.type;
    }
}
