package com.seeds.admin.dto;

/**
 * 数据范围
 *
 * @author hang.yu
 * @date 2022/7/25
 */
public class DataScope {

    private String sqlFilter;

    public DataScope(String sqlFilter) {
        this.sqlFilter = sqlFilter;
    }

    public String getSqlFilter() {
        return sqlFilter;
    }

    public void setSqlFilter(String sqlFilter) {
        this.sqlFilter = sqlFilter;
    }

    @Override
    public String toString() {
        return this.sqlFilter;
    }

}