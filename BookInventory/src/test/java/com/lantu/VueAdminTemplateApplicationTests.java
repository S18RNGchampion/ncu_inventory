package com.lantu;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lantu.domain.po.Bookinfo;
import com.lantu.domain.po.User;
import com.lantu.mapper.BookinfoMapper;
import com.lantu.mapper.UserMapper;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VueAdminTemplateApplicationTests {

    @Resource
    private BookinfoMapper bookinfoMapper;
    
    @Test
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
        }
    }
    
    
    
    
}
