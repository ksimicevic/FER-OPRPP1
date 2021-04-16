package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Enum representing all 3 states that lexer can be found in. TEXT state is state outside the brackets, TAG_NAME is state
 * when lexer is reading the first element in tag, TAG_ELEMENTS is state when lexer is parsing all the remaining contents inside
 * the tag brackets.
 */
public enum LexerState {
    TEXT, TAG_NAME, TAG_ELEMENTS
}
