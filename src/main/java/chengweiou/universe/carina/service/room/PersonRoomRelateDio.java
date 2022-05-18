package chengweiou.universe.carina.service.room;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.blackhole.dao.BaseDio;
import chengweiou.universe.blackhole.dao.BaseSQL;
import chengweiou.universe.blackhole.dao.DioCache;
import chengweiou.universe.blackhole.model.AbstractSearchCondition;
import chengweiou.universe.carina.dao.room.PersonRoomRelateDao;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate.Dto;

@DioCache(false) // todo 这个可以用cache，但要先处理 两个update 之后 cache的问题
@Component
public class PersonRoomRelateDio extends BaseDio<PersonRoomRelate, Dto> {
    @Autowired
    private PersonRoomRelateDao dao;
    @Override
    protected PersonRoomRelateDao getDao() { return dao; }
    @Override
    protected String baseFind(AbstractSearchCondition searchCondition, Dto sample) {
        return new BaseSQL() {{
            if (searchCondition.getK() != null) WHERE("""
                (name LIKE #{searchCondition.like.k}
                )""");
            if (searchCondition.getIdList() != null) WHERE("id in ${searchCondition.foreachIdList}");
            if (sample != null) {
                if (sample.getPersonId() != null) WHERE("personId = #{sample.personId}");
                if (sample.getRoomId() != null) WHERE("roomId = #{sample.roomId}");
                if (sample.getStatus() != null) WHERE("status = #{sample.status}");
            }
        }}.toString();
    }

    public long update(PersonRoomRelate e) {
        e.updateAt();
        if (e.getLastMessage() != null) e.fixLastMessage();
        return dao.update(e.toDto());
    }
    public long updateByOtherPerson(PersonRoomRelate e, SearchCondition roomIdSearchCondition) {
        e.updateAt();
        return dao.updateByOtherPerson(e.toDto(), roomIdSearchCondition);
    }

    public List<PersonRoomRelate> findRoomId(SearchCondition searchCondition, PersonRoomRelate sample) {
        searchCondition.setDefaultSort("updateAt");
        PersonRoomRelate.Dto dtoSample = sample!=null ? sample.toDto() : PersonRoomRelate.NULL.toDto();
        List<PersonRoomRelate.Dto> dtoList = dao.findRoomId(searchCondition, dtoSample);
        List<PersonRoomRelate> result = dtoList.stream().map(e -> e.toBean()).toList();
        return result;
    }
}
