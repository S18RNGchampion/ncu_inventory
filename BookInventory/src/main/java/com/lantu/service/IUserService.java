package com.lantu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lantu.domain.po.User;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2024-05-15
 */
public interface IUserService extends IService<User> {

    Map<String, Object> login(User user);

    Map<String, Object> getUserInfo(String token);

    void logout(String token);
}
