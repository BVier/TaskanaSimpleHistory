package pro.taskana.simplehistory.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.validateMockitoUsage;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import pro.taskana.TaskanaEngine;
import pro.taskana.TimeInterval;
import pro.taskana.impl.ClassificationQueryImpl;
import pro.taskana.simplehistory.impl.mappings.HistoryQueryMapper;

/**
 * Unit Test for SimpleHistoryServiceImplTest.
 *
 * @author BV
 */
@RunWith(PowerMockRunner.class)
public class HistoryQueryImplTest {

    private HistoryQueryImpl historyQueryImpl;
    
    @Mock
    private TaskanaHistoryEngineImpl taskanaHistoryEngineMock;
    
    @Mock
    private HistoryQueryMapper historyQueryMock;
    

    @Before
    public void setup() {
        historyQueryImpl = new HistoryQueryImpl(taskanaHistoryEngineMock, historyQueryMock);
    }

    @Test
    public void testShouldReturnList() throws SQLException {
        List<HistoryEventImpl> returnList = new ArrayList<>();
        returnList.add(createHistoryEvent(4, "abcd", "T22", "car", "BV", "this was important", null));
        TimeInterval interval = new TimeInterval(Instant.now().minusNanos(1000), Instant.now());

        doNothing().when(taskanaHistoryEngineMock).openConnection();
        doNothing().when(taskanaHistoryEngineMock).returnConnection();
        doReturn(returnList).when(historyQueryMock).queryHistoryEvent(historyQueryImpl);

        List<HistoryEventImpl> result = historyQueryImpl
                .idIn(4)
                .taskIdIn("TKI:01")
                .workbasketKeyIn("T22","some_long_long, long loooooooooooooooooooooooooooooooooooong String.")
                .typeIn(null)
                .userIdIn("BV")
                .commentLike("%as important")
                .createdIn(interval)
                .list();

        validateMockitoUsage();
        assertArrayEquals(returnList.toArray(), result.toArray());
    }
    
    private HistoryEventImpl createHistoryEvent(long id, String taskId, String workbasketKey, String type, String userId, String comment, Instant created) {
    	HistoryEventImpl he = new HistoryEventImpl();
    	he.setId(id);
    	he.setTaskId(taskId);
    	he.setWorkbasketKey(workbasketKey);
    	he.setType(type);
    	he.setUserId(userId);
    	he.setComment(comment);
    	he.setCreated(created);
    	return he;
    }
}
