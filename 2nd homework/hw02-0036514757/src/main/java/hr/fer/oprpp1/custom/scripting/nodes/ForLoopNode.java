package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.*;

import java.util.Objects;

/**
 * Represents a node that contains all contents inside for loop tag.
 */
public class ForLoopNode extends Node {
    private ElementVariable variable;
    private Element startExpression;
    private Element endExpression;
    private Element stepExpression;

    /**
     * Creates a new for loop node with given parameters. For loop tag must contain at least 3 elements and at max 4.
     *
     * @param variable first element in the tag, mandatory
     * @param startExpression second element in the tag, mandatory
     * @param endExpression third element in the tag, mandatory
     * @param stepExpression fourth element in the tag, optional, can be null
     */
    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
        this.variable = variable;
        this.startExpression = startExpression;
        this.endExpression = endExpression;
        this.stepExpression = stepExpression;
    }

    public ElementVariable getVariable() {
        return variable;
    }

    public Element getStartExpression() {
        return startExpression;
    }

    public Element getEndExpression() {
        return endExpression;
    }

    public Element getStepExpression() {
        return stepExpression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForLoopNode that = (ForLoopNode) o;
        return Objects.equals(variable, that.variable) &&
                Objects.equals(startExpression, that.startExpression) &&
                Objects.equals(endExpression, that.endExpression) &&
                Objects.equals(stepExpression, that.stepExpression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable, startExpression, endExpression, stepExpression);
    }
}
