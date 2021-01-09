package cn.lghuntfor.commons.mybatis.tk.common;

import cn.hutool.core.convert.Convert;
import cn.lghuntfor.commons.common.page.PageInfo;

/**
 * 分页工具类
 * @author liaogang
 * @date 2020/9/2 14:03
 */
public class PageUtil {

    public static <T> PageInfo<T> covertPage(com.github.pagehelper.PageInfo<T> page) {
        return new PageInfo(Convert.toLong(page.getPageNum()), Convert.toLong(page.getPageSize()), Convert.toLong(page.getPages()), page.getTotal(), page.getList());
    }

}
