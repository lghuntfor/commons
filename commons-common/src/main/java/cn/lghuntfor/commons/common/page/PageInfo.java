package cn.lghuntfor.commons.common.page;

import cn.hutool.core.convert.Convert;
import cn.lghuntfor.commons.common.query.Query;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 分页信息封装
 * @author liaogang
 * @date 2020/9/2 11:42
 */
@Data
public class PageInfo<T> implements Serializable {

    /**
     * 当前页, 默认第1页
     */
    private Long page;

    /**
     * 每页显示个数, 默认10
     */
    private Long size;

    /**
     * 排序信息:
     * property-asc 或 property-desc
     * ASC,DESC不区分大小写
     */
    private List<String> sort;

    /**
     * 总共有多少页
     */
    private Long pages;

    /**
     * 总共有多少条记录
     */
    private Long total;

    /**
     * 分页结果数据
     */
    private List<T> content = new ArrayList<>();

    /** 是否显示查询分页信息/是否查询总记录数 */
    private boolean pageShowFlag = true;

    public PageInfo() {
        this.page = 1L;
        this.size = 10L;
    }

    public PageInfo(Long page, Long size) {
        this.page = page;
        this.size = size;
    }

    public PageInfo(Long page, Long size, List<String> sort) {
        this.page = page;
        this.size = size;
        this.sort = sort;
    }

    public PageInfo(Long page, Long size, List<String> sort, Boolean pageShowFlag) {
        this.page = page;
        this.size = size;
        this.sort = sort;
        this.pageShowFlag = pageShowFlag;
    }

    public PageInfo(Long page, Long size, Long pages, Long total) {
        this.page = page;
        this.size = size;
        this.pages = pages;
        this.total = total;
    }

    public PageInfo(Long page, Long size, List<String> sort, Long pages, Long total) {
        this.page = page;
        this.size = size;
        this.pages = pages;
        this.total = total;
        this.sort = sort;
    }

    public PageInfo(Long page, Long size, List<String> sort, Long pages, Long total, Boolean pageShowFlag) {
        this.page = page;
        this.size = size;
        this.pages = pages;
        this.total = total;
        this.sort = sort;
        this.pageShowFlag = pageShowFlag;
    }

    public PageInfo(Long page, Long size, Long pages, Long total, List<T> content) {
        this.page = page;
        this.size = size;
        this.pages = pages;
        this.total = total;
        this.content = content;
    }

    public PageInfo(Long page, Long size, Long pages, Long total, List<T> content, List<String> sort) {
        this.page = page;
        this.size = size;
        this.pages = pages;
        this.total = total;
        this.content = content;
        this.sort = sort;
    }

    public PageInfo(Long page, Long size, Long pages, Long total, List<T> content, List<String> sort, Boolean pageShowFlag) {
        this.page = page;
        this.size = size;
        this.pages = pages;
        this.total = total;
        this.content = content;
        this.sort = sort;
        this.pageShowFlag = pageShowFlag;
    }

    /**
     * 从page中复制分页信息
     * @author liaogang
     * @date 2020/11/6
     * @param page
     * @return cn.lghuntfor.commons.common.page.PageInfo<T>
     */
    public static <T> PageInfo<T> copyPageInfo(PageInfo<?> page) {
        return new PageInfo(Convert.toLong(page.getPage(), 1L), Convert.toLong(page.getSize(), 10L)
                , Optional.ofNullable(page.getSort()).orElse(new ArrayList<>())
                , Convert.toLong(page.getPages(), 0L), Convert.toLong(page.getTotal(), 0L), page.isPageShowFlag());
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
