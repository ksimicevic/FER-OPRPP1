package hr.fer.oprpp1.hw04.db;

/**
 * This class represents a comparison operator.
 */
public interface IComparisonOperator {
    /**
     * Returns true if given arguments satisfy the comparison operator.
     *
     * @param value1 first argument
     * @param value2 second argument
     * @return true if arguments satisfy the comparison operator
     */
    boolean satisfied(String value1, String value2);
}
