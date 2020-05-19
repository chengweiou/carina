package chengweiou.universe.carina.service.history;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.history.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {
    @Autowired
    private HistoryDio dio;

    @Override
    public void save(History e) throws FailException {
        dio.save(e);
    }

    @Override
    public void delete(History e) throws FailException {
        dio.delete(e);
    }

    @Override
    public long delete(List<History> list) {
        return dio.delete(list);
    }

    @Override
    public long update(History e) {
        return dio.update(e);
    }

    @Override
    public long updateReadByPersonAndRoom(History e) {
        e.setUnread(false);
        return dio.updateUnreadByRoomAndPerson(e);
    }

    @Override
    public History findById(History e) {
        return dio.findById(e);
    }

    @Override
    public long count(SearchCondition searchCondition) {
        return dio.count(searchCondition, null);
    }

    @Override
    public List<History> find(SearchCondition searchCondition) {
        return dio.find(searchCondition, null);
    }

    @Override
    public long count(SearchCondition searchCondition, History sample) {
        return dio.count(searchCondition, sample);
    }

    @Override
    public List<History> find(SearchCondition searchCondition, History sample) {
        return dio.find(searchCondition, sample);
    }

}
