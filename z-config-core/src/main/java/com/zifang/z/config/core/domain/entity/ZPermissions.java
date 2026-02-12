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
@TableName("z_permissions")
@Data
public class ZPermissions implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *关联z_roles.role
     */
    @TableId(type = IdType.AUTO)
    private String role;

    /**
     *资源标识（如配置data_id:group）
     */
    @TableId(type = IdType.AUTO)
    private String resource;

    /**
     *权限操作（read/write/delete）
     */
    @TableId(type = IdType.AUTO)
    private String action;
}