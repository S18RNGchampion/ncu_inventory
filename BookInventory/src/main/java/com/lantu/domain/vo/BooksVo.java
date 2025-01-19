package com.lantu.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class BooksVo {
    private Integer id;

    private String newbarcode;

    private Integer status;

    private Date createdtime;

    private int floorname;

    private String shelf;

    private int rownum;

    private int colnum;

    private String name;
}
