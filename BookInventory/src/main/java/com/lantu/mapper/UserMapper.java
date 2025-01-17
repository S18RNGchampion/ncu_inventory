package com.lantu.mapper;

import com.lantu.domain.po.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2024-05-15
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
