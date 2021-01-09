package cn.lghuntfor.commons.mybatis.plus.common;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.lghuntfor.commons.common.constants.Consts;
import cn.lghuntfor.commons.common.constants.SortType;
import cn.lghuntfor.commons.common.page.PageInfo;
import cn.lghuntfor.commons.common.page.Sort;
import cn.lghuntfor.commons.common.query.Query;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 分页转换工具类
 * @author liaogang
 * @date 2020/9/2 14:28
 */
public class PageUtil {

    /**
     * mybatis-plus Page 转为 自定义的PageInfo
     * @author liaogang
     * @date 2020/11/5
     * @param page
     * @return cn.lghuntfor.commons.common.page.PageInfo<T>
     */
    public static <T> PageInfo<T> convert(IPage<T> page) {
        List<OrderItem> orders = page.orders();
        List<String> sorts = new ArrayList<>();
        if (CollUtil.isNotEmpty(orders)) {
            orders.forEach(o -> sorts.add(StringUtils.underlineToCamel(o.getColumn()) + Consts.DASH
                    + (o.isAsc() ? SortType.ASC.name(): SortType.DESC.name()).toLowerCase()));
        }
        return new PageInfo(page.getCurrent(), page.getSize(), page.getPages(), page.getTotal(), page.getRecords(), sorts, page.isSearchCount());
    }


    /**
     * 自定义的PageInfo Page 转为 mybatis-plus
     * @author liaogang
     * @date 2020/11/5
     * @param pageQuery
     * @return com.baomidou.mybatisplus.core.metadata.IPage<T>
     */
    public static <T> IPage<T>  convert2IPage(PageInfo<T> pageQuery) {
        return convert2IPage(pageQuery, null);
    }


    /**
     * 自定义的PageInfo Page 转为 mybatis-plus
     * clazz 实体类型 会针对提供的排序字体进行过滤
     * @author liaogang
     * @date 2020/11/5
     * @param pageQuery
     * @param clazz 实体类型 会针对提供的排序字体进行过滤
     * @return com.baomidou.mybatisplus.core.metadata.IPage<T>
     */
    public static <T> IPage<T>  convert2IPage(PageInfo<T> pageQuery, Class clazz) {
        if (pageQuery == null) {
            return new Page<>();
        }
        Long current = pageQuery.getPage();
        Long size = pageQuery.getSize();
        if (current == null || current <= 0) {
            current = 1L;
        }
        if (size == null || size <= 0) {
            size = 10L;
        }
        size = (size > Consts.MAX_PAGE_SIZE) ? Consts.MAX_PAGE_SIZE : size;

        Page<T> page = new Page<>(current, size, pageQuery.isPageShowFlag());
        List<String> sorts = pageQuery.getSort();
        List<Sort> sortList = SortUtil.getSorts(sorts, clazz);
        if (CollUtil.isNotEmpty(sortList)) {
            List<OrderItem> items = new ArrayList<>(sortList.size());
            sortList.forEach(s -> items.add(new OrderItem(s.getColumn(), s.isAsc())));
            page.addOrder(items);
        }
        return page;
    }

    /**
     * 由Query对象构建PageInfo对象
     * @author liaogang
     * @date 2020/11/5
     * @param query
     * @return cn.lghuntfor.commons.common.page.PageInfo<T>
     */
    public static <T> PageInfo<T> buildPageInfo(Query query) {
        return new PageInfo(Convert.toLong(query.getPage(), 1L), Convert.toLong(query.getSize(), 10L)
                , Optional.ofNullable(query.getSort()).orElse(new ArrayList<>()), query.isPageShowFlag());
    }
}
