package chengweiou.universe.carina.service.history;


import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.util.LogUtil;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.history.History;
import chengweiou.universe.carina.dao.history.HistoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class HistoryDio {
    @Autowired
    private HistoryDao dao;

    public void save(History e) throws FailException {
        e.fillNotRequire();
        e.fillNotRequire();
        e.createAt();
        e.updateAt();
        History.Dto dto = e.toDto();
        long count = dao.save(dto);
        if (count != 1) throw new FailException();
        e.setId(dto.getId());
    }

    public void delete(History e) throws FailException {
        long count = dao.delete(e.toDto());
        if (count != 1) throw new FailException();
    }

    public long delete(List<History> list) {
        SearchCondition searchCondition = Builder.set("idList", list.stream().map(e -> e.getId().toString()).collect(Collectors.toList())).to(new SearchCondition());
        long count = dao.deleteMulti(searchCondition.getForeachIdList());
        if (count != list.size()) LogUtil.i("delete multi history total:" + list.size() + " success: " + count + ". idList=" + searchCondition.getIdList());
        return count;
    }

    public long update(History e) {
        e.updateAt();
        return dao.update(e.toDto());
    }

    public long updateUnreadByRoomAndPerson(History e) {
        e.updateAt();
        return dao.updateByRoomAndPerson(e.toDto());
    }

    public History findById(History e) {
        History.Dto result = dao.findById(e.toDto());
        if (result == null) return History.NULL;
        return result.toBean();
    }

    public long count(SearchCondition searchCondition, History sample) {
        return dao.count(searchCondition, sample!=null ? sample.toDto() : null);
    }

    public List<History> find(SearchCondition searchCondition, History sample) {
        searchCondition.setDefaultSort("updateAt");
        List<History.Dto> dtoList = dao.find(searchCondition, sample!=null ? sample.toDto() : null);
        List<History> result = dtoList.stream().map(e -> e.toBean()).collect(Collectors.toList());
        return result;
    }
}
