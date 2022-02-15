package com.example.report.controller;

import com.example.commonutil.util.request.R;
import com.example.report.entity.Car;
import com.example.report.entity.User;
import com.example.report.feign.AdminFeign;
import com.example.report.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/car")
public class CarController {

   @Autowired
   private UserService userService;

   @Autowired
   private AdminFeign authFeign;

    @RequestMapping("/list")
    public R searchData() throws Exception {
        List<User> userList = userService.selectUserList();
        return authFeign.getUserList();
    }


    @PostMapping("/save")
    public R saveCar(@RequestBody Car car){
        return R.ok(car);
    }
}
