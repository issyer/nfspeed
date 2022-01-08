package com.example.report.controller;

import com.example.commonutil.util.request.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/car")
public class CarController {

    @RequestMapping("/list")
    public R searchData(){
        return R.ok();
    }


}
