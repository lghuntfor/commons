package cn.lghuntfor.commons.mybatis.plus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;

/**
 * 通用Mapper
 * 添加批量插入的方法
 * @author liaogang
 * @date 2020/11/18 16:29
 */
public interface CommonMapper<T> extends BaseMapper<T> {

    /**
     * 批量新增方法
     * 生成SQL: insert into table_name values (), (), ()
     * 注意: entity中为null的属性, 插入也会设置成null, 无法复用数据表中的默认值, 需要手动设置默认值
     * (1)注意有默认值的字段,需要程序中手动设置才生效
     * (2)注意字段非空的情况,也需要程序设置值,不然会报错
     * @author liaogang
     * @date 2020/11/18
     * @param entityList
     * @return int
     */
    int insertBatchSomeColumn(Collection<T> entityList);
}
