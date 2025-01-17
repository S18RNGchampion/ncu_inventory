package com.lantu;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.lantu.domain.po.Bookinfo;
import com.lantu.mapper.BookinfoMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReadTxtToMap {
   
    
    public static void main(String[] args) {
        
        
        
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
        
        }
    }
}
