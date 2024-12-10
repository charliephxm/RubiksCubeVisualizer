package com.rubikssolver.rubikscubevisualizer;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private final String name;
    private final List<TreeNode> children;

    public TreeNode(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }
}