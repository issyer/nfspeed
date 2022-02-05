package com.example.report.controller;

import com.example.commonutil.util.kuaidiniao.KDNiao;
import com.example.commonutil.util.minio.MinIoFileUtil;
import com.example.commonutil.util.request.R;
import com.example.report.entity.Car;
import io.minio.messages.Bucket;
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
    private MinIoFileUtil minIoFileUtil;

    @Autowired
    private KDNiao kdNiao;


    @RequestMapping("/list")
    public R searchData() throws Exception {
        String result = kdNiao.getOrderTraces(null,"STO","773145780535205");
        return R.ok(result);
    }


    @PostMapping("/save")
    public R saveCar(@RequestBody Car car){
        return R.ok(car);
    }
}
