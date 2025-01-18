package com.lantu.domain.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author XuShiPing
 * @since 2025/1/17  21:28
 */
@Data
public class TotalViewVo {
    private Long totalNum;
    private Long inventoryNum;
    private StatusNum statusNum;
    private Map<Integer,StatusNum> floorStatusList;
}
