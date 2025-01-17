package com.lantu;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lantu.domain.po.Bookinfo;
import com.lantu.mapper.BookinfoMapper;
import com.lantu.mapper.UserMapper;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @description: TODO
 * @date 2024/8/20 9:22
 */
public class aaa {
    @Resource
    private UserMapper userMapper;
    @Resource
    private BookinfoMapper bookinfoMapper;
    
    void addnewbarcode(){
        
        String filePath = "D:\\Desktop\\match1.txt"; // 请替换为你的txt文件路径
        Map<String, String> dataMap = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    dataMap.put(parts[1], parts[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // 输出Map中的内容
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
            LambdaUpdateWrapper<Bookinfo> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Bookinfo::getBarcode , entry.getKey()).set(Bookinfo::getNewbarcode , entry.getValue());
            bookinfoMapper.update(null , wrapper);
//            LambdaQueryWrapper<Bookinfo> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(Bookinfo::getBarcode , entry.getKey());
//            Bookinfo bookinfo = bookinfoMapper.selectOne(queryWrapper);
//            bookinfo.setNewbarcode(entry.getValue());
////            LambdaUpdateWrapper<Bookinfo> wrapper = new LambdaUpdateWrapper<>();
////            wrapper.eq(Bookinfo::getBarcode , bookinfo.getId());
//            bookinfoMapper.updateById(bookinfo);
        }
        
        
    }
    
}
