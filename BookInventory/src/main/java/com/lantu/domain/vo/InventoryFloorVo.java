package com.lantu.domain.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryFloorVo {

    private String shelfNum;
    private StatusNum statusNum;

}
