package acceptance.historyquery;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.List;

import org.junit.Test;

import acceptance.AbstractAccTest;
import pro.taskana.BaseQuery.SortDirection;
import pro.taskana.TimeInterval;
import pro.taskana.simplehistory.impl.HistoryEventImpl;
import pro.taskana.simplehistory.impl.SimpleHistoryServiceImpl;
import pro.taskana.simplehistory.query.HistoryQueryColumnName;

public class QueryHistoryAccTest extends AbstractAccTest {

    private SimpleHistoryServiceImpl historyService;

    public QueryHistoryAccTest() {
        super();
        this.historyService = (SimpleHistoryServiceImpl) taskanaHistoryEngine.getTaskanaHistoryService();
    }
    
    @Test
    public void testQueryAttributes() {
        int[] results = new int[7];
        int[] expected = {2,3,2,2,2,2,2};
        
        List<HistoryEventImpl> returnValues = historyService.createHistoryQuery().idIn(1,3).list();
        results[0] = returnValues.size(); //should be 2

        returnValues = historyService.createHistoryQuery().typeIn("CREATE").list();
        results[1] = returnValues.size(); //should be 3

        returnValues = historyService.createHistoryQuery().userIdIn("admin").list();
        results[2] = returnValues.size(); //should be 2

        TimeInterval timeInterval = new TimeInterval(Instant.now().minusSeconds(10), Instant.now());
        returnValues = historyService.createHistoryQuery().createdIn(timeInterval).list();
        results[3] = returnValues.size(); //should be 2

        returnValues = historyService.createHistoryQuery().commentLike("created %").list();
        results[4] = returnValues.size(); //should be 2

        returnValues = historyService.createHistoryQuery().workbasketKeyIn("WBI:100000000000000000000000000000000001").list();
        results[5] = returnValues.size(); //should be 2

        returnValues = historyService.createHistoryQuery().taskIdIn("TKI:000000000000000000000000000000000000").list();
        results[6] = returnValues.size(); //should be 2
        
        assertArrayEquals(expected, results);
    }
    
    @Test
    public void testQueryListOffset() {
        List<HistoryEventImpl> result = historyService.createHistoryQuery().list(1, 2);
        List<HistoryEventImpl> wrongList = historyService.createHistoryQuery().list();

        assertEquals(2, result.size());

        assertNotEquals(wrongList.get(0).getId(), result.get(0).getId());
        assertEquals(wrongList.get(1).getId(), result.get(0).getId());
    }
    
    @Test
    public void testCorrectResultWithWrongConstraints() {
        // To large limit should not throw an error
        List<HistoryEventImpl> result = historyService.createHistoryQuery().list(1, 1000);
        List<HistoryEventImpl> wrongList = historyService.createHistoryQuery().list();
        assertEquals(2, result.size());

        // To large offset should return an empty list
        result = historyService.createHistoryQuery().list(100,1000);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testListValues() {
        int[] listSize = new int[7];
        int[] expected = {3,1,2,2,3,2,2};
        
        List<String> returnedList = historyService.createHistoryQuery().listValues(HistoryQueryColumnName.ID, null);
        listSize[0] = returnedList.size(); //should be 3

        returnedList = historyService.createHistoryQuery().listValues(HistoryQueryColumnName.TYPE, null);
        listSize[1] = returnedList.size(); // should be 1

        returnedList = historyService.createHistoryQuery().listValues(HistoryQueryColumnName.USER_ID, null);
        listSize[2] = returnedList.size(); //should be 2

        returnedList = historyService.createHistoryQuery().listValues(HistoryQueryColumnName.CREATED, null);
        listSize[3] = returnedList.size(); //should be 2

        returnedList = historyService.createHistoryQuery().listValues(HistoryQueryColumnName.COMMENT, null);
        listSize[4] = returnedList.size(); //should be 3

        returnedList = historyService.createHistoryQuery().listValues(HistoryQueryColumnName.WORKBASKET_KEY, null);
        listSize[5] = returnedList.size(); //should be 2

        returnedList = historyService.createHistoryQuery().listValues(HistoryQueryColumnName.TASK_ID, null);
        listSize[6] = returnedList.size(); //should be 2

        assertArrayEquals(expected, listSize);
    }

    @Test
    public void testListValuesAscendingAndDescending() {
        List<String> defaultList = historyService.createHistoryQuery().listValues(HistoryQueryColumnName.ID, null);
        List<String> ascendingList = historyService.createHistoryQuery().listValues(HistoryQueryColumnName.ID, SortDirection.ASCENDING);
        List<String> descendingList = historyService.createHistoryQuery().listValues(HistoryQueryColumnName.ID, SortDirection.DESCENDING);

        assertEquals(3, ascendingList.size());
        assertEquals("1", ascendingList.get(0));
        assertArrayEquals(defaultList.toArray(), ascendingList.toArray());
        assertEquals(ascendingList.get(2), descendingList.get(0)); //Last element in ascendingList should be the same as first in the descendingList
    }

    @Test
    public void testSingle() {
        HistoryEventImpl single = historyService.createHistoryQuery().idIn(2).single();
        assertEquals(2, single.getId());
        assertEquals("CREATE", single.getType());

        single = historyService.createHistoryQuery().typeIn("CREATE","xy").single();
        assertEquals(1, single.getId());
    }

    @Test
    public void testCount() {
        long count = historyService.createHistoryQuery().idIn(2).count();
        assertEquals(1,count);

        count = historyService.createHistoryQuery().count();
        assertEquals(3, count);

        count = historyService.createHistoryQuery().userIdIn("klaus", "arnold", "benni").count();
        assertEquals(0, count);
    }
}
