package com.zifang.util.db.test0;

import lombok.Data;

import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Table(name = "resource_item")
public class ResourceItem {
    private Long id;
    private String cmsId;
    private String type;
    private int ownerId;
    private Boolean shared;
    private Timestamp createTime;
    private Timestamp updateTime;
}
