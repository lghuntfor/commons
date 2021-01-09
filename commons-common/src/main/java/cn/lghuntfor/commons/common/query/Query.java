package cn.lghuntfor.commons.common.query;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 基础的查询信息封装对象
 * @author liaogang
 * @date 2020/9/9 14:46
 */
@Data
public class Query implements Serializable {

    /** 当前页 */
    private Long page = 1L;

    /** 每页显示数 */
    private Long size = 10L;

    /** 当前所在公司id */
    private Long companyId;

    /** 当前操作的用户id */
    private Long currentUserId;

    /** 搜索条件 */
    private String search;

    /**
     * 状态：0-无效(删除),1-有效(正常),2-暂停
     */
    private Byte status;

    /**
     * 排序信息:
     * property-asc 或 property-desc
     * ASC,DESC不区分大小写
     */
    private List<String> sort;

    /** 是否显示查询分页信息/是否查询总记录数 */
    private boolean pageShowFlag = true;

    public Query() { }

}
