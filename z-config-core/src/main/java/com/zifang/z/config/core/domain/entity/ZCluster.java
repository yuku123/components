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
@TableName("z_cluster")
@Data
public class ZCluster implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *关联z_service_info.id
     */
    private Long serviceId;

    /**
     *集群名
     */
    private String name;

    /**
     *健康检查类型
     */
    private String healthCheckType;

    /**
     *健康检查URL
     */
    private String healthCheckUrl;

    /**
     *健康检查间隔（毫秒）
     */
    private Integer healthCheckInterval;

    /**
     *集群元数据
     */
    private String metadata;

    /**
     *创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     *修改时间
     */
    private LocalDateTime gmtModified;
}