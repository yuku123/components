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
@TableName("z_subscription")
@Data
public class ZSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *消费实例ID（同instance_id格式）
     */
    private String consumerInstanceId;

    /**
     *消费实例IP
     */
    private String consumerIp;

    /**
     *消费实例端口
     */
    private Integer consumerPort;

    /**
     *订阅的服务ID（关联z_service_info.id）
     */
    private Long subscribeServiceId;

    /**
     *订阅服务的命名空间
     */
    private String subscribeNamespace;

    /**
     *订阅的集群名
     */
    private String subscribeCluster;

    /**
     *订阅时间
     */
    private LocalDateTime subscribeTime;

    /**
     *取消订阅时间
     */
    private LocalDateTime unsubscribeTime;

    /**
     *订阅状态（1=有效，0=取消）
     */
    private Boolean status;

    /**
     *消费端自定义元数据（JSON格式）
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