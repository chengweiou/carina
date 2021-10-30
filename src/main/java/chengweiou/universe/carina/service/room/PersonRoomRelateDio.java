package chengweiou.universe.carina.service.room;


import chengweiou.universe.blackhole.dao.BaseSQL;
import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import chengweiou.universe.carina.dao.room.PersonRoomRelateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class PersonRoomRelateDio {
    @Autowired
    private PersonRoomRelateDao dao;

    public void save(PersonRoomRelate e) throws FailException, ProjException {
        long count = dao.countByKey(e.toDto());
        if (count != 0) throw new ProjException("dup key: person:" + e.getPerson().getId() + ", room:" + e.getRoom().getId() + " exists", BasicRestCode.EXISTS);
        e.fillNotRequire();
        e.createAt();
        e.updateAt();
        PersonRoomRelate.Dto dto = e.toDto();
        count = dao.save(dto);
        if (count != 1) throw new FailException();
        e.setId(dto.getId());
    }

    public void delete(PersonRoomRelate e) throws FailException {
        long count = dao.delete(e.toDto());
        if (count != 1) throw new FailException();
    }

    public long update(PersonRoomRelate e) {
        e.updateAt();
        if (e.getLastMessage() != null) e.fixLastMessage();
        return dao.update(e.toDto());
    }

    public PersonRoomRelate findById(PersonRoomRelate e) {
        PersonRoomRelate.Dto result = dao.findById(e.toDto());
        if (result == null) return PersonRoomRelate.NULL;
        return result.toBean();
    }
    public long countByKey(PersonRoomRelate e) {
        return dao.countByKey(e.toDto());
    }
    public PersonRoomRelate findByKey(PersonRoomRelate e) {
        PersonRoomRelate.Dto result = dao.findByKey(e.toDto());
        if (result == null) return PersonRoomRelate.NULL;
        return result.toBean();
    }
    public long count(SearchCondition searchCondition, PersonRoomRelate sample) {
        PersonRoomRelate.Dto dtoSample = sample!=null ? sample.toDto() : PersonRoomRelate.NULL.toDto();
        String where = baseFind(searchCondition, dtoSample);
        return dao.count(searchCondition, dtoSample, where);
    }

    public List<PersonRoomRelate> find(SearchCondition searchCondition, PersonRoomRelate sample) {
        searchCondition.setDefaultSort("updateAt");
        PersonRoomRelate.Dto dtoSample = sample!=null ? sample.toDto() : PersonRoomRelate.NULL.toDto();
        String where = baseFind(searchCondition, dtoSample);
        List<PersonRoomRelate.Dto> dtoList = dao.find(searchCondition, dtoSample, where);
        List<PersonRoomRelate> result = dtoList.stream().map(e -> e.toBean()).collect(Collectors.toList());
        return result;
    }

    private String baseFind(SearchCondition searchCondition, PersonRoomRelate.Dto sample) {
        return new BaseSQL() {{
            if (searchCondition.getK() != null) WHERE("""
                (name LIKE #{searchCondition.like.k}
                )""");
            if (searchCondition.getIdList() != null) WHERE("id in ${searchCondition.foreachIdList}");
            if (sample != null) {
                if (sample.getPersonId() != null) WHERE("personId = #{sample.personId}");
                if (sample.getRoomId() != null) WHERE("roomId = #{sample.roomId}");
            }
        }}.toString();
    }

}
