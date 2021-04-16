package hr.fer.oprpp1.custom.scripting.parser;

/**
 * This class represents an exception that will be thrown when parser cannot parse the document.
 */
public class SmartScriptParserException extends RuntimeException {

    public SmartScriptParserException(String msg) {
        super(msg);
    }
}
