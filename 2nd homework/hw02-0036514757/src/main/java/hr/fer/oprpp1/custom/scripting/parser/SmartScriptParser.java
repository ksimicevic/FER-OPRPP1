package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.scripting.elems.*;
import hr.fer.oprpp1.custom.scripting.lexer.Lexer;
import hr.fer.oprpp1.custom.scripting.lexer.LexerException;
import hr.fer.oprpp1.custom.scripting.lexer.Token;
import hr.fer.oprpp1.custom.scripting.lexer.TokenType;
import hr.fer.oprpp1.custom.scripting.nodes.*;

/**
 * This class represents a parser that will parse the given document and create appropriate document tree.
 */
public class SmartScriptParser {
    private ObjectStack stack; //stack that will be used while parsing to create a tree
    private Lexer lexer; //lexer used for creating tokens
    private DocumentNode document; //main document of parsed text, it will contain all other nodes

    /**
     * Creates a new parser that will parse given text.
     *
     * @param text to be parsed
     */
    public SmartScriptParser(String text) {
        this.lexer = new Lexer(text);
        this.stack = new ObjectStack();
        this.document = new DocumentNode();
        this.parse();
    }

    /**
     * Parses the given text and creates document tree.
     *
     * @throws SmartScriptParserException if method fails to create a valid document tree
     */
    private void parse() {
        this.stack.push(this.document);
        try {
            Token currentToken = this.lexer.nextToken();
            Node currentNode;

            while (!(currentToken.getType() == TokenType.EOF)) {
                currentNode = (Node) this.stack.peek();

                if (currentToken.getType() == TokenType.TEXT) {
                    currentNode.addChildNode(this.getTextNode());
                } else if (currentToken.getType() == TokenType.OPEN_TAG) {
                    Token tagName = this.lexer.nextToken();

                    if (tagName.getValue().toString().toLowerCase().equals("for")) {
                        Node forLoopNode = this.getForLoopNode();
                        currentNode.addChildNode(forLoopNode);
                        this.stack.push(forLoopNode);
                    } else if (tagName.getValue().toString().toLowerCase().equals("end")) {
                        if (this.lexer.nextToken().getType() == TokenType.CLOSE_TAG) {
                            this.stack.pop();
                        } else {
                            throw new SmartScriptParserException("Invalid end tag");
                        }

                        if (this.stack.isEmpty()) {
                            throw new SmartScriptParserException("There's more end tags than opened non-empty tags");
                        }
                    } else {
                        currentNode.addChildNode(this.getEchoNode());
                    }
                }
                currentToken = this.lexer.nextToken();
            }
        } catch (LexerException ex) {
            throw new SmartScriptParserException("Lexer exception has occured.");
        }

    }

    /**
     * Creates a new text node from the next lexer token.
     *
     * @return new text nod
     */
    private Node getTextNode() {
        Token textToken = this.lexer.getToken();
        return new TextNode(new ElementString(textToken.getValue().toString()));
    }

    /**
     * This method will be called when parser detects open tag brackets and FOR as tag name and it will attempt to create a
     * ForLoopNode out of contents in tag bracket.
     *
     * @return new ForLoopNode
     * @throws SmartScriptParserException if contents inside ForLoop tag do not follow the rules of ForLoopNode.
     */
    private Node getForLoopNode() {
        Token token;
        Token endToken = new Token(TokenType.CLOSE_TAG, "$}");
        ElementVariable variable;
        Element startExpression;
        Element endExpression;
        Element stepExpression;

        token = this.lexer.nextToken();

        if (token.getType() == TokenType.VARIABLE) {
            variable = new ElementVariable(token.getValue().toString());
        } else {
            throw new SmartScriptParserException("Expected variable in for loop tag");
        }

        startExpression = getElement(this.lexer.nextToken());
        endExpression = getElement(this.lexer.nextToken());

        if (!checkForLoopElement(startExpression) || !checkForLoopElement(endExpression)) {
            throw new SmartScriptParserException("Invalid elements inside for loop tag");
        }

        token = this.lexer.nextToken();
        if (token.equals(endToken)) {
            return new ForLoopNode(variable, startExpression, endExpression, null);
        } else {
            stepExpression = getElement(token);
            if (!this.lexer.nextToken().equals(endToken)) {
                throw new SmartScriptParserException("Too many elements in for loop tag");
            } else {
                if (checkForLoopElement(startExpression)) {
                    return new ForLoopNode(variable, startExpression, endExpression, stepExpression);
                } else {
                    throw new SmartScriptParserException("Invalid element inside for loop tag");
                }
            }
        }

    }

    /**
     * Checks if found element is a valid element inside for loop tag.
     *
     * @param element to be checked
     * @return true if given element is a valid element inside for loop tag
     */
    private boolean checkForLoopElement(Element element) {
        return element instanceof ElementVariable || element instanceof ElementConstantInteger
                || element instanceof ElementConstantDouble || element instanceof ElementString;
    }

    /**
     * This method will be called when parser detects open tag brackets and '=' as tag name and it will attempt to create a new
     * EchoNode based on the contents inside of the tag.
     *
     * @return new EchoNode
     */
    private Node getEchoNode() {
        Token token;
        Token endToken = new Token(TokenType.CLOSE_TAG, "$}");
        Element[] elements = new Element[20]; //I will be back cause this cant be right
        int index = 0;

        token = this.lexer.nextToken();

        while (!token.equals(endToken)) {
            elements[index] = getElement(token);
            index++;
            token = this.lexer.nextToken();
        }

        return new EchoNode(elements);
    }

    /**
     * This method will create appropriate element for given token.
     *
     * @param token from which an element will be created
     * @return element created from token
     */
    private Element getElement(Token token) {
        Element element;
        element = switch (token.getType()) {
            case STRING -> new ElementString(token.getValue().toString());
            case INTEGER -> new ElementConstantInteger(Integer.parseInt(token.getValue().toString()));
            case DOUBLE -> new ElementConstantDouble(Double.parseDouble(token.getValue().toString()));
            case VARIABLE -> new ElementVariable(token.getValue().toString());
            case FUNCTION -> new ElementFunction(token.getValue().toString());
            case OPERATOR -> new ElementOperator(token.getValue().toString());
            default -> throw new SmartScriptParserException("Invalid element");
        };

        return element;
    }

    /**
     * Returns the document node that was created while parsing the given text.
     *
     * @return document node created while parsing
     */
    public DocumentNode getDocumentNode() {
        return this.document;
    }

}

