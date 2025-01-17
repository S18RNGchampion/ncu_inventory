package com.lantu.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2024/5/15 17:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookInfoVo {
    
    private String name;
    
    private String author;
    
    private String address;
    
    private String callNumber;
    
    private String bookStatus;
    
    private String newbarcode;
}
