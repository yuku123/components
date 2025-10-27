package com.zifang.util.db.meta;

import com.zifang.util.core.meta.Description;
import lombok.Data;

/**
 * @author zifang
 */
@Data
public class DataSourceTableColumnDTO {

    @Description("列id")
    private Long datasourceTableColumnId;

    @Description("数据标记")
    private String datasourceCode;

    @Description("当前字段归属表名称")
    private String tableName;

    @Description("列名称")
    private String columnName;

    @Description("列类型")
    private String columnType;

    @Description("列长度")
    private String columnLength;

    @Description("列的注解")
    private String columnComment;

    public String nativeSignature() {
        return tableName + ":" + columnType + ":" + columnComment;
    }
}
