package cn.lghuntfor.commons.mybatis.plus.common;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.lghuntfor.commons.common.constants.Consts;
import cn.lghuntfor.commons.common.constants.SortType;
import cn.lghuntfor.commons.common.page.Sort;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * mybatis plus排序工具
 * @author liaogang
 * @date 2020/10/27 11:29
 */
public class SortUtil {


    /**
     * 向QueryWrapper中添加排序信息
     * @author liaogang
     * @date 2020/10/27
     * @param sortList
     * @param qw
     * @return void
     */
    public static void addSort(List<String> sortList, QueryWrapper<?> qw) {
        addSort(sortList, qw, null);
    }

    /**
     * 向QueryWrapper中添加排序信息
     * clazz 实体类型 会针对提供的排序字体进行过滤
     * @author liaogang
     * @date 2020/10/27
     * @param sortList
     * @param qw
     * @param clazz 实体类型 会针对提供的排序字体进行过滤
     * @return void
     */
    public static void addSort(List<String> sortList, QueryWrapper<?> qw, Class clazz) {
        List<Sort> sorts = getSorts(sortList, clazz);
        if (CollUtil.isEmpty(sorts)) {
            return;
        }
        sorts.forEach(sort -> {
            if (sort.isAsc()) {
                qw.orderByAsc(sort.getColumn());
            } else {
                qw.orderByDesc(sort.getColumn());
            }
        });
    }


    /**
     * 获取排序信息
     * property已经转换成column
     * @author liaogang
     * @date 2020/11/4
     * @param sortList
     * @return
     */
    public static List<Sort> getSorts(List<String> sortList) {
        return getSorts(sortList, null);
    }


    /**
     * 获取排序信息
     * property已经转换成column
     * clazz 实体类型 会针对提供的排序字体进行过滤
     * @author liaogang
     * @date 2020/11/4
     * @param sortList
     * @param clazz 实体类型 会针对提供的排序字体进行过滤
     * @return
     */
    public static List<Sort> getSorts(List<String> sortList, Class clazz) {
        if (CollUtil.isEmpty(sortList)) {
            return null;
        }
        List<String> propertyNames = new ArrayList<>();
        if (clazz != null) {
            /** 只获取类型自身的字段 */
            ReflectionUtils.doWithLocalFields(clazz, (f) -> {
                propertyNames.add(f.getName());
            });
        }
        /** 是否需要针对sort中的property检测过滤 */
        boolean needCheck = CollUtil.isNotEmpty(propertyNames);

        List<Sort> sorts = new ArrayList<>(sortList.size());
        for (String sort : sortList) {
            if (StrUtil.isBlank(sort)) {
                continue;
            }
            String[] sortArr = sort.split(Consts.DASH);
            if (sortArr.length == 2) {
                /** 将property已经转换成column */
                String propertyName = sortArr[0];
                sortArr[0] = StrUtil.toUnderlineCase(propertyName);
                boolean isAsc;
                if (SortType.DESC.name().equalsIgnoreCase(sortArr[1])) {
                    isAsc = false;
                } else {
                    /** 非反序的情况都视为正序, 即默认正序 */
                    isAsc = true;
                }
                if (needCheck && !propertyNames.contains(propertyName)) {
                    /** 针对property进行过滤, 不符合要求忽略掉 */
                    continue;
                }
                sorts.add(new Sort(sortArr[0], isAsc));
            }
        }
        return sorts;
    }

}
