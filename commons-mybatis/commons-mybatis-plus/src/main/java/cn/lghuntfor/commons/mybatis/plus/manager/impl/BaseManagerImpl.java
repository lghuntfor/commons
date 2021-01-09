package cn.lghuntfor.commons.mybatis.plus.manager.impl;

import cn.hutool.core.collection.CollUtil;
import cn.lghuntfor.commons.mybatis.plus.common.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import cn.lghuntfor.commons.common.constants.ReturnMsg;
import cn.lghuntfor.commons.common.exception.CommonException;
import cn.lghuntfor.commons.common.page.PageInfo;
import cn.lghuntfor.commons.mybatis.plus.manager.BaseManager;
import cn.lghuntfor.commons.mybatis.plus.mapper.CommonMapper;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 基于mybatis-plus
 * 基础Service的抽象类, 实现了基础接口
 * @author liaogang
 * @date 2020/9/2 14:11
 */
public abstract class BaseManagerImpl<T, M extends CommonMapper<T>> implements BaseManager<T> {

    /** mybatis 日志对象, saveBatch方法调用需要使用到 */
    private Log logger = LogFactory.getLog(getClass());

    @Autowired
    protected M mapper;

    private Class<T> clazz;
    private Class<T> mapperClass;

    public BaseManagerImpl() {
        Type type = this.getClass().getGenericSuperclass();
        ParameterizedType ptype = (ParameterizedType) type;
        this.clazz = (Class<T>) ptype.getActualTypeArguments()[0];
        this.mapperClass = (Class<T>) ptype.getActualTypeArguments()[1];
    }

    @Override
    public T getById(Serializable id) {
        return mapper.selectById(id);
    }

    @Override
    public List<T> listAll() {
        return mapper.selectList(new QueryWrapper<>());
    }

    @Override
    public List<T> listByWhere(T entity) {
        return mapper.selectList(new QueryWrapper<>(entity));
    }

    @Override
    public int insertBatch(Collection<T> entityList) {
        return insertBatch(entityList, 200);
    }

    @Override
    public int insertBatch(Collection<T> entityList, int batchSize) {
        if (entityList.size() <= batchSize) {
            return mapper.insertBatchSomeColumn(entityList);
        }
        List<List<T>> batchList = CollUtil.split(entityList, batchSize);
        int i = 0;
        for (List<T> list : batchList) {
            i += mapper.insertBatchSomeColumn(list);
        }
        return i;
    }

    @Override
    public List<T> listByQuery(QueryWrapper<T> queryWrapper) {
        return mapper.selectList(queryWrapper);
    }

    @Override
    public List<T> listByIds(Collection ids) {
        return mapper.selectBatchIds(ids);
    }

    @Override
    public Integer countByWhere(T entity) {
        return mapper.selectCount(new QueryWrapper<>(entity));
    }

    @Override
    public Integer countByQuery(QueryWrapper<T> queryWrapper) {
        return mapper.selectCount(queryWrapper);
    }

    @Override
    public T getOne(T entity) {
        return mapper.selectOne(new QueryWrapper<>(entity));
    }

    @Override
    public T getOne(QueryWrapper<T> queryWrapper) {
        return mapper.selectOne(queryWrapper);
    }

    @Override
    public int save(T entity) {
        /** 直接使用了 saveSelective 相同的方法 */
        return mapper.insert(entity);
    }

    @Override
    public int saveSelective(T entity) {
        return mapper.insert(entity);
    }

    @Override
    public void saveBatch(List<T> entityList) {
        saveBatch(entityList, 200);
    }

    @Override
    public void saveBatch(List<T> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(mapperClass, SqlMethod.INSERT_ONE);
        SqlHelper.executeBatch(clazz, logger, entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
    }

    @Override
    public int updateById(T entity) {
        /** 直接使用了 updateById 相同的方法 */
        return mapper.updateById(entity);
    }

    @Override
    public int updateByQuery(T entity, QueryWrapper<T> queryWrapper) {
        return mapper.update(entity, queryWrapper);
    }

    @Override
    public int updateSelectiveById(T entity) {
        return mapper.updateById(entity);
    }

    @Override
    public void updateBatch(List<T> entityList) {
        updateBatch(entityList, 200);
    }

    @Override
    public void updateBatch(List<T> entityList, int batchSize) {
        String sqlStatement = SqlHelper.getSqlStatement(mapperClass, SqlMethod.UPDATE_BY_ID);
        SqlHelper.executeBatch(clazz, logger, entityList, batchSize, (sqlSession, entity) -> {
            MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
            param.put(Constants.ENTITY, entity);
            sqlSession.update(sqlStatement, param);
        });
    }

    @Override
    public int removeById(Serializable id) {
        return mapper.deleteBatchIds(Collections.singleton(id));
    }

    @Override
    public int removeByIds(Collection ids) {
        return mapper.deleteBatchIds(ids);
    }

    @Override
    public int removeByQuery(QueryWrapper queryWrapper) {
        return mapper.delete(queryWrapper);
    }

    @Override
    public PageInfo<T> page(PageInfo<T> pageQuery, T entity) {
        IPage<T> page = PageUtil.convert2IPage(pageQuery, clazz);
        IPage<T> pageResult = mapper.selectPage(page, new QueryWrapper<>(entity));
        return PageUtil.convert(pageResult);
    }

    @Override
    public PageInfo<T> page(PageInfo<T> pageQuery, QueryWrapper<T> queryWrapper) {
        IPage<T> page = PageUtil.convert2IPage(pageQuery, clazz);
        IPage<T> resultPage = mapper.selectPage(page, queryWrapper);
        return PageUtil.convert(resultPage);
    }
}
