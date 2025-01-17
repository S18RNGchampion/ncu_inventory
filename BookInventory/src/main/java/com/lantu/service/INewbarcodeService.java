package com.lantu.service;

import com.lantu.domain.po.Newbarcode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lantu.domain.vo.*;

import java.util.List;

/**
 * <p>
 *  服务类
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

    StatusNum getTotalStatusNum();

    List<InventoryFloorVo> inventoryByFloor(Integer floorNum);
}
