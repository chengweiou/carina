package chengweiou.universe.carina.dao.room;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import chengweiou.universe.carina.base.dao.BaseDao;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.room.Room.Dto;

@Repository
@Mapper
public interface RoomDao extends BaseDao<Dto> {

    @SelectProvider(type = Sql.class, method = "findId")
    List<Dto> findId(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Dto sample, @Param("where") String where);

    class Sql {
        public String findId(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Dto sample, @Param("where")final String where) {
            return new SQL().SELECT("id").FROM("room").toString().concat(where).concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }
    }


}
