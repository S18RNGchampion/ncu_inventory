package com.lantu.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author XuShiPing
 * @since 2025/1/17  21:44
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusNum {
    private Long matchStatusNum = 0L;
    private Long notMatchStatusNum = 0L;
    private Long fixedMatchStatusNum = 0L;
    private Long errorStatusNum = 0L;


}
