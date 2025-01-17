package com.lantu.domain.vo;

import lombok.Data;

/**
 * @author XuShiPing
 * @since 2025/1/17  21:44
 */

@Data
public class StatusNum {
    int matchStatusNum;
    int notMatchStatusNum;
    int fixedMatchStatusNum;
    int errorStatusNum;
}
