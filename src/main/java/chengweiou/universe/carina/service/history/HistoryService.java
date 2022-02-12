package chengweiou.universe.carina.service.history;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.carina.model.entity.history.History;

@Service
public class HistoryService {
    @Autowired
    private HistoryDio dio;

    public long updateReadByPersonAndRoom(History e) {
        e.setUnread(false);
        return dio.updateUnreadByRoomAndPerson(e);
    }

}
