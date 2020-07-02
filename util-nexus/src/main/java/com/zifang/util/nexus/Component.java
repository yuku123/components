package com.zifang.util.nexus;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Maven组件
 */
@Data
public class Component implements Comparable<Component> {

    private String id;

    private String repository;

    private String group;

    private String name;

    private String version;

    private String format;

    private List<Asset> assets = new ArrayList<>();

    @Override
    public int compareTo(Component o) {
        return this.version.compareTo(o.getVersion());
    }
}
