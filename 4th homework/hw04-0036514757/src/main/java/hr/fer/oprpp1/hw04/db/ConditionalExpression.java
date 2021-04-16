package hr.fer.oprpp1.hw04.db;

/**
 * Class representing a conditional expression in query.
 */
public class ConditionalExpression {
    private IFieldValueGetter fieldGetter;
    private String stringLiteral;
    private IComparisonOperator comparisonOperator;

    /**
     * Creates a new conditional expression.
     *
     * @param fieldGetter        fieldGetter
     * @param stringLiteral      stringLiteral
     * @param comparisonOperator comparisonOperator
     */
    public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral, IComparisonOperator comparisonOperator) {
        this.fieldGetter = fieldGetter;
        this.stringLiteral = stringLiteral;
        this.comparisonOperator = comparisonOperator;
    }

    public IFieldValueGetter getFieldGetter() {
        return fieldGetter;
    }

    public String getStringLiteral() {
        return stringLiteral;
    }

    public IComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }
}
