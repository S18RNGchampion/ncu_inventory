package com.lantu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lantu.common.vo.Result;
import com.lantu.domain.dto.BookInfoDTO;
import com.lantu.domain.vo.BookInfoVo;
import com.lantu.mapper.BookinfoMapper;
import com.lantu.service.IBookinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-05-15
 */
@RestController
@RequestMapping("/bookinfo")
public class BookinfoController {
    @Autowired
    private IBookinfoService bookinfoService;
    
    @GetMapping("/list")
    public Result getBookInfoList(@RequestParam(value = "name" , required = false) String name,
                                  @RequestParam(value = "newbarcode",required = false) String newbarcode,
                                  @RequestParam(value = "pageNo") Long pageNo,
                                  @RequestParam(value = "pageSize") Long pageSize){
        BookInfoDTO bookInfoDTO = new BookInfoDTO();
        bookInfoDTO.setName(name);
        bookInfoDTO.setNewbarcode(newbarcode);
        bookInfoDTO.setPageNo(pageNo);
        bookInfoDTO.setPageSize(pageSize);
        return  bookinfoService.getBookInfoByNameBarcode(bookInfoDTO);
        
    }
    
    
    @PostMapping("/load")
    public Result loadTxtAndGetBookinfo(@RequestPart MultipartFile file,
                                        @RequestParam(value = "pageNo") Long pageNo,
                                        @RequestParam(value = "pageSize") Long pageSize){
        
     
        return bookinfoService.loadTxtAndGetBookinfo(file , pageNo , pageSize);
    }
    

}
