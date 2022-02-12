package chengweiou.universe.carina.service.person;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.blackhole.dao.BaseDio;
import chengweiou.universe.blackhole.dao.BaseSQL;
import chengweiou.universe.blackhole.model.AbstractSearchCondition;
import chengweiou.universe.carina.dao.person.PersonDao;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.model.entity.person.Person.Dto;


@Component
public class PersonDio extends BaseDio<Person, Dto> {
    @Autowired
    private PersonDao dao;
    @Override
    protected PersonDao getDao() { return dao; }
    @Override
    protected Class getTClass() { return Person.class; };
    @Override
    protected String getDefaultSort() { return "updateAt"; };
    @Override
    protected String baseFind(AbstractSearchCondition searchCondition, Dto sample) {
        return new BaseSQL() {{
            if (searchCondition.getK() != null) WHERE("""
            (name LIKE #{searchCondition.like.k}
            )""");
            if (searchCondition.getIdList() != null) WHERE("id in ${searchCondition.foreachIdList}");
            if (sample != null) {
            }
        }}.toString();
    }

}
