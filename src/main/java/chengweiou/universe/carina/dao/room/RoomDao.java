package chengweiou.universe.carina.dao.room;


import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.room.Room;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface RoomDao {

    @Insert("insert into room(type, personIdListString, createAt, updateAt) values" +
            "(#{type}, #{personIdListString}, #{createAt}, #{updateAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long save(Room e);

    @Delete("delete from room where id=#{id}")
    long delete(Room e);

    @UpdateProvider(type = Sql.class, method = "update")
    long update(Room e);

    @Select("select * from room where id=#{id}")
    Room findById(Room e);
    @Select("select * from room where type=#{type} and personIdListString=#{personIdListString}")
    Room findByKey(Room e);

    @SelectProvider(type = Sql.class, method = "count")
    long count(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Room sample);

    @SelectProvider(type = Sql.class, method = "find")
    List<Room> find(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Room sample);

    class Sql {
        public String update(final Room e) {
            return new SQL() {{
                UPDATE("room");
                if (e.getType() != null) SET("type = #{type}");
                if (e.getPersonIdListString() != null) SET("personIdListString = #{personIdListString}");
                SET("updateAt = #{updateAt}");
                WHERE("id=#{id}");
            }}.toString();
        }
        public String count(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Room sample) {
            return new SQL() {{
                SELECT("count(*)"); FROM("room");
                if (searchCondition.getIdList() != null) WHERE("id in ${searchCondition.foreachIdList}");
                if (sample != null) {
                    if (sample.getType() != null) WHERE("type = (${sample.type})");
                    if (sample.getPersonIdListString() != null) WHERE("personIdListString in (${sample.personIdListString})");
                }
            }}.toString();
        }

        public String find(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Room sample) {
            return new SQL() {{
                SELECT("*"); FROM("room");
                if (searchCondition.getIdList() != null) WHERE("id in ${searchCondition.foreachIdList}");
                if (sample != null) {
                    if (sample.getType() != null) WHERE("type = (${sample.type})");
                    if (sample.getPersonIdListString() != null) WHERE("personIdListString in (${sample.personIdListString})");
                }

            }}.toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }
    }
}
