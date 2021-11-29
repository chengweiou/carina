package chengweiou.universe.carina.service.room;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.blackhole.dao.BaseSQL;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.carina.dao.room.RoomDao;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import chengweiou.universe.carina.model.entity.room.Room;


@Component
public class RoomDio {
    @Autowired
    private RoomDao dao;

    public void save(Room e) throws FailException, ProjException {
        long count = dao.countByKey(e.toDto());
        if (count != 0) throw new ProjException("dup key: type:" + e.getType() + ", personIdList:" + e.getPersonIdList() + " exists", BasicRestCode.EXISTS);
        e.fillNotRequire();
        e.createAt();
        e.updateAt();
        Room.Dto dto = e.toDto();
        count = dao.save(dto);
        if (count != 1) throw new FailException();
        e.setId(dto.getId());
    }

    public void delete(Room e) throws FailException {
        long count = dao.delete(e.toDto());
        if (count != 1) throw new FailException();
    }

    public long update(Room e) {
        e.updateAt();
        return dao.update(e.toDto());
    }

    public Room findById(Room e) {
        Room.Dto result = dao.findById(e.toDto());
        if (result == null) return Room.NULL;
        return result.toBean();
    }
    public long countByKey(Room e) {
        return dao.countByKey(e.toDto());
    }
    public Room findByKey(Room e) {
        Room.Dto result = dao.findByKey(e.toDto());
        if (result == null) return Room.NULL;
        return result.toBean();
    }
    public long count(SearchCondition searchCondition, Room sample) {
        Room.Dto dtoSample = sample!=null ? sample.toDto() : Room.NULL.toDto();
        String where = baseFind(searchCondition, dtoSample);
        return dao.count(searchCondition, dtoSample, where);
    }

    public List<Room> find(SearchCondition searchCondition, Room sample) {
        searchCondition.setDefaultSort("updateAt");
        Room.Dto dtoSample = sample!=null ? sample.toDto() : Room.NULL.toDto();
        String where = baseFind(searchCondition, dtoSample);
        List<Room.Dto> dtoList = dao.find(searchCondition, dtoSample, where);
        List<Room> result = dtoList.stream().map(e -> e.toBean()).collect(Collectors.toList());
        return result;
    }

    public List<Room> findId(SearchCondition searchCondition, Room sample) {
        searchCondition.setDefaultSort("updateAt");
        Room.Dto dtoSample = sample!=null ? sample.toDto() : Room.NULL.toDto();
        String where = baseFind(searchCondition, dtoSample);
        List<Room.Dto> dtoList = dao.findId(searchCondition, dtoSample, where);
        List<Room> result = dtoList.stream().map(e -> e.toBean()).collect(Collectors.toList());
        return result;
    }

    private String baseFind(SearchCondition searchCondition, Room.Dto sample) {
        return new BaseSQL() {{
            if (searchCondition.getIdList() != null) WHERE("id in ${searchCondition.foreachIdList}");
            if (sample != null) {
                if (sample.getType() != null) WHERE("type = #{sample.type}");
                if (sample.getPersonIdListString() != null) WHERE("personIdListString in (${sample.personIdListString})");
            }
        }}.toString();
    }


}
