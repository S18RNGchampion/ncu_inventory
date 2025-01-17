package com.lantu;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lantu.domain.po.Bookinfo;
import com.lantu.domain.po.Newbarcode;
import com.lantu.domain.po.User;
import com.lantu.enums.InventoryStatusEnum;
import com.lantu.mapper.BookinfoMapper;
import com.lantu.mapper.NewbarcodeMapper;
import com.lantu.mapper.UserMapper;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private NewbarcodeMapper newbarcodeMapper;

    @Test
    public void test1(){


        LambdaQueryWrapper<Newbarcode> matchWrapper = Wrappers.lambdaQuery();
        LambdaQueryWrapper<Newbarcode> notMatchWrapper = Wrappers.lambdaQuery();
        LambdaQueryWrapper<Newbarcode> fixedMatchWrapper = Wrappers.lambdaQuery();
        LambdaQueryWrapper<Newbarcode> errorWrapper = Wrappers.lambdaQuery();
        errorWrapper.eq(Newbarcode::getStatus, InventoryStatusEnum.errorStatus);
        matchWrapper.eq(Newbarcode::getStatus, InventoryStatusEnum.matchStatus);
        notMatchWrapper.eq(Newbarcode::getStatus, InventoryStatusEnum.notMatchStatus);
        fixedMatchWrapper.eq(Newbarcode::getStatus, InventoryStatusEnum.fixedMatchStatus);
        Long errorCount = newbarcodeMapper.selectCount(errorWrapper);
        Long notMatchCount = newbarcodeMapper.selectCount(notMatchWrapper);
        Long fixedMatchCount = newbarcodeMapper.selectCount(fixedMatchWrapper);
        Long matchCount = newbarcodeMapper.selectCount(matchWrapper);

        System.out.println(l);
    }




}
