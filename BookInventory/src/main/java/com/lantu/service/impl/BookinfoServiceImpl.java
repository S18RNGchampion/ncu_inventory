package com.lantu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lantu.common.vo.Result;
import com.lantu.domain.dto.BookInfoDTO;
import com.lantu.domain.po.Bookinfo;
import com.lantu.domain.po.BookinfoNewbarcode;
import com.lantu.domain.po.Newbarcode;
import com.lantu.domain.vo.BookInfoVo;
import com.lantu.mapper.BookinfoMapper;
import com.lantu.mapper.BookinfoNewbarcodeMapper;
import com.lantu.mapper.NewbarcodeMapper;
import com.lantu.service.IBookinfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2024-05-15
 */
@Service
public class BookinfoServiceImpl extends ServiceImpl<BookinfoMapper, Bookinfo> implements IBookinfoService {
    
    @Autowired
    private BookinfoMapper bookinfoMapper;
    
    @Autowired
    private NewbarcodeMapper newbarcodeMapper;
    
//    @Override
//    public Result getBookInfoByNameBarcode(BookInfoDTO bookInfoDTO) {
//        // 创建 LambdaQueryWrapper 实例
//        LambdaQueryWrapper<Newbarcode> queryWrapper = new LambdaQueryWrapper<>();
//        // 添加查询条件：status 字段为 null
//        queryWrapper.isNull(Newbarcode::getStatus);
//        // 执行查询并返回结果
//        List<Newbarcode> newbarcodeInfoList = newbarcodeMapper.selectList(queryWrapper);
//
//        List<String> newbarcodes = newbarcodeInfoList.stream()
//                .map(newbarcode -> newbarcode.getNewbarcode().trim())  // 去除前后空白字符，包括制表符
//                .collect(Collectors.toList());
//
//        if (!newbarcodeInfoList.isEmpty()) {
//            // 创建 LambdaUpdateWrapper 实例
//            LambdaUpdateWrapper<Newbarcode> updateWrapper = new LambdaUpdateWrapper<>();
//            // 添加更新条件：newbarcode 在 barcodesToUpdate 列表中
//            updateWrapper.in(Newbarcode::getNewbarcode, newbarcodes);
//            // 设置更新的值：status 字段为 1
//            Newbarcode updateNewbarcode = new Newbarcode();
//            updateNewbarcode.setStatus(1);
//            // 执行批量更新
//            newbarcodeMapper.update(updateNewbarcode, updateWrapper);
//        }
//        List<BookInfoVo> bookInfoVoList=null ;
//        if (!newbarcodes.isEmpty()){
//            bookInfoVoList = bookinfoMapper.getTxtBookInfo(newbarcodes , (bookInfoDTO.getPageNo() - 1) * bookInfoDTO.getPageSize() ,bookInfoDTO.getPageSize());
//        }
//
//        Page<BookInfoVo> page1 = new Page<>(bookInfoDTO.getPageNo(), bookInfoDTO.getPageSize());
//        page1.setRecords(bookInfoVoList);
//        page1.setTotal(newbarcodes.size());
//
//        return Result.success(page1);
//    }
    @Override
    public Result getBookInfoByNameBarcode(BookInfoDTO bookInfoDTO) {
        // 1. 查询出数据库中 createdtime 的最大值（即最后一次的时间）
        Date latestTime = newbarcodeMapper.selectMaxCreatedTime();
        
        // 2. 如果没有记录，直接返回空结果
        if (latestTime == null) {
            return Result.success(new Page<>());
        }
        
        // 3. 创建 LambdaQueryWrapper 实例，查找 createdtime 等于最大时间的所有记录
        LambdaQueryWrapper<Newbarcode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Newbarcode::getCreatedtime, latestTime);  // 查找最新时间的所有记录
        
        // 执行查询并返回结果（根据分页查询）
        Page<Newbarcode> page = new Page<>(bookInfoDTO.getPageNo(), bookInfoDTO.getPageSize());
        List<Newbarcode> newbarcodeInfoList = newbarcodeMapper.selectPage(page, queryWrapper).getRecords();
        
        // 提取 newbarcode 列表
        List<String> newbarcodes = newbarcodeInfoList.stream()
                .map(newbarcode -> newbarcode.getNewbarcode().trim())  // 去除前后空白字符，包括制表符
                .collect(Collectors.toList());
        
        List<BookInfoVo> bookInfoVoList = null;
        if (!newbarcodes.isEmpty()) {
            // 根据 newbarcodes 查询 bookinfo
            bookInfoVoList = bookinfoMapper.getTxtBookInfo(newbarcodes,
                    (bookInfoDTO.getPageNo() - 1) * bookInfoDTO.getPageSize(),
                    bookInfoDTO.getPageSize());
        }
        
        // 返回分页数据
        Page<BookInfoVo> page1 = new Page<>(bookInfoDTO.getPageNo(), bookInfoDTO.getPageSize());
        page1.setRecords(bookInfoVoList);
        page1.setTotal(newbarcodes.size());
        
        return Result.success(page1);
    }
    
    
    
    @Override
    public Result loadTxtAndGetBookinfo(MultipartFile file , Long pageNo , Long pageSize) {
        //获取文件初始名称
        String originalFilename = file.getOriginalFilename();
        String name = UUID.randomUUID().toString();
        String type = originalFilename.substring(originalFilename.lastIndexOf("."));
        File dest = new File("E:\\file" + "\\" + name + type);
        if (!dest.exists()) {
            dest.mkdir();
        }
        try {
            file.transferTo(dest.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将条形码插入数据库
        List<String> newbarcodeList = addnewbarcode(dest.getAbsoluteFile().toString());
        List<BookInfoVo> bookInfoVoList = new ArrayList<>();
        if (!newbarcodeList.isEmpty()){
            //把插入的数据查询出来
            bookInfoVoList = bookinfoMapper.getTxtBookInfo(newbarcodeList , (pageNo - 1) * pageSize ,pageSize);
        }
        //用page封装数据
        Page<BookInfoVo> page = new Page<>(pageNo, pageSize);
        page.setRecords(bookInfoVoList);
        page.setTotal(newbarcodeList.size());
        
        return Result.success(page);
    }
    
    
    List<String> addnewbarcode(String path) {
        
        String filePath = path; // 请替换为你的txt文件路径
        Map<String, String> dataMap = new HashMap<>();
        List<String> newbarcodeList = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    dataMap.put(parts[1], parts[0]);
                    newbarcodeList.add(parts[0]);
                }
                if (parts.length == 1){
                    newbarcodeList.add(parts[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // 输出Map中的内容
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
            LambdaUpdateWrapper<Bookinfo> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Bookinfo::getBarcode, entry.getKey()).set(Bookinfo::getNewbarcode, entry.getValue());
            bookinfoMapper.update(null, wrapper);
        }
        return newbarcodeList;
    }
    
}

