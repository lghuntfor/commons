package cn.lghuntfor.commons.common.convert;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 对象转换工具类
 * @author liaogang
 * @date 2020/11/11 14:09
 */
@Slf4j
public class ConvertUtils {

    /**
     * 将已知对象转换成指定类型对象
     * @author liaogang
     * @date 2020/11/11
     * @param source
     * @param targetClazz
     * @return T
     */
    public static <T> T convert(Object source, Class<T> targetClazz) {
        T target = null;
        if (source != null) {
            try {
                target = targetClazz.newInstance();
                BeanUtils.copyProperties(source, target);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return target;
    }

    /**
     * 将已知对象集合转换成指定类型对象集合
     * @author liaogang
     * @date 2020/11/11
     * @param sourceList
     * @param targetClazz
     * @return T
     */
    public static <T> List<T> convertList(Collection<?> sourceList, Class<T> targetClazz) {
        List<T> targetList = null;
        if (CollUtil.isNotEmpty(sourceList)) {
            targetList = new ArrayList<>(sourceList.size());
            for (Object source : sourceList) {
                T target = convert(source, targetClazz);
                if (target != null) {
                    targetList.add(target);
                }
            }
        }
        return targetList;
    }

}
