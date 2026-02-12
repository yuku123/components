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
@TableName("z_roles")
@Data
public class ZRoles implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *关联z_users.username
     */
    @TableId(type = IdType.AUTO)
    private String username;

    /**
     *角色名
     */
    @TableId(type = IdType.AUTO)
    private String role;
}