package com.lantu.service;

import com.lantu.common.vo.Result;
import com.lantu.domain.vo.TotalViewVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author XuShiPing
 * @since 2025/1/17  22:08
 */


public interface ITotalViewService {

   Result<TotalViewVo> getTotalView();
}
