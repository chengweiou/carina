package chengweiou.universe.carina.base.dao;


import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import chengweiou.universe.blackhole.dao.AbstractBaseDao;
import chengweiou.universe.blackhole.dao.BaseDaoImpl;
import chengweiou.universe.blackhole.model.AbstractSearchCondition;
import chengweiou.universe.blackhole.model.entity.DtoEntity;

public interface BaseDao<Dto extends DtoEntity> extends AbstractBaseDao<Dto> {
    @InsertProvider(type = BaseDaoImpl.class, method = "save")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    long save(Dto e);

    @DeleteProvider(type = BaseDaoImpl.class, method = "delete")
    long delete(Dto e);
    @DeleteProvider(type = BaseDaoImpl.class, method = "deleteByKey")
    long deleteByKey(Dto e);
    @DeleteProvider(type = BaseDaoImpl.class, method = "deleteBySample")
    long deleteBySample(@Param("e") Dto e, @Param("sample") Dto sample);
    @DeleteProvider(type = BaseDaoImpl.class, method = "deleteByIdList")
    long deleteByIdList(@Param("e") Dto e, @Param("idList") List idList);

    @UpdateProvider(type = BaseDaoImpl.class, method = "update")
    long update(Dto e);
    @UpdateProvider(type = BaseDaoImpl.class, method = "updateByKey")
    long updateByKey(Dto e);
    @UpdateProvider(type = BaseDaoImpl.class, method = "updateBySample")
    long updateBySample(@Param("e") Dto e, @Param("sample") Dto sample);
    @UpdateProvider(type = BaseDaoImpl.class, method = "updateByIdList")
    long updateByIdList(@Param("e") Dto e, @Param("idList") List idList);

    @SelectProvider(type = BaseDaoImpl.class, method = "findById")
    Dto findById(Dto e);
    @SelectProvider(type = BaseDaoImpl.class, method = "countByKey")
    long countByKey(Dto e);
    @SelectProvider(type = BaseDaoImpl.class, method = "findByKey")
    Dto findByKey(Dto e);

    @SelectProvider(type = BaseDaoImpl.class, method = "count")
    long count(@Param("searchCondition") AbstractSearchCondition searchCondition, @Param("sample") Dto sample, @Param("where") String where);

    @SelectProvider(type = BaseDaoImpl.class, method = "find")
    List<Dto> find(@Param("searchCondition") AbstractSearchCondition searchCondition, @Param("sample") Dto sample, @Param("where") String where);

    @SelectProvider(type = BaseDaoImpl.class, method = "findId")
    List<Dto> findId(@Param("searchCondition") AbstractSearchCondition searchCondition, @Param("sample") Dto sample, @Param("where") String where);
}
