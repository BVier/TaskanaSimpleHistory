package pro.taskana.simplehistory.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.taskana.ClassificationQuery;
import pro.taskana.TimeInterval;
import pro.taskana.BaseQuery.SortDirection;
import pro.taskana.simplehistory.impl.mappings.HistoryQueryMapper;
import pro.taskana.simplehistory.query.HistoryQuery;
import pro.taskana.simplehistory.query.HistoryQueryColumnName;

public class HistoryQueryImpl implements HistoryQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryQueryImpl.class);

	private TaskanaHistoryEngineImpl taskanaHistoryEngine;
	private HistoryQueryMapper historyQueryMapper;

	private HistoryQueryColumnName columnName;
	private List<String> orderBy;
	private List<String> orderColumns;
	private int max_rows = -1;  // limit for rows. used to make list(offset, limit) and single() more efficient.
	private long[] idIn;
	private String[] taskIdIn;
	private String[] workbasketKeyIn;
	private String[] typeIn;
	private String[] userIdIn;
	private String commentLike;
	private TimeInterval[] createdIn;

	public HistoryQueryImpl(TaskanaHistoryEngineImpl taskanaHistoryEngineImpl, HistoryQueryMapper historyQueryMapper) {
		this.taskanaHistoryEngine = taskanaHistoryEngineImpl;
		this.historyQueryMapper = historyQueryMapper;
        this.orderBy = new ArrayList<>();
        this.orderColumns = new ArrayList<>();
	}

    @Override
    public HistoryQueryImpl idIn(long... id) {
        this.idIn = id;
        return this;
    }

    @Override
    public HistoryQuery typeIn(String... type) {
        this.typeIn = type;
        return this;
    }

    @Override
    public HistoryQuery userIdIn(String... userId) {
        this.userIdIn = userId;
        return this;
    }

    @Override
    public HistoryQuery createdIn(TimeInterval... createdIn) {
        this.createdIn = createdIn;
        return this;
    }

    @Override
    public HistoryQuery commentLike(String commentLike) {
        this.commentLike = commentLike;
        return this;
    }

    @Override
    public HistoryQuery workbasketKeyIn(String... workbasketKey) {
        this.workbasketKeyIn = workbasketKey;
        return this;
    }

    @Override
    public HistoryQuery taskIdIn(String... taskId) {
        this.taskIdIn = taskId;
        return this;
    }

    @Override
    public List<HistoryEventImpl> list() {
        LOGGER.debug("entry to list(), this = {}", this);
        List<HistoryEventImpl> result = new ArrayList<>();
        try {
            taskanaHistoryEngine.openConnection();
            result = historyQueryMapper.queryHistoryEvent(this);
            LOGGER.debug("transaction was successful. Result: {}", result.toString());
            return result;
        } catch (SQLException e) {
            LOGGER.error("Method openConnection() could not open a connection to the database.",
                    e.getCause());
            return result;
        } catch (NullPointerException npe) {
            LOGGER.error("No History Event found.");
            return result;
        } finally {
            taskanaHistoryEngine.returnConnection();
        }
    }

    @Override
    public List<HistoryEventImpl> list(int offset, int limit) {
    	LOGGER.debug("entry to list({},{}), this = {}", offset, limit, this);
        List<HistoryEventImpl> result = new ArrayList<>();
        try {
            taskanaHistoryEngine.openConnection();
            this.max_rows= offset + limit;
            result = historyQueryMapper.queryHistoryEvent(this);
            LOGGER.debug("transaction was successful. Result: {}", result.toString());
            limit = Math.min(result.size() - offset, limit);
            if(result.size() > offset) {
                return result.subList(offset, offset + limit);
            } else {
                return new ArrayList<>();
            }
        } catch (SQLException e) {
            LOGGER.error("Method openConnection() could not open a connection to the database.",
                    e.getCause());
            return result;
        } catch (NullPointerException npe) {
            LOGGER.error("No History Event found.");
            return result;
        } finally {
            taskanaHistoryEngine.returnConnection();
            this.max_rows = -1;
        }
    }

    @Override
    public List<String> listValues(HistoryQueryColumnName dbColumnName, SortDirection sortDirection) {
    	LOGGER.debug("entry to listValues() of column {} with sortDirection {}, this {}", dbColumnName, sortDirection, this);
        List<String> result = new ArrayList<>();
        this.columnName = dbColumnName;
        this.orderBy.clear();
        this.addOrderCriteria(columnName.toString(), sortDirection);
        
        try {
            taskanaHistoryEngine.openConnection();
            result = historyQueryMapper.queryHistoryColumnValues(this);
            LOGGER.debug("transaction was successful. Result: {}", result.toString());
            return result;
        } catch (SQLException e) {
            LOGGER.error("Method openConnection() could not open a connection to the database.",
                    e.getCause());
            return result;
        } catch (NullPointerException npe) {
            LOGGER.error("No History Event found.");
            return result;
        } finally {
            taskanaHistoryEngine.returnConnection();
        }
    }

    @Override
    public HistoryEventImpl single() {
        LOGGER.debug("entry to list(), this = {}", this);
        HistoryEventImpl result = new HistoryEventImpl();
        try {
            taskanaHistoryEngine.openConnection();
            this.max_rows = 1;
            result = historyQueryMapper.queryHistoryEvent(this).get(0);
            LOGGER.debug("transaction was successful. Result: {}", result.toString());
            return result;
        } catch (SQLException e) {
            LOGGER.error("Method openConnection() could not open a connection to the database.",
                    e.getCause());
            return result;
        } catch (NullPointerException npe) {
            LOGGER.error("No History Event found.");
            return result;
        } finally {
            taskanaHistoryEngine.returnConnection();
            this.max_rows = -1;
        }
    }

    @Override
    public long count() {
    	LOGGER.debug("entry to count(), this = {}", this);
        try {
            taskanaHistoryEngine.openConnection();
            long result = historyQueryMapper.countHistoryEvent(this);
            LOGGER.debug("transaction was successful. Result: {}", result);
            return result;
        } catch (SQLException e) {
            LOGGER.error("Method openConnection() could not open a connection to the database.",
                    e.getCause());
            return -1;
        } catch (NullPointerException npe) {
            LOGGER.error("No History Event found.");
            return -1;
        } finally {
            taskanaHistoryEngine.returnConnection();
        }
    }

    private void addOrderCriteria(String columnName, SortDirection sortDirection) {
        String orderByDirection = " " + (sortDirection == null ? SortDirection.ASCENDING : sortDirection);
        orderBy.add(columnName.toString() + orderByDirection);
        orderColumns.add(columnName.toString());
    }

}
