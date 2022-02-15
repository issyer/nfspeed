package com.example.report.feign;

import com.example.commonutil.util.request.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author sunwanghe
 * @Date 2022/02/13
 */
@FeignClient(value = "admin")
public interface AdminFeign {

    @GetMapping("/user/currentUser")
    R getUserList();
}
