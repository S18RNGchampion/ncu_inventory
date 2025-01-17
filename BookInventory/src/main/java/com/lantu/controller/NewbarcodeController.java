package com.lantu.controller;


import com.lantu.common.vo.Result;
import com.lantu.domain.dto.BooksDetailsRequestDTO;
import com.lantu.domain.vo.FrameBooksVo;
import com.lantu.domain.vo.SheltInfoVo;
import com.lantu.domain.vo.SummaryInfoVo;
import com.lantu.service.impl.NewbarcodeServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-05-15
 */
@RestController
@RequestMapping("/newbarcode")
public class NewbarcodeController {
    
    private final NewbarcodeServiceImpl newbarcodeService;
    
    public NewbarcodeController(NewbarcodeServiceImpl newbarcodeService) {
        this.newbarcodeService = newbarcodeService;
    }
    
    @GetMapping("/summary")
    public Result fetchSummaryBookData() {
        try {
            // 调用服务层方法获取书籍盘点数据
            SummaryInfoVo summaryInfo = newbarcodeService.getSummaryBookData();
    
            // 返回成功结果
            return Result.success(summaryInfo);
        } catch (Exception e) {
            // 返回失败响应
            return Result.fail("获取书籍盘点数据失败");
        }
    }
    
    /**
     * 根据楼层编号获取书架数据
     * @param  楼层编号（整数类型）
     * @return 返回楼层的书架数据
     */
    @PostMapping("/floor")
    public Result fetchShelvesData(@RequestBody Map<String, Integer> requestBody) {
        try {
            int floorname = requestBody.get("floorname"); // 从请求体中解析 floorname
            SheltInfoVo sheltInfoVo = newbarcodeService.getShelvesData(floorname);
            return Result.success(sheltInfoVo);
        } catch (Exception e) {
            return Result.fail("获取书架数据失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/framebooks")
    public Result fetchFrameBooksData(@RequestBody Map<String, Object> requestBody) {
        try {
            // 从请求体中解析 floorName 和 shelfName
            int floorName = (int) requestBody.get("floorName");
            String shelfName = (String) requestBody.get("shelfName");
    
            // 打印参数，确保接收到的参数正确
            System.out.println("Received floorName: " + floorName + ", shelfName: " + shelfName);
    
    
            // 参数验证
            if (floorName <= 0 || shelfName == null || shelfName.isEmpty()) {
                return Result.fail("Invalid input parameters: floorName or shelfName");
            }
            
            // 调用服务层方法获取书框书籍数据
            List<FrameBooksVo> frameBooksData = newbarcodeService.getFrameBooksData(floorName, shelfName);
            
            // 返回成功结果
            return Result.success(frameBooksData);
        } catch (Exception e) {
            // 返回失败响应
            return Result.fail("获取书框书籍数据失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取书框书籍数据
     */
    @PostMapping("/booksDetails")
    public Result fetchBooksDetailsData(@RequestBody BooksDetailsRequestDTO requestDTO) {
        try {
            // 获取请求参数
            int floorName = requestDTO.getFloor();  // floor 是 int 类型
            String shelfName = requestDTO.getShelf();  // shelf 是 String 类型
            int row = requestDTO.getRow();  // row 是 int 类型
            int col = requestDTO.getCol();  // col 是 int 类型
        
            // 打印接收到的参数（调试用）
            System.out.println("Received floorName: " + floorName + ", shelfName: " + shelfName + ", row: " + row + ", col: " + col);
        
            // 参数验证
            if (floorName <= 0 || shelfName == null || shelfName.isEmpty()) {
                return Result.fail("Invalid input parameters: floorName or shelfName cannot be null or empty");
            }
        
            // 调用服务层方法获取书框书籍数据
            List<String> bookNames = newbarcodeService.getBooksDetailsData(floorName, shelfName, row, col);
        
            // 返回成功结果
            return Result.success(bookNames);
        } catch (Exception e) {
            // 返回失败响应
            return Result.fail("获取书框书籍数据失败: " + e.getMessage());
        }
    }
    
    
    
    
    
    
    
    
}