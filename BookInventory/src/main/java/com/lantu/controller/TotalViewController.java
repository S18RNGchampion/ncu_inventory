package com.lantu.controller;

import com.lantu.common.vo.Result;
import com.lantu.domain.vo.TotalViewVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author XuShiPing
 * @since 2025/1/17  22:08
 */

@RestController
public class TotalViewController {

    @GetMapping("/totalView")
    public Result<TotalViewVo> totalView(){
        return null;
    }
}
