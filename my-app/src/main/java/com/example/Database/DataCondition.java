package com.example.Database;

import java.util.ArrayList;

public class DataCondition {
    /**
     * Merge two conditions with a logical operator.
     * @param condition1    The first condition
     * @param condition2    The second condition
     * @param typeLogical   The logical operator to merge the conditions (e.g., "and", "or")
     * @return              The merged condition
     * @throws IllegalArgumentException If the logical operator is not "and" or "or"
     */
    public String mergeCondition(String condition1, String condition2, String typeLogical) {
        String condition = "";
        switch (typeLogical) {
            case "and":
                condition = condition1 + " AND " + condition2;
                break;
            case "or":
                condition = condition1 + " OR " + condition2;
                break;
            default:
                throw new IllegalArgumentException("Invalid merge type: " + typeLogical + "\n" 
                                                    + "The true merge types are: " +
                                                    "and" + ", " + 
                                                    "or");
        }
        return condition;
    }
    
    /**
     * Create a comparison condition.
     * @param leftParam         the left parameter
     * @param rightParam        the right parameter
     * @param typeComparison    the type of comparison (e.g., "equal", "notEqual", "greaterThan", "lessThan", "greaterThanOrEqual", "lessThanOrEqual")
     * @return                  the comparison condition
     * @throws IllegalArgumentException If the comparison type is not one of the valid types
     */
    public String comparisonCondition(String leftParam, String rightParam, String typeComparison) {
        String condition = "";
        switch (typeComparison) {
            case "equal":
                condition = leftParam + " = " + rightParam;
                break;
            case "notEqual":
                condition = leftParam + " != " + rightParam;
                break;
            case "greaterThan":
                condition = leftParam + " > " + rightParam;
                break;
            case "lessThan":
                condition = leftParam + " < " + rightParam;
                break;
            case "greaterThanOrEqual":
                condition = leftParam + " >= " + rightParam;
                break;
            case "lessThanOrEqual":
                condition = leftParam + " <= " + rightParam;
                break;
            default:
                throw new IllegalArgumentException("Invalid comparison type: " + typeComparison + "\n" 
                                                    + "The true comparison types are: " +
                                                    "equal" + ", " + 
                                                    "notEqual" + ", " +
                                                    "greaterThan" + ", " + 
                                                    "lessThan" + ", " +
                                                    "greaterThanOrEqual" + ", " +
                                                    "lessThanOrEqual");
        }
        return condition;
    }

    /**
     * Create a condition for a range of values.
     * @param param             the parameter to check
     * @param leftParam         the left parameter of the range
     * @param rightParam        the right parameter of the range
     * @return                  the range condition
     * @throws IllegalArgumentException If the left parameter is greater than the right parameter
     */
    public String betweenCondtion(String param, String leftParam, String rightParam) {
        return param + " BETWEEN " + leftParam + " AND " + rightParam;
    }

    /**
     * Create a condition for a list of values.
     * @param param     the parameter to check
     * @param values    the list of values
     * @return          the new condition
     */
    public String inCondition(String param, ArrayList<String> values) {
        String condition = param + " IN (";
        for (int i = 0; i < values.size(); i++) {
            condition += values.get(i);
            if (i < values.size() - 1) {
                condition += ", ";
            }
        }
        condition += ")";
        return condition;
    }

    /**
     * Create a condition for a list of values.
     * @param param     the parameter to check
     * @param values    the list of values
     * @return          the condition for the list of values
     */
    public String likeCondition(String param, String value) {
        return param + " LIKE " + value;
    }

    /**
     * Create a condition for a list of values.
     * @param param     the parameter to check
     * @return          the new condition
     */
    public String isNullCondition(String param) {
        return param + " IS NULL";
    }

    /**
     * Create a condition for a list of values.
     * @param param     the parameter to check
     * @return          the new condition
     */
    public String isNotNullCondition(String param) {
        return param + " IS NOT NULL";
    }

    /**
     * Create a condition for a list of values.
     * @param param     the parameter to check
     * @return          the new condition
     */
    public String distinctCondition(String param) {
        return "DISTINCT " + param;
    }
}
