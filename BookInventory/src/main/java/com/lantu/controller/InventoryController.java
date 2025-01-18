package com.lantu.controller;

import com.lantu.common.vo.Result;
import com.lantu.domain.vo.InventoryFloorVo;
import com.lantu.domain.vo.SheltInfoVo;
import com.lantu.service.IBookinfoService;
import com.lantu.service.INewbarcodeService;
import lombok.RequiredArgsConstructor;
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
        List<InventoryFloorVo> list =  newBarcodeService.inventoryByFloor(floorNum);
        return Result.success(list);
    }

}
