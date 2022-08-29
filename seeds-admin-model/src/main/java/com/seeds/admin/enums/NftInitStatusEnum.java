package com.seeds.admin.enums;

import lombok.Getter;

/**
 * @author hang.yu
 * @date 2022/8/18
 */
@Getter
public enum NftInitStatusEnum {

    NORMAL(0, "正常"),
    CREATING(1, "创建中"),
    CREATE_FAILED(2, "创建失败"),
    UPDATING(3, "修改中"),
    UPDATE_FAILED(4, "修改失败"),
    DELETING(5, "删除中"),
    DELETE_FAILED(6, "删除失败"),
    ;

    private final int code;
    private final String desc;

    NftInitStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}