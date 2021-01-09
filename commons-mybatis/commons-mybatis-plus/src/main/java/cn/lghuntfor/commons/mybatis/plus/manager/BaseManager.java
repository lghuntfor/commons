package cn.lghuntfor.commons.mybatis.plus.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.lghuntfor.commons.common.page.PageInfo;
import cn.lghuntfor.commons.mybatis.api.Manager;

import java.util.Collection;
import java.util.List;

/**
 * 基础服务接口
 * @author liaogang
 * @date 2020/9/2 11:33
 */
public interface BaseManager<T> extends Manager<T> {

    /**
     * 真实的批量插入数据方法
     * 生成SQL: insert into table_name values (), (), ()
     * 注意: entity中为null的属性, 插入也会设置成null, 无法复用数据表中的默认值, 需要手动设置默认值
     * (1)注意有默认值的字段,需要程序中手动设置才生效
     * (2)注意字段非空的情况,也需要程序设置值,不然会报错
     * @author liaogang
     * @date 2020/11/18
     * @param entityList
     * @return int
     */
    int insertBatch(Collection<T> entityList);

    /**
     * 分批批量保存
     * @author liaogang
     * @date 2020/11/18
     * @param entityList
     * @param batchSize
     * @return int
     */
    int insertBatch(Collection<T> entityList, int batchSize);

    /**
     * 条件查询, 获取list结果
     * @author liaogang
     * @date 2020/9/2
     * @param queryWrapper
     * @return java.util.List<T>
     */
    List<T> listByQuery(QueryWrapper<T> queryWrapper);

    /**
     * 条件查询, 获取记录数
     * @author liaogang
     * @date 2020/9/2
     * @param queryWrapper
     * @return java.lang.Integer
     */
    Integer countByQuery(QueryWrapper<T> queryWrapper);

    /**
     * 条件查询, 获取单个结果
     * @author liaogang
     * @date 2020/9/2
     * @param queryWrapper
     * @return T
     */
    T getOne(QueryWrapper<T> queryWrapper);

    /**
     * 更新指定条件的数据
     * @author liaogang
     * @date 2020/9/2
     * @param entity
     * @param queryWrapper
     * @return void
     */
    int updateByQuery(T entity, QueryWrapper<T> queryWrapper);

    /**
     * 条件删除
     * @author liaogang
     * @date 2020/9/23
     * @param queryWrapper
     * @return void
     */
    int removeByQuery(QueryWrapper queryWrapper);

    /**
     * 分布查询
     * @author liaogang
     * @date 2020/9/2
     * @param pageQuery
     * @param queryWrapper
     * @return cn.lghuntfor.commons.constants.page.PageInfo
     */
    PageInfo<T> page(PageInfo<T> pageQuery, QueryWrapper<T> queryWrapper);
}
