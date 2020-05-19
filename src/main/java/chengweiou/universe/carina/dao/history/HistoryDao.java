package chengweiou.universe.carina.dao.history;


import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.history.History;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface HistoryDao {

    @Insert("insert into history(roomId, personId, senderId, type, v, unread, updateAt) values" +
            "(#{room.id}, #{person.id}, #{sender.id}, #{type}, #{v}, #{unread}, #{updateAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long save(History e);

    @Delete("delete from history where id=#{id}")
    long delete(History e);

    @Delete("delete from history where id in ${foreachIdList}")
    long deleteMulti(String foreachIdList);

    @UpdateProvider(type = Sql.class, method = "update")
    long update(History e);
    @Update("update history set unread=#{unread} where roomId=#{room.id} and personId=#{person.id}")
    long updateByRoomAndPerson(History e);

    @Select("select * from history where id=#{id}")
    @Results({
            @Result(property = "room.id", column = "roomId"),
            @Result(property = "person.id", column = "personId"),
            @Result(property = "sender.id", column = "senderId"),
    })
    History findById(History e);

    @SelectProvider(type = Sql.class, method = "count")
    long count(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") History sample);

    @SelectProvider(type = Sql.class, method = "find")
    @Results({
            @Result(property = "room.id", column = "roomId"),
            @Result(property = "person.id", column = "personId"),
            @Result(property = "sender.id", column = "senderId"),
    })
    List<History> find(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") History sample);

    class Sql {

        public String update(final History e) {
            return new SQL() {{
                UPDATE("history");
                if (e.getRoom() != null) SET("roomId = #{room.id}");
                if (e.getPerson() != null) SET("personId = #{person.id}");
                if (e.getSender() != null) SET("senderId = #{sender.id}");
                if (e.getType() != null) SET("type = #{type}");
                if (e.getV() != null) SET("v = #{v}");
                if (e.getUnread() != null) SET("unread = #{unread}");
                SET("updateAt = #{updateAt}");
                WHERE("id=#{id}");
            }}.toString();
        }
        public String count(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final History sample) {
            return new SQL() {{
                SELECT("count(*)"); FROM("history");
                if (searchCondition.getIdList() != null) WHERE("id in ${searchCondition.foreachIdList}");
                if (sample != null) {
                    if (sample.getPerson() != null) WHERE("personId = #{sample.person.id}");
                    if (sample.getSender() != null) WHERE("senderId = #{sample.sender.id}");
                    if (sample.getRoom() != null) WHERE("roomId = #{sample.room.id}");
                    if (sample.getUnread() != null) WHERE("unread = #{sample.unread}");
                }
            }}.toString();
        }

        public String find(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final History sample) {
            return new SQL() {{
                SELECT("*"); FROM("history");
                if (searchCondition.getIdList() != null) WHERE("id in ${searchCondition.foreachIdList}");
                if (sample != null) {
                    if (sample.getPerson() != null) WHERE("personId = #{sample.person.id}");
                    if (sample.getSender() != null) WHERE("senderId = #{sample.sender.id}");
                    if (sample.getRoom() != null) WHERE("roomId = #{sample.room.id}");
                    if (sample.getUnread() != null) WHERE("unread = #{sample.unread}");
                }
            }}.toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }
    }
}
