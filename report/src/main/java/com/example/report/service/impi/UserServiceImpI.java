package com.example.report.service.impi;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.report.entity.User;
import com.example.report.feign.AdminFeign;
import com.example.report.mapper.UserMapper;
import com.example.report.service.UserService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author sunwanghe
 * @Date 2022/02/06
 */
@Service
public class UserServiceImpI extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private AdminFeign adminFeign;

    @Override
    public List<User> selectUserList() {
        User user = new User();
        return baseMapper.selectUserList(user);
    }

    @Override
    @GlobalTransactional
    public Boolean saveUser() {
        User user = new User();
        user.setUserNo("000002");
        user.setUserName("哈登");

        adminFeign.saveUser();

        String data = null;
        System.out.println(data.length());
        this.save(user);
        return true;
    }
}
