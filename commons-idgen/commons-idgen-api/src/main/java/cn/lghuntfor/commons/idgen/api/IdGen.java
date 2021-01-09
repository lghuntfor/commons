package cn.lghuntfor.commons.idgen.api;

/**
 * 获取Id的接口定义
 * @author liaogang
 * @date 2020/9/2 09:21
 */
public interface IdGen {

    /**
     * 获取id
     * @author liaogang
     * @date 2020/9/2
     * @return java.lang.Long
     */
    Long nextId();

    /**
     * 根据业务key, 获取对应的id
     * @author liaogang
     * @date 2020/9/2
     * @param businessKey
     * @return java.lang.Long
     */
    Long nextId(String businessKey);

}
