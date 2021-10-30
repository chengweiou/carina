package chengweiou.universe.carina.base.dao;

import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

import chengweiou.universe.blackhole.dao.BaseDaoImpl;
import chengweiou.universe.blackhole.model.entity.DtoEntity;
import chengweiou.universe.carina.model.SearchCondition;

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

    @SelectProvider(type = BaseDaoImpl.class, method = "count")
    long count(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Dto sample, @Param("where") String where);

    @SelectProvider(type = BaseDaoImpl.class, method = "find")
    List<Dto> find(@Param("searchCondition") SearchCondition searchCondition, @Param("sample") Dto sample, @Param("where") String where);
}
