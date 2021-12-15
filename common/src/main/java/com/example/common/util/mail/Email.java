package com.example.common.util.mail;

import lombok.Data;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 邮件实体
 * @author Kayll
 * @date 2018/12/21 15:14
 * 邮件对象
 */
@Data
public class Email {

    /**
     * 初始化邮件
     *
     * @param from     发件人地址
     * @param to       收件人地址
     * @param subject  邮件主题
     * @param body     邮件内容
     * @param textType 邮件类型
     * @param picture    内嵌图片
     */
    public Email(EmailAccount from, List<EmailAccount> to, String subject, String body, List<File> picture, String textType) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.textType = textType;
        this.picture = picture;
    }

    /**
     * 初始化邮件
     * @param from 发件人
     * @param to 收件人
     * @param subject 主题
     */
    public Email(EmailAccount from, List<EmailAccount> to, String subject) {
        this.from = from;
        this.to = to;
        this.subject = subject;
    }

    /**
     * 邮件id
     */
    private String id;

    /**
     * 发件人
     */
    private EmailAccount from;

    /**
     * 收件人、抄送人、密送人
     */
    private List<EmailAccount> to, cc, bcc;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String body;

    /**
     * 邮件文本类型，html/plain
     */
    private String textType;

    /**
     * 邮件内嵌图片
     */
    private List<File> picture;

    /**
     * 附件
     */
    private List<File> files;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 邮件优先级
     */
    private int priority;


}
