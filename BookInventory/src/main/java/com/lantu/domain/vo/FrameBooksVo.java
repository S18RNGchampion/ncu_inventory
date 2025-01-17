package com.lantu.domain.vo;

import lombok.Data;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2024/12/14 12:20
 */
@Data
public class FrameBooksVo {
    
    private int row;        // 行号
    private int col;        // 列号
    private int bookCount;  // 书籍数量
    
    // Lombok 会自动生成构造方法、getter、setter、toString、equals 和 hashCode 方法
}