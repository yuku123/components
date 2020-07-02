package com.zifang.util.nexus;

import lombok.Data;

@Data
public class Asset {

    private String id;

    private String repository;

    private String path;

    private String downloadUrl;

    private Checksum checksum;

    private String format;

}