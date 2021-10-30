package chengweiou.universe.carina.dao.room;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import chengweiou.universe.carina.base.dao.BaseDao;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate.Dto;

@Repository
@Mapper
public interface PersonRoomRelateDao extends BaseDao<Dto> {
    class Sql {

    }
}
