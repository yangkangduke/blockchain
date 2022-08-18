package com.seeds.admin.enums;

import lombok.Getter;

/**
 * @author hang.yu
 * @date 2022/8/18
 */
@Getter
public enum NftInitStatusEnum {

    CREATING(0, "创建中"),
    CREATE_SUCCESS(1, "创建成功"),
    CREATE_FAILED(2, "创建失败"),
    UPDATING(3, "修改中"),
    UPDATE_SUCCESS(4, "修改成功"),
    UPDATE_FAILED(5, "修改失败"),
    ;

    private final int code;
    private final String desc;

    NftInitStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}