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
@TableName("z_service_info")
@Data
public class ZServiceInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *服务名（格式：group@@name）
     */
    private String serviceName;

    /**
     *服务分组
     */
    private String group;

    /**
     *命名空间ID
     */
    private String namespace;

    /**
     *集群映射（JSON格式）
     */
    private String clusterMap;

    /**
     *缓存毫秒数
     */
    private Integer cacheMillis;

    /**
     *健康检查模式
     */
    private String healthCheckMode;

    /**
     *健康检查超时时间
     */
    private Integer healthCheckTimeout;

    /**
     *IP删除超时时间
     */
    private Integer ipDeleteTimeout;

    /**
     *创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     *修改时间
     */
    private LocalDateTime gmtModified;
}