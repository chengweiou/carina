package chengweiou.universe.carina.dao.history;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import chengweiou.universe.carina.base.dao.BaseDao;
import chengweiou.universe.carina.model.entity.history.History.Dto;

@Repository
@Mapper
public interface HistoryDao extends BaseDao<Dto> {

    @Delete("delete from history where id in ${foreachIdList}")
    long deleteMulti(String foreachIdList);

    @Update("update history set unread=#{unread} where roomId=#{roomId} and personId=#{personId}")
    long updateByRoomAndPerson(Dto e);

    class Sql {

    }
}
