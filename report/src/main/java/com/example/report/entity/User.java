package com.example.report.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author sunwanghe
 * @Date 2022/02/06
 */
@Data
@TableName("user")
public class User {
    
    @TableId
    private Integer id;

//    @TableField("userNo")
    private String userNo;
    private String userName;
    private String password;
}
