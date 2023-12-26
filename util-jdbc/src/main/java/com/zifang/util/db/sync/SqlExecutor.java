package com.zifang.util.db.sync;

import com.zifang.util.core.common.status.BaseStatusCode;
import com.zifang.util.core.lang.exception.BusinessException;
import com.zifang.util.db.sync.meta.DataSourceTableColumnDTO;
import com.zifang.util.db.sync.meta.DataSourceTableDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zifang
 */
@Slf4j
public class SqlExecutor {

    private DataSource dataSource;

    private static Map<String, DataSource> dataSourceCache = new LinkedHashMap<>();

    public List<DataSourceTableDTO> fetchTableInfo(String schemaMark) {
        List<DataSourceTableDTO> dataSourceTableDTOS = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            ResultSet resultSet = conn.getMetaData().getTables(schemaMark, "%", "%", new String[]{"TABLE"});
            while (resultSet.next()) {
                DataSourceTableDTO dataSourceTableDTO = new DataSourceTableDTO();
                dataSourceTableDTO.setTableName(resultSet.getString("TABLE_NAME"));
                dataSourceTableDTO.setDescriptions(resultSet.getString("REMARKS"));
                dataSourceTableDTOS.add(dataSourceTableDTO);
            }
            return dataSourceTableDTOS;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BusinessException(BaseStatusCode.FAIL, "获取表信息出错");
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<DataSourceTableColumnDTO> fetchTableColumnInfo(String schemaMark, String tableName) {
        List<DataSourceTableColumnDTO> dataSourceTableColumnDTOS = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            ResultSet resultSet = conn.getMetaData().getColumns(schemaMark, "%", tableName, null);
            while (resultSet.next()) {
                DataSourceTableColumnDTO dataSourceTableColumnDTO = new DataSourceTableColumnDTO();
                dataSourceTableColumnDTO.setColumnName(resultSet.getString("COLUMN_NAME"));
                dataSourceTableColumnDTO.setColumnType(resultSet.getString("TYPE_NAME").toLowerCase());
                dataSourceTableColumnDTO.setColumnComment(resultSet.getString("REMARKS"));
                dataSourceTableColumnDTOS.add(dataSourceTableColumnDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dataSourceTableColumnDTOS;
    }

    public void createTable(String tableName, String descriptions) throws RuntimeException {
        String sql = String.format(
                "create table IF NOT EXISTS %s(id bigint(20) comment '主键') COMMENT='%s' ENGINE=InnoDB DEFAULT CHARSET=utf8;",
                tableName,
                descriptions
        );
        executeDML(dataSource, sql);
    }

    public void createTableColumn(String tableName, String columnName, String columnType , String columnComment) throws RuntimeException {
        String sql = String.format("ALTER TABLE %s ADD %s %s comment '%s'",
                tableName,
                columnName,
                columnType,
                columnComment
        );
        executeDML(dataSource, sql);
    }

    public void updateTableColumn(String tableName, String columnName, String targetColumnName, String targetColumnType , String targetColumnComment) throws RuntimeException {
        String sql = String.format("ALTER TABLE %s change %s %s %s comment '%s'",
                tableName,
                columnName,
                targetColumnName,
                targetColumnType,
                targetColumnComment
        );
        executeDML(dataSource, sql);
    }

    public void removeTableColumn(String tableName, String columnName) throws RuntimeException {
        String sql = String.format("ALTER TABLE %s drop column %s;", tableName, columnName);
        executeDML(dataSource, sql);
    }

    public void updateTable(String tableName, String targetTableName, String targetTableComments) throws RuntimeException {
        String sql1 = String.format("ALTER TABLE %s rename to %s",
                tableName,
                targetTableName
        );

        String sql2 = String.format("alter TABLE %s comment '%s';",
                tableName,
                targetTableComments
        );
        executeDML(dataSource, sql1);
        executeDML(dataSource, sql2);
    }

    public void removeTable(String tableName, String descriptions) throws RuntimeException {
        String sql = String.format(" DROP TABLE %s", tableName);
        executeDML(dataSource, sql);
    }

    public List<DataSourceTableDTO> deepFetchTableInfo(DataSource dataSource, String schemaMark) {
        List<DataSourceTableDTO> dataSourceTableDTOS = new ArrayList<>();
        List<DataSourceTableColumnDTO> dataSourceTableColumnDTOS = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            ResultSet tableResultSet = conn.getMetaData().getTables(schemaMark, "%", "%", new String[]{"TABLE"});
            while (tableResultSet.next()) {
                DataSourceTableDTO dataSourceTableDTO = new DataSourceTableDTO();
                dataSourceTableDTO.setTableName(tableResultSet.getString("TABLE_NAME"));
                dataSourceTableDTO.setDescriptions(tableResultSet.getString("REMARKS"));
                dataSourceTableDTOS.add(dataSourceTableDTO);
            }

            ResultSet columnResultSet = conn.getMetaData().getColumns(schemaMark, "%", "%", null);
            while (columnResultSet.next()) {
                DataSourceTableColumnDTO dataSourceTableColumnDTO = new DataSourceTableColumnDTO();
//                dataSourceTableColumnDTO.setDatasourceCode();
                dataSourceTableColumnDTO.setTableName(columnResultSet.getString("TABLE_NAME"));
                dataSourceTableColumnDTO.setColumnName(columnResultSet.getString("COLUMN_NAME"));
                dataSourceTableColumnDTO.setColumnType(columnResultSet.getString("TYPE_NAME"));
                dataSourceTableColumnDTO.setColumnComment(columnResultSet.getString("REMARKS"));
                dataSourceTableColumnDTOS.add(dataSourceTableColumnDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        Map<String, List<DataSourceTableColumnDTO>> columnMap = dataSourceTableColumnDTOS.stream().collect(Collectors.groupingBy(DataSourceTableColumnDTO::getTableName));
        dataSourceTableDTOS.forEach(e -> e.setColumns(columnMap.get(e.getTableName())));

        return dataSourceTableDTOS;
    }

    public void executeDml(DataSource dataSource, String sql) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            Statement smt = connection.createStatement();
            log.info("execute dml sql:{}", sql);
            smt.executeUpdate(sql);
        } catch (SQLException e) {
            log.error(String.format("执行操作数据库失败：执行的sql:%S,错误信息 %S", sql, e.getMessage()));
            throw new BusinessException(BaseStatusCode.FAIL, "执行操作数据库失败，请通知值班人员");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Map<String, Object>> selectList(DataSource dataSource, String sql) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            QueryRunner qr = new QueryRunner();
            List<Map<String, Object>> l = qr.query(connection, sql, new MapListHandler());
            return l == null ? new ArrayList<>() : l;
        } catch (SQLException e) {
            log.error(String.format("数据库查询异常：执行的sql:%S,错误信息 %S", sql, e.getMessage()));
            throw new BusinessException(BaseStatusCode.FAIL, "数据库查询异常，请通知值班人员");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Integer count(DataSource dataSource, String sqlCnt) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            QueryRunner qr = new QueryRunner();
            return Integer.valueOf(String.valueOf(qr.query(connection, sqlCnt, new ScalarHandler())));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void executeDML(DataSource dataSource, String sql) throws RuntimeException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            Statement smt = connection.createStatement();
            smt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("操作数据表失败，请联系管理员！");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}