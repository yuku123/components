package com.zifang.util.core.composite;

import lombok.Data;

@Data
public class Node {
    private int id;
    private int parentId;
    private String name;

    public Node(int id, int parentId, String name) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }

    @Override
    public String toString(){
        return name;
    }
}
