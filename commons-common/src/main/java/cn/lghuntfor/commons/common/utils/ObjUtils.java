package cn.lghuntfor.commons.common.utils;

import java.util.Optional;

/**
 * 对象工具类
 * @author liaogang
 * @date 2020/11/23 14:17
 */
public class ObjUtils {

    /**
     * 通过Optional提供安全获取对象的方法
     * 当obj为null时, 返回默认值defaultValue
     * @author liaogang
     * @date 2020/11/23
     * @param obj
     * @param defaultVal
     * @return T
     */
    public static <T> T get(T obj, T defaultVal) {
        return Optional.ofNullable(obj).orElse(defaultVal);
    }

}
