package com.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.admin.entity.User;

import java.util.List;

/**
 * @Author sunwanghe
 * @Date 2022/02/06
 */
public interface UserService extends IService<User> {

    List<User> selectUserList();

    Boolean saveUser();
}
