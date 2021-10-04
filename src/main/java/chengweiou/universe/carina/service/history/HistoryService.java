package chengweiou.universe.carina.service.history;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.history.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {
    @Autowired
    private HistoryDio dio;

    public void save(History e) throws FailException {
        dio.save(e);
    }

    public void delete(History e) throws FailException {
        dio.delete(e);
    }

    public long delete(List<History> list) {
        return dio.delete(list);
    }

    public long update(History e) {
        return dio.update(e);
    }

    public long updateReadByPersonAndRoom(History e) {
        e.setUnread(false);
        return dio.updateUnreadByRoomAndPerson(e);
    }

    public History findById(History e) {
        return dio.findById(e);
    }

    public long count(SearchCondition searchCondition) {
        return dio.count(searchCondition, null);
    }

    public List<History> find(SearchCondition searchCondition) {
        return dio.find(searchCondition, null);
    }

    public long count(SearchCondition searchCondition, History sample) {
        return dio.count(searchCondition, sample);
    }

    public List<History> find(SearchCondition searchCondition, History sample) {
        return dio.find(searchCondition, sample);
    }

}
