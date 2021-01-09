package cn.lghuntfor.commons.mybatis.tk.mapper;

import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;


/**
 * 通用Mapper接口
 * @author liaogang
 * @date 2020/9/2 11:54
 */
public interface CommonMapper<T> extends Mapper<T>
        , InsertListMapper<T>, IdsMapper<T> {
}
