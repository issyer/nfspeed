package com.example.auth.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class UserVo {
    private Long id;
    private String username;
    private String password;
    private Integer status;
    private List<String> roles;
}
