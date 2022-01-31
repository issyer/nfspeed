package com.example.report.controller;

import com.example.commonutil.util.request.R;
import com.example.report.entity.Car;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/car")
public class CarController {

    @RequestMapping("/list")
    public R searchData(){
        return R.ok();
    }


    @PostMapping("/save")
    public R saveCar(@RequestBody Car car){
        return R.ok(car);
    }
}
