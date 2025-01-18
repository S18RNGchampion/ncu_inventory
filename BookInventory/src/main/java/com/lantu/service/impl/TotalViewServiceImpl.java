package com.lantu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.lantu.common.vo.Result;
import com.lantu.domain.po.Bookinfo;
import com.lantu.domain.po.Newbarcode;
import com.lantu.domain.vo.TotalViewVo;
import com.lantu.mapper.BookinfoMapper;
import com.lantu.mapper.NewbarcodeMapper;
import com.lantu.service.INewbarcodeService;
import com.lantu.service.ITotalViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author XuShiPing
 * @since 2025/1/18  12:17
 */
@Service
public class TotalViewServiceImpl implements ITotalViewService {

    @Autowired
    private NewbarcodeMapper newbarcodeMapper;

    @Autowired
    private BookinfoMapper bookinfoMapper;

    @Autowired
    private INewbarcodeService iNewbarcodeService;

    @Override
    public Result<TotalViewVo> getTotalView() {
        TotalViewVo totalViewVo = new TotalViewVo();
        LambdaQueryWrapper<Bookinfo> bookinfoLambdaQueryWrapper = Wrappers.lambdaQuery();
        LambdaQueryWrapper<Newbarcode> newbarcodeLambdaQueryWrapper = Wrappers.lambdaQuery();
        newbarcodeLambdaQueryWrapper.isNotNull(Newbarcode::getStatus);

        LambdaQueryWrapper<Newbarcode> inventoryNumLambdaQueryWrapper = Wrappers.lambdaQuery();
        inventoryNumLambdaQueryWrapper.isNull(Newbarcode::getStatus);
        totalViewVo.setTotalNum(bookinfoMapper.selectCount(bookinfoLambdaQueryWrapper));
        totalViewVo.setFloorStatusList(iNewbarcodeService.statisticFloorStatus());
        totalViewVo.setInventoryNum(newbarcodeMapper.selectCount(newbarcodeLambdaQueryWrapper));
        totalViewVo.setStatusNum(iNewbarcodeService.getTotalStatusNum());
        return Result.success(totalViewVo);
    }
}
