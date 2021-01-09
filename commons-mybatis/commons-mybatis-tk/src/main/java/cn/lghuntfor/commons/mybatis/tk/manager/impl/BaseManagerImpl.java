package cn.lghuntfor.commons.mybatis.tk.manager.impl;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import cn.lghuntfor.commons.common.page.PageInfo;
import cn.lghuntfor.commons.mybatis.tk.common.PageUtil;
import cn.lghuntfor.commons.mybatis.tk.manager.BaseManager;
import cn.lghuntfor.commons.mybatis.tk.mapper.CommonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于tk-mybatis
 * 基础Service的抽象类, 实现了基础接口
 * @author liaogang
 * @date 2020/9/2 11:51
 */
public abstract class BaseManagerImpl<T, M extends CommonMapper<T>> implements BaseManager<T> {

    /**
     * 日志对象, 子类可直接复用, 不需要再定义
     */
    protected Logger log = LoggerFactory.getLogger(this.getClass());

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
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<T> listAll() {
        return mapper.selectAll();
    }

    @Override
    public List<T> listByWhere(T entity) {
        return mapper.select(entity);
    }

    @Override
    public List<T> listByIds(Collection ids) {
        Object idsStr = ids.stream().map(Object::toString).collect(Collectors.joining(","));
        return mapper.selectByIds(idsStr.toString());
    }

    @Override
    public Integer countByWhere(T entity) {
        return mapper.selectCount(entity);
    }

    @Override
    public T getOne(T entity) {
        return mapper.selectOne(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int save(T entity) {
        return mapper.insert(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int saveSelective(T entity) {
        return mapper.insertSelective(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveBatch(List<T> entityList) {
        mapper.insertList(entityList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveBatch(List<T> entityList, int batchSize) {
        List<List<T>> subEntityList = CollUtil.split(entityList, batchSize);
        subEntityList.forEach(list -> mapper.insertList(list));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateById(T entity) {
        return mapper.updateByPrimaryKey(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateSelectiveById(T entity) {
        return mapper.updateByPrimaryKeySelective(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int removeById(Serializable id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int removeByIds(Collection ids) {
        Object idsStr = ids.stream().map(Object::toString).collect(Collectors.joining(","));
        return mapper.deleteByIds(idsStr.toString());
    }

    @Override
    public PageInfo<T> page(PageInfo<T> pageInfo, T entity) {
        PageHelper.startPage(pageInfo.getPage().intValue(), pageInfo.getSize().intValue());
        List<T> list = mapper.select(entity);
        com.github.pagehelper.PageInfo<T> page = new com.github.pagehelper.PageInfo<>(list);
        return PageUtil.covertPage(page);
    }
}
