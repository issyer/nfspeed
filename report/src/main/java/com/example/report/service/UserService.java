package com.example.report.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.report.entity.User;

import java.util.List;

/**
 * @Author sunwanghe
 * @Date 2022/02/06
 */
public interface UserService extends IService<User> {

    List<User> selectUserList();
}
