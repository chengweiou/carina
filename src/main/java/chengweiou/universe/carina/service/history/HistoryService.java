package chengweiou.universe.carina.service.history;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.history.History;

import java.util.List;

public interface HistoryService {
    void save(History e) throws FailException;

    void delete(History e) throws FailException;
    long delete(List<History> list);

    long update(History e);

    History findById(History e);

    long count(SearchCondition searchCondition);
    List<History> find(SearchCondition searchCondition);

    long count(SearchCondition searchCondition, History sample);
    List<History> find(SearchCondition searchCondition, History sample);

}
