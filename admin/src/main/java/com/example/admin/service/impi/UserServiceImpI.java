package com.example.admin.service.impi;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.entity.User;
import com.example.admin.mapper.UserMapper;
import com.example.admin.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author sunwanghe
 * @Date 2022/02/06
 */
@Service
public class UserServiceImpI extends ServiceImpl<UserMapper, User> implements UserService {


    @Override
    public List<User> selectUserList() {
        User user = new User();
        return baseMapper.selectUserList(user);
    }

    @Override
    public Boolean saveUser() {
        User user = new User();
        user.setUserNo("000003");
        user.setUserName("杜兰特");

        this.save(user);
        return true;
    }
}
