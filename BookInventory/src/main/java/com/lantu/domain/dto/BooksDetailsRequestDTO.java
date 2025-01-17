package com.lantu.domain.dto;

import lombok.Data;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2024/12/17 18:57
 */
@Data
public class BooksDetailsRequestDTO {
    private Integer floor;
    private String shelf;
    private Integer row;
    private Integer col;
}
