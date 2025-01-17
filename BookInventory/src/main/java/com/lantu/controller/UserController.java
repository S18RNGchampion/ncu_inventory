package com.lantu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lantu.common.vo.Result;
import com.lantu.domain.po.User;
import com.lantu.domain.vo.BookInfoVo;
import com.lantu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2024-05-15
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;
    
    @GetMapping("/all")
    public Result<List<User>> getAllUser(){
        List<User> userList = userService.list();
        return Result.success(userList , "查询成功");
    }
    
    @PostMapping("/login")
    public Result<Map<String , Object>> login(@RequestBody User user){
        Map<String , Object> data = userService.login(user);
        if (data!= null){
            return Result.success(data);
        }
        return Result.fail(20002 , "用户名或者密码错误");
    }
    
    @GetMapping("/info")
    public Result<Map<String , Object>> getUserInfo(@RequestParam("token") String token){
        // 根据token获取用户信息
        Map<String , Object> data = userService.getUserInfo(token);
        if (data != null){
            return Result.success(data);
        }
        return Result.fail(20003 , "登录信息无效 请重新登录");
        
    }
    
    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("X-Token") String token){
        userService.logout(token);
        return Result.success();
        
    }
    


}
