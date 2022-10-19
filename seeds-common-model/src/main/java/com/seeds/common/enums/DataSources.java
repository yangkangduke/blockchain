package com.seeds.common.enums;

/**
 * @author milo
 */
public enum DataSources {

    /**
     * 主库 支持读写
     */
    MASTER("master"),
    /**
     * 从库，只读
     */
    READONLY("readonly");

    /**
     * data source name
     */
    String value;

    DataSources(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
