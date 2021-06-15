package chengweiou.universe.carina.dao.person;


import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.person.Person;

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
            return baseFind(searchCondition, sample).SELECT("count(*)").toString();
        }

        public String find(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Person sample) {
            return baseFind(searchCondition, sample).SELECT("*").toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }

        private SQL baseFind(SearchCondition searchCondition, Person sample) {
            return new SQL() {{
                FROM("person");
                if (searchCondition.getK() != null) WHERE("(" +
                        "name LIKE #{searchCondition.like.k} " +
                        ")");
                if (searchCondition.getIdList() != null) WHERE("id in ${searchCondition.foreachIdList}");
            }};
        }
    }
}
