package com.chengweiou.universe.carina.dao.person;


import com.chengweiou.universe.carina.model.SearchCondition;
import com.chengweiou.universe.carina.model.entity.person.Person;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PersonDao {

    @Insert("insert into person(id, name, imgsrc, unread, createAt, updateAt) values" +
            "(#{id}, #{name}, #{imgsrc}, #{unread}, #{createAt}, #{updateAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long save(Person e);

    @Delete("delete from person where id=#{id}")
    long delete(Person e);

    @UpdateProvider(type = Sql.class, method = "update")
    long update(Person e);

    @Select("select * from person where id=#{id}")
    Person findById(Person e);

    @SelectProvider(type = Sql.class, method = "count")
    long count(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Person sample);

    @SelectProvider(type = Sql.class, method = "find")
    List<Person> find(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Person sample);

    class Sql {

        public String update(final Person e) {
            return new SQL() {{
                UPDATE("person");
                if (e.getName() != null) SET("name = #{name}");
                if (e.getImgsrc() != null) SET("imgsrc = #{imgsrc}");
                if (e.getUnread() != null) SET("unread = #{unread}");
                SET("updateAt = #{updateAt}");
                WHERE("id=#{id}");
            }}.toString();
        }
        public String count(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Person sample) {
            return new SQL() {{
                SELECT("count(*)"); FROM("person");
                if (searchCondition.getK() != null) WHERE("(" +
                        "name LIKE #{searchCondition.like.k} " +
                        ")");
                if (searchCondition.getIdList() != null) WHERE("id in ${searchCondition.foreachIdList}");
            }}.toString();
        }

        public String find(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Person sample) {
            return new SQL() {{
                SELECT("*"); FROM("person");
                if (searchCondition.getK() != null) WHERE("(" +
                        "name LIKE #{searchCondition.like.k} " +
                        ")");
                if (searchCondition.getIdList() != null) WHERE("id in ${searchCondition.foreachIdList}");
            }}.toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }
    }
}
