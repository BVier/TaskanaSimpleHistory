package pro.taskana.simplehistory.query;

import pro.taskana.QueryColumnName;

/**
 * Enum containing the column names for @see pro.taskana.mappings.QueryMapper#queryClassificationColumnValues(pro.taskana.impl.ClassificationQueryImpl).
 *
 * @author jsa
 */
public enum HistoryQueryColumnName implements QueryColumnName {
    ID("id"),
    TYPE("type"),
    USER_ID("user_id"),
    CREATED("created"),
    COMMENT("comment"),
    TASK_ID("task_id"),
    WORKBASKET_KEY("workbasket_key");

    private String name;
    HistoryQueryColumnName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
