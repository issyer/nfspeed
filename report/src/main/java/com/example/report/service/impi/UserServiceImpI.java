package com.example.report.service.impi;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.report.entity.User;
import com.example.report.mapper.UserMapper;
import com.example.report.service.UserService;
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
        return baseMapper.selectUserList();
    }
}
