package chengweiou.universe.carina.dao.person;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import chengweiou.universe.carina.base.dao.BaseDao;
import chengweiou.universe.carina.model.entity.person.Person.Dto;

@Repository
@Mapper
public interface PersonDao extends BaseDao<Dto> {

    class Sql {

    }
}
