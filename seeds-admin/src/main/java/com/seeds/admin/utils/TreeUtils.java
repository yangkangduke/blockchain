package com.seeds.admin.utils;

import com.seeds.admin.dto.TreeNode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 树形结构工具类
 *
 * @author hang.yu
 * @date 2022/7/14
 */
public class TreeUtils {

    /**
     * 根据父级code，构建树节点
     */
    public static <T extends TreeNode> List<T> buildTree(List<T> treeNodes) {
        List<T> treeList = new ArrayList<>();
        for(T treeNode : treeNodes) {
            if (StringUtils.isEmpty(treeNode.getParentCode())) {
                treeList.add(findChildren(treeNodes, treeNode));
            }
        }

        return treeList;
    }

    /**
     * 查找子节点
     */
    private static <T extends TreeNode> T findChildren(List<T> treeNodes, T rootNode) {
        for(T treeNode : treeNodes) {
            if(rootNode.getCode().equals(treeNode.getParentCode())) {
                rootNode.getChildren().add(findChildren(treeNodes, treeNode));
            }
        }
        return rootNode;
    }

    /**
     * 构建树节点
     */
    public static <T extends TreeNode> List<T> build(List<T> treeNodes) {
        List<T> result = new ArrayList<>();

        //list转map
        Map<String, T> nodeMap = new LinkedHashMap<>(treeNodes.size());
        for(T treeNode : treeNodes){
            nodeMap.put(treeNode.getCode(), treeNode);
        }

        for(T node : nodeMap.values()) {
            T parent = nodeMap.get(node.getParentCode());
            if(parent != null && !(node.getCode().equals(parent.getCode()))){
                parent.getChildren().add(node);
                continue;
            }

            result.add(node);
        }

        return result;
    }

}