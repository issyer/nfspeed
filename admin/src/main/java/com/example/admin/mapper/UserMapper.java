package com.example.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.admin.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author sunwanghe
 * @Date 2022/02/06
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    List<User> selectUserList(@Param("user") User user);

}
