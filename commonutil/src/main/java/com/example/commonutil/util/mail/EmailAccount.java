package com.example.commonutil.util.mail;

import lombok.Data;

import javax.mail.internet.InternetAddress;

/**
 * 邮件账户类
 * @author Kayll
 * @date 2018/12/21 17:09
 */
@Data
public class EmailAccount {

    /**
     * 初始化账户
     * @param address 账户地址
     */
    public EmailAccount(InternetAddress address) {
        this.address = address;
    }

    /**
     * 初始化账户
     * @param address 账户地址
     * @param userName 用户名
     */
    public EmailAccount(InternetAddress address, String userName) {
        this.address = address;
        this.userName = userName;
    }

    /**
     * 邮件账户地址
     */
    private InternetAddress address;

    /**
     * 邮件账户用户名
     */
    private String userName;
}
