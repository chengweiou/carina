package chengweiou.universe.carina.service.room;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.blackhole.dao.BaseDio;
import chengweiou.universe.blackhole.dao.BaseSQL;
import chengweiou.universe.blackhole.model.AbstractSearchCondition;
import chengweiou.universe.carina.dao.room.RoomDao;
import chengweiou.universe.carina.model.entity.room.Room;
import chengweiou.universe.carina.model.entity.room.Room.Dto;


@Component
public class RoomDio extends BaseDio<Room, Dto> {
    @Autowired
    private RoomDao dao;
    @Override
    protected RoomDao getDao() { return dao; }
    @Override
    protected Class getTClass() { return Room.class; };
    @Override
    protected String getDefaultSort() { return "updateAt"; };
    @Override
    protected String baseFind(AbstractSearchCondition searchCondition, Dto sample) {
        return new BaseSQL() {{
            if (searchCondition.getIdList() != null) WHERE("id in ${searchCondition.foreachIdList}");
            if (sample != null) {
                if (sample.getType() != null) WHERE("type = #{sample.type}");
                if (sample.getPersonIdListString() != null) WHERE("personIdListString in (${sample.personIdListString})");
            }
        }}.toString();
    }
}
