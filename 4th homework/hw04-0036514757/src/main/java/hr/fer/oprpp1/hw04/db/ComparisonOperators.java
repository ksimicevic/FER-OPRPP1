package hr.fer.oprpp1.hw04.db;

/**
 * Class with several ComparisonOperators constants.
 */
public class ComparisonOperators {
    public static final IComparisonOperator LESS = (value1, value2) -> value1.compareTo(value2) < 0;
    public static final IComparisonOperator LESS_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) <= 0;
    public static final IComparisonOperator GREATER = (value1, value2) -> value1.compareTo(value2) > 0;
    public static final IComparisonOperator GREATER_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) >= 0;
    public static final IComparisonOperator EQUALS = String::equals;
    public static final IComparisonOperator NOT_EQUALS = (value1, value2) -> !value1.equals(value2);

    public static final IComparisonOperator LIKE = ((value1, value2) -> {
        int wildcardPosition = value2.indexOf('*');

        if(wildcardPosition == 0) {
            return value1.endsWith(value2.substring(1));
        } else if(wildcardPosition == value2.length() - 1) {
            return value1.startsWith(value2.substring(0, value2.length() - 1));
        } else {
            String[] parts = value2.split("\\*");

            if(parts.length > 2) {
                throw new IllegalArgumentException("There are more than 1 wildcard characters in expression");
            }

            return value1.matches(parts[0] + ".*" + parts[1]);
        }
    });

}
