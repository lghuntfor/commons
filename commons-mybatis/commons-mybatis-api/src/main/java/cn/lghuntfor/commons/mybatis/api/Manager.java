package cn.lghuntfor.commons.mybatis.api;

import cn.lghuntfor.commons.common.page.PageInfo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 请添加描述
 *
 * @author liaogang
 * @date 2020/9/29 18:08
 */
public interface Manager<T> {

    /**
     * 获取指定Id的值
     * @author liaogang
     * @date 2020/9/2
     * @param id
     * @return T
     */
    T getById(Serializable id);

    /**
     * 查询所有结果, 对于数据比较多的表, 慎用
     * @author liaogang
     * @date 2020/9/2
     * @param
     * @return java.util.List<T>
     */
    List<T> listAll();

    /**
     * 条件查询, 获取list结果
     * @author liaogang
     * @date 2020/9/2
     * @param entity
     * @return java.util.List<T>
     */
    List<T> listByWhere(T entity);

    /**
     * 获取多个指定id的结果
     * @author liaogang
     * @date 2020/9/2
     * @param ids
     * @return java.util.List<T>
     */
    List<T> listByIds(Collection ids);

    /**
     * 条件查询, 获取记录数
     * @author liaogang
     * @date 2020/9/2
     * @param entity
     * @return java.lang.Integer
     */
    Integer countByWhere(T entity);

    /**
     * 条件查询, 获取单个结果
     * @author liaogang
     * @date 2020/9/2
     * @param entity
     * @return T
     */
    T getOne(T entity);

    /**
     * 保存一条数据, 没有值的字段会置为null, 不会取字段的默认值, 非空约束可能会保错
     * @author liaogang
     * @date 2020/9/2
     * @param entity
     * @return void
     */
    int save(T entity);

    /**
     * 保存一条数据, 只保存有值的字段, 没值的字段会取字段的默认值
     * @author liaogang
     * @date 2020/9/2
     * @param entity
     * @return void
     */
    int saveSelective(T entity);

    /**
     * 批量保存
     * @author liaogang
     * @date 2020/9/2
     * @param entityList
     * @return void
     */
    void saveBatch(List<T> entityList);

    /**
     * 分批批量保存
     * @author liaogang
     * @date 2020/9/2
     * @param entityList
     * @param batchSize
     * @return void
     */
    void saveBatch(List<T> entityList, int batchSize);

    /**
     * 根据ID更新一条记录, 没有值的字段也会被更新成null
     * @author liaogang
     * @date 2020/9/2
     * @param entity
     * @return void
     */
    int updateById(T entity);

    /**
     * 根据ID更新一条记录, 只会更新有值的字段
     * @author liaogang
     * @date 2020/9/2
     * @param entity
     * @return void
     */
    int updateSelectiveById(T entity);

    /**
     * 批量编辑
     * @author liaogang
     * @date 2020/9/2
     * @param entityList
     * @return void
     */
    void updateBatch(List<T> entityList);

    /**
     * 分批批量编辑
     * @author liaogang
     * @date 2020/9/2
     * @param entityList
     * @param batchSize
     * @return void
     */
    void updateBatch(List<T> entityList, int batchSize);

    /**
     * 删除指定Id的数据
     * @author liaogang
     * @date 2020/9/2
     * @param id
     * @return void
     */
    int removeById(Serializable id);

    /**
     * 批量删除指定id值的数据
     * @author liaogang
     * @date 2020/9/2
     * @param ids
     * @return void
     */
    int removeByIds(Collection ids);

    /**
     * 分布查询
     * @author liaogang
     * @date 2020/9/2
     * @param pageQuery
     * @param entity
     * @return cn.lghuntfor.commons.constants.page.PageInfo
     */
    PageInfo<T> page(PageInfo<T> pageQuery, T entity);

}
