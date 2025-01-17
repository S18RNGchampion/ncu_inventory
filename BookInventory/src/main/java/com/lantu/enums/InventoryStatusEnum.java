package com.lantu.enums;

import lombok.Getter;

/**
 * @author XuShiPing
 * @since 2025/1/17  21:32
 */
@Getter
public enum InventoryStatusEnum {

    matchStatus(1,"匹配"),
    notMatchStatus(2,"未匹配"),
    fixedMatchStatus(3,"待确定"),
    errorStatus(0,"识别失败");

    private final int status;
    private final String info;

    InventoryStatusEnum(int status, String info) {
        this.status = status;
        this.info = info;
    }

}
