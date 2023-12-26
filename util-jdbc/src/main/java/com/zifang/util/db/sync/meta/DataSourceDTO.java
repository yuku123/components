package com.zifang.util.db.sync.meta;

import com.zifang.util.core.meta.Description;
import lombok.Data;

/**
 * @author zifang
 */
@Data
public class DataSourceDTO {

    @Description("数据源id")
    private Long id;

    @Description("数据标识")
    private String datasourceCode;

    @Description("数据源名称")
    private String datasourceName;

    @Description("数据源地址")
    private String datasourceUrl;

    @Description("端口")
    private Integer portNumber;

    @Description("库名称")
    private String schemaMark;

    @Description("用户名称")
    private String userName;

    @Description("密码")
    private String pw;

    @Description("当前数据描述")
    private String descriptions;

    @Description("数据源类型")
    private String datasourceType;
}
