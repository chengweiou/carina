package chengweiou.universe.carina.dao.history;


import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import chengweiou.universe.carina.base.dao.BaseDao;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.history.History;
import chengweiou.universe.carina.model.entity.history.History.Dto;

@Repository
@Mapper
public interface HistoryDao extends BaseDao<Dto> {

    @Delete("delete from history where id in ${foreachIdList}")
    long deleteMulti(String foreachIdList);

    @Update("update history set unread=#{unread} where roomId=#{roomId} and personId=#{personId}")
    long updateByRoomAndPerson(Dto e);

    @SelectProvider(type = Sql.class, method = "count")
    long count(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Dto sample);

    @SelectProvider(type = Sql.class, method = "find")
    List<Dto> find(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Dto sample);

    class Sql {

        public String count(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Dto sample) {
            return baseFind(searchCondition, sample).SELECT("count(*)").toString();
        }

        public String find(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Dto sample) {
            return baseFind(searchCondition, sample).SELECT("*").toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }

        private SQL baseFind(SearchCondition searchCondition, Dto sample) {
            return new SQL() {{
                FROM("history");
                if (searchCondition.getIdList() != null) WHERE("id in ${searchCondition.foreachIdList}");
                if (searchCondition.getMaxId() != null) WHERE("id < #{searchCondition.maxId}");
                if (sample != null) {
                    if (sample.getPersonId() != null) WHERE("personId = #{sample.personId}");
                    if (sample.getSenderId() != null) WHERE("senderId = #{sample.senderId}");
                    if (sample.getRoomId() != null) WHERE("roomId = #{sample.roomId}");
                    if (sample.getUnread() != null) WHERE("unread = #{sample.unread}");
                }
            }};
        }

    }
}
