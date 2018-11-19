package pro.taskana.simplehistory.impl.mappings;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import pro.taskana.impl.ClassificationQueryImpl;
import pro.taskana.simplehistory.impl.HistoryEventImpl;
import pro.taskana.simplehistory.impl.HistoryQueryImpl;

/**
 * This class is the mybatis mapping of historyQueries.
 */
public interface HistoryQueryMapper {

    @Select(
        "<script>"
            + "SELECT ID, TYPE, USER_ID, CREATED, COMMENT, WORKBASKET_KEY, TASK_ID "
            + "FROM HISTORY_EVENTS"
            + "<where>"
            + "<if test='idIn != null'>AND ID IN (<foreach item='item' collection='idIn' separator=',' >#{item}</foreach>)</if> "
            + "<if test='taskIdIn != null'>AND TASK_ID IN (<foreach item='item' collection='taskIdIn' separator=',' >#{item}</foreach>)</if> "
            + "<if test='workbasketKeyIn != null'>AND WORKBASKET_KEY IN (<foreach item='item' collection='workbasketKeyIn' separator=',' >#{item}</foreach>)</if> "
            + "<if test='typeIn != null'>AND TYPE IN (<foreach item='item' collection='typeIn' separator=',' >#{item}</foreach>)</if> "
            + "<if test='createdIn !=null'> AND ( <foreach item='item' collection='createdIn' separator=' OR ' > ( <if test='item.begin!=null'> CREATED &gt;= #{item.begin} </if> <if test='item.begin!=null and item.end!=null'> AND </if><if test='item.end!=null'> CREATED &lt;=#{item.end} </if>)</foreach>)</if> "
            + "<if test='userIdIn != null'>AND USER_ID IN (<foreach item='item' collection='userIdIn' separator=',' >#{item}</foreach>)</if> "
            + "<if test='commentLike != null'>AND COMMENT LIKE #{commentLike}</if> "
            + "</where>"
            + "<if test='max_rows > 0'> FETCH FIRST #{max_rows} ROWS ONLY </if>"
            + "</script>")
    @Results(value = {
            @Result(property = "id", column = "ID"),
            @Result(property = "taskId", column = "TASK_ID"),
            @Result(property = "type", column = "TYPE"),
            @Result(property = "created", column = "CREATED"),
            @Result(property = "userId", column = "USER_ID"),
            @Result(property = "comment", column = "COMMENT"),
            @Result(property = "workbasketKey", column = "WORKBASKET_KEY")
        })
    List<HistoryEventImpl> queryHistoryEvent(HistoryQueryImpl historyEventQuery);

    @Select(
        "<script>"
            + "SELECT COUNT(ID) "
            + "FROM HISTORY_EVENTS"
            + "<where>"
            + "<if test='idIn != null'>AND ID IN (<foreach item='item' collection='idIn' separator=',' >#{item}</foreach>)</if> "
            + "<if test='taskIdIn != null'>AND TASK_ID IN (<foreach item='item' collection='taskIdIn' separator=',' >#{item}</foreach>)</if> "
            + "<if test='workbasketKeyIn != null'>AND WORKBASKET_KEY IN (<foreach item='item' collection='workbasketKeyIn' separator=',' >#{item}</foreach>)</if> "
            + "<if test='typeIn != null'>AND TYPE IN (<foreach item='item' collection='typeIn' separator=',' >#{item}</foreach>)</if> "
            + "<if test='createdIn !=null'> AND ( <foreach item='item' collection='createdIn' separator=' OR ' > ( <if test='item.begin!=null'> CREATED &gt;= #{item.begin} </if> <if test='item.begin!=null and item.end!=null'> AND </if><if test='item.end!=null'> CREATED &lt;=#{item.end} </if>)</foreach>)</if> "
            + "<if test='userIdIn != null'>AND USER_ID IN (<foreach item='item' collection='userIdIn' separator=',' >#{item}</foreach>)</if> "
            + "<if test='commentLike != null'>AND COMMENT LIKE #{commentLike}</if> "
            + "</where>"
            + "</script>")
    long countHistoryEvent(HistoryQueryImpl historyEventQuery);

    @Select("<script>SELECT DISTINCT ${columnName} "
            + "FROM HISTORY_EVENTS"
            + "<where>"
            + "<if test='idIn != null'>AND ID IN(<foreach item='item' collection='idIn' separator=',' >#{item}</foreach>)</if> "
            + "<if test='taskIdIn != null'>AND TASK_ID IN(<foreach item='item' collection='taskIdIn' separator=',' >#{item}</foreach>)</if> "
            + "<if test='workbasketKeyIn != null'>AND WORKBASKET_KEY IN(<foreach item='item' collection='workbasketKeyIn' separator=',' >#{item}</foreach>)</if> "
            + "<if test='createdIn !=null'> AND ( <foreach item='item' collection='createdIn' separator=' OR ' > ( <if test='item.begin!=null'> CREATED &gt;= #{item.begin} </if> <if test='item.begin!=null and item.end!=null'> AND </if><if test='item.end!=null'> CREATED &lt;=#{item.end} </if>)</foreach>)</if> "
            + "<if test='typeIn != null'>AND TYPE IN(<foreach item='item' collection='nameIn' separator=',' >#{item}</foreach>)</if> "
            + "<if test='userIdIn != null'>AND USER_ID IN(<foreach item='item' collection='nameIn' separator=',' >#{item}</foreach>)</if> "
            + "<if test='commentLike != null'>AND COMMENT like #{commentLike}</if> "
            + "</where>"
            + "<if test='!orderBy.isEmpty()'>ORDER BY <foreach item='item' collection='orderBy' separator=',' >${item}</foreach></if> "
            + "<if test=\"_databaseId == 'db2'\">with UR </if> "
            + "</script>")
    List<String> queryHistoryColumnValues(HistoryQueryImpl historyQuery);
}
