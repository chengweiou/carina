package chengweiou.universe.carina.dao.room;


import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate;

@Repository
@Mapper
public interface PersonRoomRelateDao {
    @Insert("insert into personRoomRelate(personId, roomId, name, imgsrc, unread, lastMessage, createAt, updateAt) values" +
            "(#{person.id}, #{room.id}, #{name}, #{imgsrc}, #{unread}, #{lastMessage}, #{createAt}, #{updateAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long save(PersonRoomRelate e);

    @Delete("delete from personRoomRelate where id=#{id}")
    long delete(PersonRoomRelate e);

    @UpdateProvider(type = Sql.class, method = "update")
    long update(PersonRoomRelate e);

    @Select("select * from personRoomRelate where id=#{id}")
    @Results({
            @Result(property = "person.id", column = "personId"),
            @Result(property = "room.id", column = "roomId"),
    })
    PersonRoomRelate findById(PersonRoomRelate e);
    @Select("select * from personRoomRelate where personId=#{person.id} and roomId=#{room.id}")
    @Results({
            @Result(property = "person.id", column = "personId"),
            @Result(property = "room.id", column = "roomId"),
    })
    PersonRoomRelate findByKey(PersonRoomRelate e);

    @SelectProvider(type = Sql.class, method = "count")
    long count(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") PersonRoomRelate sample);

    @SelectProvider(type = Sql.class, method = "find")
    @Results({
            @Result(property = "person.id", column = "personId"),
            @Result(property = "room.id", column = "roomId"),
    })
    List<PersonRoomRelate> find(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") PersonRoomRelate sample);

    class Sql {

        public String update(final PersonRoomRelate e) {
            return new SQL() {{
                UPDATE("personRoomRelate");
                if (e.getPerson() != null) SET("personId = #{person.id}");
                if (e.getRoom() != null) SET("roomId = #{room.id}");
                if (e.getName() != null) SET("name = #{name}");
                if (e.getImgsrc() != null) SET("imgsrc = #{imgsrc}");
                if (e.getUnread() != null) SET("unread = #{unread}");
                if (e.getLastMessage() != null) SET("lastMessage = #{lastMessage}");
                SET("updateAt = #{updateAt}");
                WHERE("id=#{id}");
            }}.toString();
        }

        public String count(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final PersonRoomRelate sample) {
            return baseFind(searchCondition, sample).SELECT("count(*)").toString();
        }

        public String find(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final PersonRoomRelate sample) {
            return baseFind(searchCondition, sample).SELECT("*").toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }

        private SQL baseFind(SearchCondition searchCondition, PersonRoomRelate sample) {
            return new SQL() {{
                FROM("personRoomRelate");
                if (searchCondition.getK() != null) WHERE("(" +
                        "name LIKE #{searchCondition.like.k} " +
                        ")");
                if (searchCondition.getIdList() != null) WHERE("id in ${searchCondition.foreachIdList}");
                if (sample != null) {
                    if (sample.getPerson() != null) WHERE("personId = #{sample.person.id}");
                    if (sample.getRoom() != null) WHERE("roomId = #{sample.room.id}");
                }
            }};
        }
    }
}
