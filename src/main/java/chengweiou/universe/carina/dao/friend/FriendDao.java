package chengweiou.universe.carina.dao.friend;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import chengweiou.universe.carina.base.dao.BaseDao;
import chengweiou.universe.carina.model.entity.friend.Friend.Dto;

@Repository
@Mapper
public interface FriendDao extends BaseDao<Dto> {

    class Sql {

    }
}
