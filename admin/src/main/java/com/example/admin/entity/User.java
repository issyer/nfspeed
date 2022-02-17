package com.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

//    @TableField("userNo")
    private String userNo;

//    @Excel(name = "学生性别", replace = { "男生_1", "女生_2" })
    private String userName;

//    @Excel(name = "姓名",  width = 20)
    private String password;

}
