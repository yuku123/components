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
@TableName("z_instance")
@Data
public class ZInstance implements Serializable {

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
     *实例唯一ID（格式：serviceId@@ip:port）
     */
    private String instanceId;

    /**
     *实例IP
     */
    private String ip;

    /**
     *实例端口
     */
    private Integer port;

    /**
     *权重
     */
    private Double weight;

    /**
     *健康状态（1=健康，0=不健康）
     */
    private Boolean healthy;

    /**
     *是否启用（1=启用，0=禁用）
     */
    private Boolean enabled;

    /**
     *是否临时实例
     */
    private Boolean ephemeral;

    /**
     *集群名
     */
    private String clusterName;

    /**
     *元数据（JSON格式）
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