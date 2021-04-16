package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a query parser.
 */
public class QueryParser {
    private final String query;
    private int index;
    private final char[] chars;
    private List<ConditionalExpression> listQuery;

    /**
     * Creates a new query parser and parses the given query.
     *
     * @param query to be parsed
     */
    public QueryParser(String query) {
        this.query = query;
        this.index = 0;
        this.chars = query.toCharArray();
        this.listQuery = new ArrayList<>();
        this.parse();
    }

    /**
     * Method called immediately after QueryParser object is created. It will parse the query and put the results of
     * parsing into collection listQuery.
     */
    private void parse() {
        findQuery();

        while (true) {
            if (index < this.chars.length) {
                if (!query.substring(index, query.length() - 1).isEmpty()) {
                    listQuery.add(nextConditionalExpression());
                } else {
                    break;
                }
            } else {
                break;
            }
        }
    }

    /**
     * Checks if query begins with key word "query".
     *
     * @throws IllegalStateException if key word hasn't been found
     */
    private void findQuery() {
        skipWhiteSpaces();
        String nextFive = String.valueOf(chars, index, 5);

        if (!nextFive.toLowerCase().equals("query")) {
            throw new IllegalStateException("Expected query here but didn't find it");
        } else {
            index += 5;
        }
        skipWhiteSpaces();
    }

    /**
     * Finds and returns new ConditionalExpression that is found in parsed query.
     *
     * @return new parsed ConditionalExpression
     */
    private ConditionalExpression nextConditionalExpression() {
        IFieldValueGetter fieldValueGetter;
        IComparisonOperator comparisonOperator;
        String stringLiteral;

        skipWhiteSpaces();
        fieldValueGetter = nextFieldGetter();
        skipWhiteSpaces();
        comparisonOperator = nextComparisonOperator();
        skipWhiteSpaces();
        stringLiteral = nextStringLiteral();
        findAnd();

        return new ConditionalExpression(fieldValueGetter, stringLiteral, comparisonOperator);
    }

    /**
     * Checks if key word "and" is found after most recent ConditionalExpression has been parsed.
     *
     * @throws IllegalStateException if key word isn't found
     */
    private void findAnd() {
        skipWhiteSpaces();

        if (index >= this.chars.length - 1) {
            return;
        }

        String nextThree = String.valueOf(chars, index, 3);

        if (!nextThree.toLowerCase().equals("and")) {
            throw new IllegalStateException("Expected AND here but didn't find it");
        } else {
            index += 3;
        }
    }

    /**
     * Finds and returns the next FieldValueGetter in query.
     *
     * @return new FieldValueGetter from parsed query
     */
    private IFieldValueGetter nextFieldGetter() {
        StringBuilder sb = new StringBuilder();
        while (index != this.chars.length - 1) {
            if (Character.isLetter(chars[index])) {
                sb.append(chars[index]);
                index++;
            } else {
                break;
            }
        }
        String fieldName = sb.toString();
        return getFieldGetter(fieldName);
    }

    /**
     * For given string, finds and returns corresponding FieldValueGetter if such exists.
     *
     * @param field for which this method will attempt to find a FieldValueGetter
     * @return new FieldValueGetter for given argument
     * @throws IllegalArgumentException if method failed to find a FieldValueGetter for given argument
     */
    private IFieldValueGetter getFieldGetter(String field) {
        return switch (field) {
            case "firstName" -> FieldValueGetters.FIRST_NAME;
            case "lastName" -> FieldValueGetters.LAST_NAME;
            case "jmbag" -> FieldValueGetters.JMBAG;
            default -> throw new IllegalArgumentException("Couldn't find a valid FieldValueGetter for given string");
        };
    }

    /**
     * Finds and returns the next ComparisonOperator in query.
     *
     * @return new ComparisonOperator from parsed query
     * @throws IllegalStateException if method fails to parse a comparison operator
     */
    private IComparisonOperator nextComparisonOperator() {
        StringBuilder sb = new StringBuilder();

        char currChar = chars[index];

        if (currChar == 'L') {
            sb.append(currChar);
            index++;

            for (int i = 0; i < 3; i++) {
                sb.append(chars[index++]);
            }

            if (sb.toString().equals("LIKE")) {
                return ComparisonOperators.LIKE;
            } else {
                throw new IllegalStateException("Expected LIKE operator but couldn't find it");
            }

        } else {
            if (currChar == '>' || currChar == '<') {
                char nextChar = chars[index + 1];
                if (nextChar == '=') {
                    sb.append(currChar).append(nextChar);
                    index += 2;
                } else {
                    sb.append(currChar);
                    index++;
                }
            } else if (currChar == '!') {
                char nextChar = chars[index + 1];
                if (nextChar == '=') {
                    sb.append("!=");
                    index += 2;
                } else {
                    throw new IllegalStateException("Expected = after ! but didn't find it");
                }
            } else {
                sb.append(currChar);
                index++;
            }
        }

        String operator = sb.toString();
        return getComparisonOperator(operator);
    }

    /**
     * For given string, finds and returns corresponding ComparisonOperator if such exists.
     *
     * @param operator for which this method will attempt to find a ComparisonOperator
     * @return new ComparisonOperator for given argument
     * @throws IllegalArgumentException if method failed to find a ComparisonOperator for given argument
     */
    private IComparisonOperator getComparisonOperator(String operator) {
        return switch (operator) {
            case "<" -> ComparisonOperators.LESS;
            case "<=" -> ComparisonOperators.LESS_OR_EQUALS;
            case ">" -> ComparisonOperators.GREATER;
            case ">=" -> ComparisonOperators.GREATER_OR_EQUALS;
            case "=" -> ComparisonOperators.EQUALS;
            case "LIKE" -> ComparisonOperators.LIKE;
            case "!=" -> ComparisonOperators.NOT_EQUALS;
            default -> throw new IllegalArgumentException("Couldn't find a valid ComparisonOperator for given operator");
        };
    }

    /**
     * Finds and returns the next String literal in query.
     *
     * @return new String literal from parsed query
     */
    private String nextStringLiteral() {
        StringBuilder sb = new StringBuilder();

        if (chars[index] == '"') {
            index++;
        } else {
            throw new IllegalStateException("Expected a string literal here, but didn't find it");
        }

        while (index != this.chars.length - 1) {
            if (chars[index] != '"') {
                sb.append(chars[index]);
                index++;
            } else {
                index++;
                break;
            }
        }

        return sb.toString();
    }

    /**
     * Skips all white spaces in query.
     */
    private void skipWhiteSpaces() {
        while (index != this.chars.length - 1) {
            if (Character.isWhitespace(chars[index])) {
                index++;
            } else {
                break;
            }
        }
    }

    /**
     * Returns true if query is in form "jmbag = 'xxx'";
     *
     * @return true if query consists of one conditional expression and that expression is in form "jmbag = 'xxx'"
     */
    public boolean isDirectQuery() {
        var query = listQuery.get(0);
        return listQuery.size() == 1 && query.getComparisonOperator().equals(ComparisonOperators.EQUALS)
                && query.getFieldGetter().equals(FieldValueGetters.JMBAG);
    }

    /**
     * If query is a direct query, return the queried jmbag.
     *
     * @return queried jmbag in direct query
     * @throws IllegalStateException if this method is called for query that is not a direct query
     */
    public String getQueriedJMBAG() {
        if (!isDirectQuery()) {
            throw new IllegalStateException("This query is not a direct query");
        } else {
            return listQuery.get(0).getStringLiteral();
        }
    }

    /**
     * Returns the list of all conditional expressions parsed from given query.
     *
     * @return list of parsed conditional expressions
     */
    public List<ConditionalExpression> getQuery() {
        return listQuery;
    }

    public static void main(String... args) {
        QueryParser qp1 = new QueryParser(" jmbag =\"0123456789\" ");
        System.out.println("isDirectQuery(): " + qp1.isDirectQuery()); // true
        System.out.println("jmbag was: " + qp1.getQueriedJMBAG()); // 0123456789
        System.out.println("size: " + qp1.getQuery().size()); // 1
        QueryParser qp2 = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");
        System.out.println("isDirectQuery(): " + qp2.isDirectQuery()); // false
// System.out.println(qp2.getQueriedJMBAG()); // would throw!
        System.out.println("size: " + qp2.getQuery().size()); // 2

    }
}

