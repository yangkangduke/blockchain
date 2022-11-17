package com.seeds.admin.enums;

import lombok.Getter;

/**
 * @author hang.yu
 * @date 2022/8/18
 */
@Getter
public enum RandomCodeStatusEnum {

    NORMAL(0, "正常"),
    GENERATING(1, "生成中"),
    GENERATE_FAILED(2, "生成失败"),
    EXPORTING(3, "导出中"),
    EXPORT_FAILED(4, "导出失败"),
    EXPIRED(5, "已失效"),
    ;

    private final int code;
    private final String desc;

    RandomCodeStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}