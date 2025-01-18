package com.lantu.service;

import com.lantu.domain.po.Newbarcode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lantu.domain.vo.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author author
 * @since 2024-05-15
 */
public interface INewbarcodeService extends IService<Newbarcode> {

    void processFileContent(List<String> lines);

    SummaryInfoVo getSummaryBookData();

    SheltInfoVo getShelvesData(int floorname);

    List<FrameBooksVo> getFrameBooksData(int floorName, String shelfName);


    List<String> getBooksDetailsData(int floorName, String shelfName, int row, int col);

    List<InventoryFloorVo> inventoryByFloor(Integer floorNum);

    /**
     * 统计总共盘点图书数量
     * @return
     */
    StatusNum getTotalStatusNum();


    /**
     * 统计每个楼层盘点书不同状态的数量
     */
    Map<Integer, StatusNum> statisticFloorStatus();
}
