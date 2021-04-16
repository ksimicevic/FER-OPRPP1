package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.Collection;
import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementFunction;
import hr.fer.oprpp1.custom.scripting.elems.ElementString;

/**
 * Represents the main node of a document. All other nods will be the children of this node.
 */
public class DocumentNode extends Node {

    /**
     * Creates close to original text content from the document tree model. Text won't be the same as original text used
     * to create the given document as some information will be lost while parsing the text.
     *
     * @return text that was used to create given document
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (this.getCol() == null || this.getCol().isEmpty()) {
            return "";
        }

        Collection.ElementsGetter getter = this.getCol().createElementsGetter();

        while (getter.hasNextElement()) {
            Object currentNode = getter.getNextElement();

            if (currentNode instanceof TextNode) {
                TextNode node = (TextNode) currentNode;
                this.appendTextNode(node, sb);
            } else if (currentNode instanceof ForLoopNode) {
                ForLoopNode node = (ForLoopNode) currentNode;
                this.appendForLoopNode(node, sb);
            } else if (currentNode instanceof EchoNode) {
                EchoNode node = (EchoNode) currentNode;
                this.appendEchoNode(node, sb);
            }
        }
        return sb.toString();
    }

    /**
     * Turns the contents of given text node back into the form that was used to create this node.
     *
     * @param textNode text node that will be turned back into text
     * @param sb       StringBuilder used to create the original text from DocumentNode
     */
    private void appendTextNode(TextNode textNode, StringBuilder sb) {
        String s = textNode.getString().asText();
        sb.append(s.replaceAll("\\{", "\\\\{"));
    }

    /**
     * Turns the contents of given for loop node back into the form that was used to create this node. This method will also
     * convert all the forLoopNode children back into the text if there are any.
     *
     * @param forLoopNode for loop node that will be turned back into text
     * @param sb          StringBuilder used to create the original text from DocumentNode
     */
    private void appendForLoopNode(ForLoopNode forLoopNode, StringBuilder sb) {
        sb.append("{$FOR")
                .append(" ")
                .append(forLoopNode.getVariable().asText())
                .append(" ")
                .append(forLoopNode.getStartExpression().asText())
                .append(" ")
                .append(forLoopNode.getEndExpression().asText())
                .append(" ");

        if (forLoopNode.getStepExpression() != null) {
            sb.append(forLoopNode.getStepExpression().asText()).append(" ");
        }

        sb.append("$}");

        int childNumber = forLoopNode.numberOfChildren();
        for (int i = 0; i < childNumber; i++) {
            Object childNode = forLoopNode.getChild(i);

            if (childNode instanceof TextNode) {
                this.appendTextNode((TextNode) childNode, sb);
            } else if (childNode instanceof ForLoopNode) {
                this.appendForLoopNode((ForLoopNode) childNode, sb);
            } else if (childNode instanceof EchoNode) {
                this.appendEchoNode((EchoNode) childNode, sb);
            }
        }
        sb.append("{$END$}");
    }

    /**
     * Turns the contents of given echo node back into the form that was used to create this node.
     *
     * @param echoNode text node that will be turned back into text
     * @param sb       StringBuilder used to create the original text from DocumentNode
     */
    private void appendEchoNode(EchoNode echoNode, StringBuilder sb) {
        sb.append("{$=");
        for (Element e : echoNode.getElements()) {
            if (e != null) {
                if (e instanceof ElementFunction) {
                    sb.append(" @").append(e.asText()).append(" ");
                } else if (e instanceof ElementString) {
                    String s = e.asText();
                    String returnedEscapeChars = s.substring(1, s.length() - 1).replaceAll("\"", "\\\\\"");
                    sb.append(" \"").append(returnedEscapeChars).append("\"");
                } else {
                    sb.append(" ").append(e.asText()).append(" ");
                }
            }
        }
        sb.append("$}");
    }

    /**
     * Returns true if two document nodes are equal.
     *
     * @param o object to be tested
     * @return true if two document nodes are equal
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentNode that = (DocumentNode) o;
        return this.getCol().equals(that.getCol());
    }

}
