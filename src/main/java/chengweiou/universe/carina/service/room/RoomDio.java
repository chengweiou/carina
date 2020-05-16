package chengweiou.universe.carina.service.room;


import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.room.Room;
import chengweiou.universe.carina.dao.room.RoomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Component
public class RoomDio {
    @Autowired
    private RoomDao dao;

    public void save(Room e) throws FailException {
        e.fillNotRequire();
        e.createAt();
        e.updateAt();
        setString(e);
        long count = dao.save(e);
        if (count != 1) throw new FailException();
    }

    public void delete(Room e) throws FailException {
        long count = dao.delete(e);
        if (count != 1) throw new FailException();
    }

    public long update(Room e) {
        if (e.getPersonIdList() != null) setString(e);
        e.updateAt();
        return dao.update(e);
    }

    public Room findById(Room e) {
        Room result = dao.findById(e);
        if (result == null) return Room.NULL;
        setList(Arrays.asList(result));
        return result;
    }

    public long count(SearchCondition searchCondition, Room sample) {
        if (sample != null) {
            if (sample.getPersonIdList() != null) setString(sample);
        }
        return dao.count(searchCondition, sample);
    }

    public List<Room> find(SearchCondition searchCondition, Room sample) {
        if (sample != null) {
            if (sample.getPersonIdList() != null) setString(sample);
        }
        searchCondition.setDefaultSort("updateAt");
        List<Room> result = dao.find(searchCondition, sample);
        setList(result);
        return result;
    }

    private void setList(List<Room> list) {
        list.parallelStream().filter(e -> !e.getPersonIdListString().isEmpty()).forEach(e -> {
            e.setPersonIdList(Pattern.compile(",").splitAsStream(e.getPersonIdListString()).map(Long::valueOf).collect(Collectors.toList()));
        });

    }
    private void setString(Room room) {
        room.setPersonIdListString(room.getPersonIdList().stream().distinct().sorted().map(String::valueOf).collect(Collectors.joining(",")));
    }
}
