package chengweiou.universe.carina.service.friend;


import chengweiou.universe.blackhole.dao.BaseSQL;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.carina.dao.friend.FriendDao;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.friend.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class FriendDio {
    @Autowired
    private FriendDao dao;

    public void save(Friend e) throws FailException, ProjException {
        long count = dao.countByKey(e.toDto());
        if (count != 0) throw new ProjException("dup key: " + e.getPerson().getId() + ", " + e.getTarget().getId() + " exists", BasicRestCode.EXISTS);
        e.fillNotRequire();
        e.createAt();
        e.updateAt();
        Friend.Dto dto = e.toDto();
        count = dao.save(dto);
        if (count != 1) throw new FailException();
        e.setId(dto.getId());
    }

    public void delete(Friend e) throws FailException {
        long count = dao.delete(e.toDto());
        if (count != 1) throw new FailException();
    }

    public long update(Friend e) {
        e.updateAt();
        return dao.update(e.toDto());
    }

    public Friend findById(Friend e) {
        Friend.Dto result = dao.findById(e.toDto());
        if (result == null) return Friend.NULL;
        return result.toBean();
    }

    public long countByKey(Friend e) {
        return dao.countByKey(e.toDto());
    }
    public Friend findByKey(Friend e) {
        Friend.Dto result = dao.findByKey(e.toDto());
        if (result == null) return Friend.NULL;
        return result.toBean();
    }

    public long count(SearchCondition searchCondition, Friend sample) {
        Friend.Dto dtoSample = sample!=null ? sample.toDto() : Friend.NULL.toDto();
        String where = baseFind(searchCondition, dtoSample);
        return dao.count(searchCondition, dtoSample, where);
    }

    public List<Friend> find(SearchCondition searchCondition, Friend sample) {
        searchCondition.setDefaultSort("updateAt");
        Friend.Dto dtoSample = sample!=null ? sample.toDto() : Friend.NULL.toDto();
        String where = baseFind(searchCondition, dtoSample);
        List<Friend.Dto> dtoList = dao.find(searchCondition, dtoSample, where);
        List<Friend> result = dtoList.stream().map(e -> e.toBean()).collect(Collectors.toList());
        return result;
    }

    private String baseFind(SearchCondition searchCondition, Friend.Dto sample) {
        return new BaseSQL() {{
            if (searchCondition.getIdList() != null) WHERE("id in ${searchCondition.foreachIdList}");
            if (sample != null) {
                if (sample.getPersonId() != null) WHERE("personId = #{sample.personId}");
                if (sample.getTargetId() != null) WHERE("targetId = #{sample.targetId}");
            }
        }}.toString();
    }

}
