package com.lantu.controller;

import com.lantu.common.vo.Result;
import com.lantu.domain.vo.*;
import com.lantu.service.IBookinfoService;
import com.lantu.service.INewbarcodeService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final IBookinfoService bookInfoService;
    private final INewbarcodeService newBarcodeService;

    @GetMapping("/floor")
    public Result inventoryByFloor(@RequestParam Integer floorNum) {
        List<InventoryFloorVo> list = newBarcodeService.inventoryByFloor(floorNum);
        return Result.success(list);
    }

    /**
     * 获得所有存在的楼层
     * @return
     */
    @GetMapping("/floors")
    public Result getFloors() {
        List<Integer> list = newBarcodeService.getFloors();
        return Result.success(list);
    }

    /**
     * 按书框查看条形码的识别状态
     * @param floorNum
     * @param shelfNum
     * @param rowNum
     * @param colNum
     * @return
     */
    @GetMapping("/bookFrame")
    public Result inventoryByBookFrame(@RequestParam Integer floorNum,
                                       @RequestParam String shelfNum,
                                       @RequestParam Integer rowNum,
                                       @RequestParam Integer colNum) {
        List<BooksVo> list = newBarcodeService.inventoryByBookFrame(floorNum, shelfNum, rowNum, colNum);
        return Result.success(list);
    }

    /**
     * 获取详细的书籍信息
     * @param newBarcodeId
     * @return
     */
    @GetMapping("/bookDetailInfo")
    public Result getBookDetailByNewBarcode(@RequestParam Integer newBarcodeId) {
        BookInfoResp bookInfoResp = newBarcodeService.getBookDetailByNewBarcode(newBarcodeId);
        return Result.success(bookInfoResp);
    }


    /**
     * 获取该楼层所有书架
     * @param floorNum
     * @return
     */
    @GetMapping("/getShelvesList")
    public Result<List<String>> getShelvesList(@RequestParam Integer floorNum) {
        return newBarcodeService.getShelvesList(floorNum);
    }

    /**
     * 书架盘点接口
     * @param floorNum
     * @param shelfNum
     * @return
     */
    @GetMapping("/shelf")
    public Result<List<FloorShelfStatusVo>> inventoryByShelf(@RequestParam Integer floorNum, @RequestParam String shelfNum) {
        return newBarcodeService.getFloorShelfInventoryStatus(floorNum,shelfNum);
    }

}
