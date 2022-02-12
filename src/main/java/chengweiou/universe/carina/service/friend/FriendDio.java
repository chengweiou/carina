package chengweiou.universe.carina.service.friend;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.blackhole.dao.BaseDio;
import chengweiou.universe.blackhole.dao.BaseSQL;
import chengweiou.universe.blackhole.model.AbstractSearchCondition;
import chengweiou.universe.carina.dao.friend.FriendDao;
import chengweiou.universe.carina.model.entity.friend.Friend;
import chengweiou.universe.carina.model.entity.friend.Friend.Dto;


@Component
public class FriendDio extends BaseDio<Friend, Dto> {
    @Autowired
    private FriendDao dao;
    @Override
    protected FriendDao getDao() { return dao; }
    @Override
    protected Class getTClass() { return Friend.class; };
    @Override
    protected String getDefaultSort() { return "updateAt"; };
    @Override
    protected String baseFind(AbstractSearchCondition searchCondition, Dto sample) {
        return new BaseSQL() {{
            if (searchCondition.getIdList() != null) WHERE("id in ${searchCondition.foreachIdList}");
            if (sample != null) {
                if (sample.getPersonId() != null) WHERE("personId = #{sample.personId}");
                if (sample.getTargetId() != null) WHERE("targetId = #{sample.targetId}");
            }
        }}.toString();
    }

}
