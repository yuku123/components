package com.zifang.z.config.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 无注释
 */
@TableName("z_config_info_history")
@Data
public class ZConfigInfoHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *配置历史ID
     */
    private Long nid;

    /**
     *配置ID
     */
    private String dataId;

    /**
     *配置分组
     */
    private String group;

    /**
     *应用名
     */
    private String appName;

    /**
     *配置内容
     */
    private String content;

    /**
     *内容MD5
     */
    private String md5;

    /**
     *创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     *修改时间
     */
    private LocalDateTime gmtModified;

    /**
     *操作人
     */
    private String srcUser;

    /**
     *操作IP
     */
    private String srcIp;

    /**
     *操作类型（新增/修改/删除）
     */
    private String opType;

    /**
     *命名空间
     */
    private String namespace;
}