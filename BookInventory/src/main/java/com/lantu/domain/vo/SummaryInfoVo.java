package com.lantu.domain.vo;

import lombok.Data;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @description: 用于存储盘点总书籍数以及各楼层书籍数量的类
 * @date 2024/12/6 4:26
 */
@Data
public class SummaryInfoVo {
    
    private int totalBooks;  // 总书籍数
    private List<FloorBookInfo> floors;  // 楼层书籍信息
    
    // 构造函数
    public SummaryInfoVo(int totalBooks, List<FloorBookInfo> floors) {
        this.totalBooks = totalBooks;
        this.floors = floors;
    }
    
    @Data
    public static class FloorBookInfo {
        private int floorname;  // 楼层ID

        private int bookCount;  // 书籍数量
        
        // 构造函数
        public FloorBookInfo(int floorname,  int bookCount) {
            this.floorname = floorname;

            this.bookCount = bookCount;
        }
    }
}
