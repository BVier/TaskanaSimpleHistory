package pro.taskana.simplehistory.query;

import pro.taskana.BaseQuery;
import pro.taskana.TimeInterval;
import pro.taskana.simplehistory.impl.HistoryEventImpl;

/**
 * HistoryQuery for generating dynamic sql.
 */
public interface HistoryQuery extends BaseQuery<HistoryEventImpl, HistoryQueryColumnName> {

     /**
     * Add your id to your query.
     *
     * @param i
     *            as String
     * @return the query
     */
    HistoryQuery idIn(long... i);

    /**
     * Add your type to your query.
     *
     * @param type
     *            as String
     * @return the query
     */
    HistoryQuery typeIn(String... type);

    /**
     * Add your userId to your query.
     *
     * @param userId
     *            as String
     * @return the query
     */
    HistoryQuery userIdIn(String... userId);

    /**
     * Add your userId to your query.
     *
     * @param createdIn
     *            the {@link TimeInterval} within which the searched-for classifications were created.
     * @return the query
     */
    HistoryQuery createdIn(TimeInterval... createdIn);

    /**
     * Add your comment to your query. It will be compared in SQL with an LIKE. If you use a wildcard like % then it
     * will be transmitted to the database.
     *
     * @param commentLike
     *            as String
     * @return the query
     */
    HistoryQuery commentLike(String commentLike);

    /**
     * Add your workbasketKey to your query.
     *
     * @param workbasketKey
     *            as String
     * @return the query
     */
    HistoryQuery workbasketKeyIn(String... workbasketKey);

    /**
     * Add your taskId to your query.
     *
     * @param taskId
     *            as String
     * @return the query
     */
    HistoryQuery taskIdIn(String... taskId);

}
