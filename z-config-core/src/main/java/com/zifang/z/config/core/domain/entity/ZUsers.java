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
@TableName("z_users")
@Data
public class ZUsers implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *用户名
     */
    @TableId(type = IdType.AUTO)
    private String username;

    /**
     *密码（BCrypt加密）
     */
    private String password;

    /**
     *是否启用（1=启用，0=禁用）
     */
    private Boolean enabled;
}