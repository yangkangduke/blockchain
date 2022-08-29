package com.seeds.admin.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 树节点
 *
 * @author hang.yu
 * @date 2022/7/14
 */
public class TreeNode<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 编码
     */
    private String code;
    /**
     * 上级编码
     */
    private String parentCode;
    /**
     * 子节点列表
     */
    private List<T> children = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }
}