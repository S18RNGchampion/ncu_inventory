package com.lantu.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2024/12/8 4:06
 */
@Data
public class SheltInfoVo {
    
    private int floorname;  // 楼层编号
    private List<Shelf> shelves;  // 该楼层的书架列表
    
    @Data
    public static class Shelf {
        private String name;  // 书架名称
        private int bookCount;  // 书架上的书籍数量
    }
}
