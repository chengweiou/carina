package chengweiou.universe.carina.dao.friend;


import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.friend.Friend;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FriendDao {

    @Insert("insert into friend(personId, targetId, updateAt) values" +
            "(#{person.id}, #{target.id}, #{updateAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long save(Friend e);

    @Delete("delete from friend where id=#{id}")
    long delete(Friend e);

    @UpdateProvider(type = Sql.class, method = "update")
    long update(Friend e);

    @Select("select * from friend where id=#{id}")
    @Results({
            @Result(property = "person.id", column = "personId"),
            @Result(property = "target.id", column = "targetId"),
    })
    Friend findById(Friend e);

    @SelectProvider(type = Sql.class, method = "count")
    long count(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Friend sample);

    @SelectProvider(type = Sql.class, method = "find")
    @Results({
            @Result(property = "person.id", column = "personId"),
            @Result(property = "target.id", column = "targetId"),
    })
    List<Friend> find(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Friend sample);

    class Sql {

        public String update(final Friend e) {
            return new SQL() {{
                UPDATE("friend");
                if (e.getPerson() != null) SET("personId = #{person.id}");
                if (e.getTarget() != null) SET("targetId = #{target.id}");
                SET("updateAt = #{updateAt}");
                WHERE("id=#{id}");
            }}.toString();
        }
        public String count(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Friend sample) {
            return new SQL() {{
                SELECT("count(*)"); FROM("friend");
                if (searchCondition.getIdList() != null) WHERE("id in ${searchCondition.foreachIdList}");
                if (sample != null) {
                    if (sample.getPerson() != null) WHERE("personId = #{sample.person.id}");
                    if (sample.getTarget() != null) WHERE("targetId = #{sample.target.id}");
                }
            }}.toString();
        }

        public String find(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Friend sample) {
            return new SQL() {{
                SELECT("*"); FROM("friend");
                if (searchCondition.getIdList() != null) WHERE("id in ${searchCondition.foreachIdList}");
                if (sample != null) {
                    if (sample.getPerson() != null) WHERE("personId = #{sample.person.id}");
                    if (sample.getTarget() != null) WHERE("targetId = #{sample.target.id}");
                }
            }}.toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }
    }
}
