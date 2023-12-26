package com.zifang.util.db.sync.meta;

import com.zifang.util.core.meta.Description;
import lombok.Data;

import java.util.List;

/**
 * @author zifang
 */
@Data
public class DataSourceTableDTO {

    @Description("数据源下表 id")
    private Long datasourceTableId;

    @Description("数据标识")
    private String datasourceCode;

    @Description("表名称")
    private String tableName;

    @Description("表描述")
    private String descriptions;

    @Description("表下的字段信息")
    private List<DataSourceTableColumnDTO> columns;

    public String getComponentCode() {
        return datasourceCode + ":" + tableName;
    }
}
