package com.zifang.util.db.test0;

import lombok.Data;

import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Table(name = "resource_item")
public class ResourceItem {
    private Long id;
    private String cmsId;
    private String type;
    private Long ownerId;
    private int shared;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
