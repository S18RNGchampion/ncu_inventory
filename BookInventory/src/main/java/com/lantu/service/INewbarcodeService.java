package com.lantu.service;

import com.lantu.common.vo.Result;
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

    /**
     * 获得该层楼所有书架名
     * @param floorNum
     * @return
     */
    Result<List<String>> getShelvesList(Integer floorNum);

    /**
     * 获得该层楼 书架的 盘点状况
     * @param floorNum
     * @param floorName
     * @return
     */
    Result<List<FloorShelfStatusVo>> getFloorShelfInventoryStatus(Integer floorNum,String floorName);


    List<Integer> getFloors();

    /**
     * 按条形码从左到右的顺序排列书框的书籍信息
     * @param floorNum
     * @param shelfNum
     * @param rowNum
     * @param colNum
     * @return
     */
    List<BooksVo> inventoryByBookFrame(Integer floorNum, String shelfNum, Integer rowNum, Integer colNum);

    /**
     * 按NewBarcode返回对应的书籍详情信息
     * @param newBarcodeId
     * @return
     */
    BookInfoResp getBookDetailByNewBarcode(Integer newBarcodeId);
}
