package cn.lghuntfor.commons.common.tree;

import cn.hutool.core.collection.CollUtil;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * 树形结构的工具类
 * @author liaogang
 * @date 2020/9/29 09:26
 */
public class TreeUtils {


    /**
     * 将一个list集合，构建成一个树结构, 返回树结构的list
     * @author liaogang
     * @date 2020/9/29
     * @param sourceList
     * @return java.util.List<cn.lghuntfor.edu.base.common.tree.TreeNode<ID,T>>
     */
    public static List<TreeNode> buildTree(List<TreeNode> sourceList) {
        List ids = sourceList.stream().map(TreeNode::getId).collect(Collectors.toList());
        List<TreeNode> topList = sourceList.stream().filter(t -> !ids.contains(t.getPid())).collect(Collectors.toList());
        List<TreeNode> childrenList = sourceList.stream().filter(t -> ids.contains(t.getPid())).collect(Collectors.toList());

        for (TreeNode pTreeNode : topList) {
            buildTree(childrenList, pTreeNode);
        }
        return topList;
    }


    /**
     * 对指定节点构建其子树
     * @author liaogang
     * @date 2020/9/29
     * @param sourceList
     * @param pTreeNode
     * @return void
     */
    public static void buildTree(List<TreeNode> sourceList, TreeNode pTreeNode) {
        for (TreeNode cTreeNode : sourceList) {
            if (cTreeNode.getPid().equals(pTreeNode.getId())) {
                pTreeNode.addChildren(cTreeNode);
                buildTree(sourceList, cTreeNode);
            }
        }
    }


    /**
     * 树的遍历
     * 遍历节点时,可以通过consumer进行自定义操作, 两个node分别是当前节点, 父级节点
     * @author liaogang
     * @date 2020/11/6
     * @param treeList
     * @param pNode
     * @param consumer 遍历节点时,封装的行为, 两个node分别是当前节点, 父级节点, consumer可以为null
     * @return void
     */
    public static void traversal(List<TreeNode> treeList, TreeNode pNode, BiConsumer<TreeNode, TreeNode> consumer) {
        if (CollUtil.isEmpty(treeList)) {
            return;
        }
        for (TreeNode node : treeList) {
            if (consumer != null) {
                consumer.accept(node, pNode);
            }
            traversal(node.getChildren(), node, consumer);
        }
    }

}
