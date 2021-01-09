package cn.lghuntfor.commons.cache.multi.common;

/**
 * 数据加载接口
 * 类似Supplier
 * @author liaogang
 * @date 2020/9/16 11:16
 */
@FunctionalInterface
public interface FuncData<T> {

    /**
     * 加载数据
     * @author liaogang
     * @date 2020/9/16
     * @return T
     */
    T loadData();

}
