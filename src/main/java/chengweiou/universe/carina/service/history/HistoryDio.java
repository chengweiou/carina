package chengweiou.universe.carina.service.history;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.blackhole.dao.BaseDio;
import chengweiou.universe.blackhole.dao.BaseSQL;
import chengweiou.universe.blackhole.model.AbstractSearchCondition;
import chengweiou.universe.carina.dao.history.HistoryDao;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.history.History;
import chengweiou.universe.carina.model.entity.history.History.Dto;

@Component
public class HistoryDio extends BaseDio<History, Dto> {
    @Autowired
    private HistoryDao dao;
    @Override
    protected HistoryDao getDao() { return dao; }
    @Override
    protected Class getTClass() { return History.class; };
    @Override
    protected String getDefaultSort() { return "updateAt"; };
    @Override
    protected String baseFind(AbstractSearchCondition searchCondition, Dto sample) {
        return new BaseSQL() {{
            if (searchCondition.getIdList() != null) WHERE("id in ${searchCondition.foreachIdList}");
            if (((SearchCondition) searchCondition).getMaxId() != null) WHERE("id < #{searchCondition.maxId}");
            if (sample != null) {
                if (sample.getPersonId() != null) WHERE("personId = #{sample.personId}");
                if (sample.getSenderId() != null) WHERE("senderId = #{sample.senderId}");
                if (sample.getRoomId() != null) WHERE("roomId = #{sample.roomId}");
                if (sample.getUnread() != null) WHERE("unread = #{sample.unread}");
            }
        }}.toString();
    }

    public long updateUnreadByRoomAndPerson(History e) {
        e.updateAt();
        return dao.updateByRoomAndPerson(e.toDto());
    }
}
