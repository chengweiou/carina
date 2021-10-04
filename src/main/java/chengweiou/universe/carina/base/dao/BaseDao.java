package chengweiou.universe.carina.base.dao;


import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

import chengweiou.universe.carina.base.entity.DtoEntity;

@Repository
@Mapper
public interface BaseDao<Dto extends DtoEntity> {
    @InsertProvider(type = BaseDaoImpl.class, method = "save")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long save(Dto e);

    @DeleteProvider(type = BaseDaoImpl.class, method = "delete")
    long delete(Dto e);

    @UpdateProvider(type = BaseDaoImpl.class, method = "update")
    long update(Dto e);

    @SelectProvider(type = BaseDaoImpl.class, method = "findById")
    Dto findById(Dto e);

    @SelectProvider(type = BaseDaoImpl.class, method = "countByKey")
    long countByKey(Dto e);
    @SelectProvider(type = BaseDaoImpl.class, method = "findByKey")
    Dto findByKey(Dto e);

    // @SelectProvider(type = BaseDaoImpl.class, method = "count")
    // long count(SearchCondition searchCondition, Dto sample);

    // @SelectProvider(type = BaseDaoImpl.class, method = "find")
    // List<Dto> find(SearchCondition searchCondition, Dto sample);

    // SQL baseFind(SearchCondition searchCondition, Dto sample);
}
