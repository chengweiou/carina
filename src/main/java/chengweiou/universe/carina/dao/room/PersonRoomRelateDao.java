package chengweiou.universe.carina.dao.room;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Repository;

import chengweiou.universe.carina.base.dao.BaseDao;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate.Dto;

@Repository
@Mapper
public interface PersonRoomRelateDao extends BaseDao<Dto> {
    @UpdateProvider(type = Sql.class, method = "updateByOtherPerson")
    long updateByOtherPerson(@Param("e")Dto e, @Param("roomIdSearchCondition")SearchCondition roomIdSearchCondition);

    @SelectProvider(type = Sql.class, method = "findRoomId")
    List<Dto> findRoomId(@Param("searchCondition")SearchCondition searchCondition, @Param("sample")Dto sample);
    class Sql {
        public String updateByOtherPerson(@Param("e")final Dto e, @Param("roomIdSearchCondition")SearchCondition roomIdSearchCondition) {
            return new SQL() {{
                UPDATE("personRoomRelate");
                if (e.getName() != null) SET("name = #{e.name}");
                if (e.getImgsrc() != null) SET("imgsrc = #{e.imgsrc}");
                SET("updateAt = #{e.updateAt}");
                WHERE("personId!=#{e.personId}");
                WHERE("roomId in ${roomIdSearchCondition.foreachIdList}");
            }}.toString();
        }


        public String findRoomId(@Param("searchCondition")final SearchCondition searchCondition, @Param("sample")final Dto sample) {
            return roomIdFind(searchCondition, sample).SELECT("roomId").toString().concat(searchCondition.getOrderBy()).concat(searchCondition.getSqlLimit());
        }
        private SQL roomIdFind(SearchCondition searchCondition, Dto sample) {
            return new SQL() {{
                FROM("personRoomRelate");
                if (sample != null) {
                    if (sample.getPersonId() != null) WHERE("personId = #{sample.personId}");
                    if (sample.getStatus() != null) WHERE("status = #{sample.status}");
                }
            }};
        }

    }
}
