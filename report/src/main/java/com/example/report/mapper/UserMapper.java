package com.example.report.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.report.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author sunwanghe
 * @Date 2022/02/06
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    List<User> selectUserList();

}
