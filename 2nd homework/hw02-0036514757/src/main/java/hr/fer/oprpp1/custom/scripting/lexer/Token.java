package hr.fer.oprpp1.custom.scripting.lexer;

import java.util.Objects;

/**
 * This class represents a token in lexical analysis.
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
        if (type == null) {
            throw new NullPointerException("Token type cannot be null");
        }

        this.type = type;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return type == token.type &&
                value.equals(token.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }

    public TokenType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }
}
