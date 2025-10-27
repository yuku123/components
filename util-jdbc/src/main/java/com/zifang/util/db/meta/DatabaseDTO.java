package com.zifang.util.db.meta;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 数据库DTO
 */
@Data
public class DatabaseDTO {

    private String databaseName;

    private List<TableDTO> tables = new ArrayList<>();

    public DatabaseDTO(String databaseName) {
        this.databaseName = Objects.requireNonNull(databaseName, "数据库名不能为空").trim();
    }

    public void addTable(TableDTO table) {
        if (table != null && !table.getColumns().isEmpty()) {
            tables.add(table);
        }
    }

    public List<TableDTO> getTables() {
        return new ArrayList<>(tables);
    }
}
