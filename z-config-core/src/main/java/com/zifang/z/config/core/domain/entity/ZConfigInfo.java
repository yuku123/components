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
@TableName("z_config_info")
@Data
public class ZConfigInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *配置ID
     */
    private String dataId;

    /**
     *配置分组
     */
    private String group;

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
     *创建人工号
     */
    private String creatorStaffNo;

    /**
     *创建人昵称
     */
    private String creatorStaffNickNm;

    /**
     *创建人真名
     */
    private String creatorStaffRealNm;

    /**
     *创建IP
     */
    private String sourceIp;

    /**
     *应用名
     */
    private String appName;

    /**
     *命名空间（多租户隔离）
     */
    private String namespace;

    /**
     *配置描述
     */
    private String configDesc;

    /**
     *使用说明
     */
    private String configUsage;

    /**
     *生效规则
     */
    private String configEnableRule;

    /**
     *配置类型（如properties、yaml）
     */
    private String configType;

    /**
     *配置JSON schema
     */
    private String configSchema;
}