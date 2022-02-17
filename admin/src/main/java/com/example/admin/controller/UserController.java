package com.example.admin.controller;



import com.example.admin.service.UserService;
import com.example.commonutil.util.request.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/currentUser")
    public R currentUser() {
        Map<String,String> map = new HashMap<>();
        return R.ok(map);
    }

    @PostMapping("/saveUser")
    public R saveUser(){
        userService.saveUser();
        return R.ok();
    }

}
