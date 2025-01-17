package com.lantu.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2024/5/15 23:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookInfoDTO {
    private String name;
    
    private String newbarcode;
    
    private Long pageNo;
    
    private Long pageSize;
}
