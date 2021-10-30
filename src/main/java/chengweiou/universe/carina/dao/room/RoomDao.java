package chengweiou.universe.carina.dao.room;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import chengweiou.universe.carina.base.dao.BaseDao;
import chengweiou.universe.carina.model.entity.room.Room.Dto;

@Repository
@Mapper
public interface RoomDao extends BaseDao<Dto> {

    class Sql {

    }
}
