package com.example.admin.controller;



import com.example.commonutil.util.request.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/currentUser")
    public R currentUser() {
        Map<String,String> map = new HashMap<>();
        return R.ok(map);
    }

}
