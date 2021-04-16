package hr.fer.oprpp1.hw04.db;

import java.util.List;

/**
 * Class representing a query filter. This object will execute the parsed query.
 */
public class QueryFilter implements IFilter {
    private List<ConditionalExpression> listQuery;

    /**
     * Creates a new QueryFilter that will execute ConditionalExpressions given as argument.
     *
     * @param listQuery list of expressions to be executed
     */
    public QueryFilter(List<ConditionalExpression> listQuery) {
        this.listQuery = listQuery;
    }

    /**
     * Returns true if given record satisfied all expressions from query.
     *
     * @param record to be filtered
     * @return true if record passes the filter
     */
    @Override
    public boolean accepts(StudentRecord record) {
        boolean result = true;

        for (ConditionalExpression expression : listQuery) {
            result = result &&
                    expression.getComparisonOperator().satisfied(expression.getFieldGetter().get(record), expression.getStringLiteral());
        }

        return result;
    }
}
