package cn.lghuntfor.commons.common.tree;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用树节点
 * 可通过继承来扩展字段
 * @author liaogang
 * @date 2020/11/10 10:29
 */
@Data
public class TreeNode implements Serializable {

    /** 节点id */
    protected Serializable id;

    /** 父节点id */
    protected Serializable pid;

    /**父节点名称*/
    protected String name;

    /** 子节点 */
    protected List<TreeNode> children;

    /** 额外节点 */
    protected Map<String, Object> extra;

    public void addChildren(TreeNode treeNode) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(treeNode);
    }

    public void addChildren(List<TreeNode> treeNodeList) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.addAll(treeNodeList);
    }

    public void putExtra(String key, Object value) {
        if (extra == null) {
            extra = new HashMap<>();
        }
        extra.put(key, value);
    }

}
