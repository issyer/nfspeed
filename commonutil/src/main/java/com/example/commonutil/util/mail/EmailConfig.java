package com.example.commonutil.util.mail;

import lombok.Data;

import java.io.InputStream;
import java.util.Properties;

//import org.apache.log4j.Logger;

/**
 * 邮件配置类，具体配置信息请额外写在一个email.properties文件中，并将该文件放在resources下的properties文件夹中
 * @author Kayll
 * @date 2018/12/25 15:25
 * 邮件配置信息
 */
@Data
public class EmailConfig {

//    private  final Logger logger = Logger.getLogger(EmailConfig.class);

    private String MAIL_DEBUT = "mail.debug";
    private   String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private   String MAIL_SMTP_PORT = "mail.smtp.port";
    private   String MAIL_HOST = "mail.host";
    private   String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
    private   String MAIL_USER = "mail.user";
    private   String MAIL_PASS = "mail.pass";
    private   String MAIL_FROM = "mail.from";

    private   String MAIL_RECEIVE_HOST = "mail.imap.host";
    private   String MAIL_RECEIVE_AUTH = "mail.imap.auth";
    private   String MAIL_RECEIVE_PORT = "mail.imap.port";
    private   String MAIL_STORE_PROTOCOL = "mail.store.protocol";
    private   String MAIL_RECEIVE_SOCKETFACTORY_CLASS = "mail.imap.socketFactory.class";
    private   String MAIL_SEND_SOCKETFACTORY_CLASS = "mail.smtp.socketFactory.class";

    private String MAIL_FILE_SAVEPATH = "mail.file.savePath";

    private String MAIL_FOLDER = "mail.folder";

    private String folder;

    private  String savepath;
    //是否开启debug调试
    private  String debug;

    //发送服务器是否需要身份验证
    private  String auth;

    //发送邮件端口
    private  String port;

    //邮件服务器主机名
    private  String host;

    //发送邮件协议名称
    private  String protocol;

    //发送邮件用户名
    private  String user;

    //发送邮件邮箱密码
    private  String pass;

    //发送邮件发件人
    private  String from;

    //接收邮件主机名
    private  String receive_host;

    //接收邮件是否需要身份验证
    private  String receive_auth;

    //接收邮件端口号
    private  String receive_port;

    //邮件接收协议名
    private  String store_protocol;

    //邮件接收的ssl验证
    private  String receive_ssl;

    //邮件发送的ssl验证
    private  String send_ssl;

    //创建单例发送Session配置信息
    private  Properties sendSessionProperties = new Properties();

    //创建单例接收Session配置信息
    private  Properties receiveSessionProperties = new Properties();

    //创建单例邮箱配置信息
    public static EmailConfig instance;

    private EmailConfig() {
        try {

            InputStream fis = EmailConfig.class.getResourceAsStream("/properties/email.properties");
            Properties prop = new Properties();
            prop.load(fis);
            auth = prop.getProperty(MAIL_SMTP_AUTH).trim();
            port = prop.getProperty(MAIL_SMTP_PORT).trim();
            debug = prop.getProperty(MAIL_DEBUT).trim();
            from = prop.getProperty(MAIL_FROM).trim();
            host = prop.getProperty(MAIL_HOST).trim();
            pass = prop.getProperty(MAIL_PASS).trim();
            protocol = prop.getProperty(MAIL_TRANSPORT_PROTOCOL).trim();
            user = prop.getProperty(MAIL_USER).trim();
            receive_host = prop.getProperty(MAIL_RECEIVE_HOST).trim();
            receive_auth = prop.getProperty(MAIL_RECEIVE_AUTH).trim();
            receive_port = prop.getProperty(MAIL_RECEIVE_PORT).trim();
            store_protocol = prop.getProperty(MAIL_STORE_PROTOCOL).trim();
            receive_ssl = prop.getProperty(MAIL_RECEIVE_SOCKETFACTORY_CLASS).trim();
            send_ssl = prop.getProperty(MAIL_SEND_SOCKETFACTORY_CLASS).trim();
            savepath = prop.getProperty(MAIL_FILE_SAVEPATH).trim();
            folder = prop.getProperty(MAIL_FOLDER).trim();
            fis.close();

            sendSessionProperties.setProperty(MAIL_SMTP_AUTH, auth);
            sendSessionProperties.setProperty(MAIL_SMTP_PORT, port);
            sendSessionProperties.setProperty(MAIL_DEBUT, debug);
            sendSessionProperties.setProperty(MAIL_HOST, host);
            sendSessionProperties.setProperty(MAIL_TRANSPORT_PROTOCOL, protocol);
            sendSessionProperties.setProperty(MAIL_SEND_SOCKETFACTORY_CLASS, send_ssl);

            receiveSessionProperties.setProperty(MAIL_RECEIVE_SOCKETFACTORY_CLASS, receive_ssl);
            receiveSessionProperties.setProperty(MAIL_STORE_PROTOCOL, store_protocol);
            receiveSessionProperties.setProperty(MAIL_RECEIVE_PORT, receive_port);
            receiveSessionProperties.setProperty(MAIL_RECEIVE_AUTH, receive_auth);
            receiveSessionProperties.setProperty(MAIL_RECEIVE_HOST, receive_host);
            receiveSessionProperties.setProperty(MAIL_DEBUT, debug);
        } catch (Exception e) {
//            logger.error("邮箱配置信息初始化异常", e);
        }
    }


    public static EmailConfig getInstance() {
        if (instance == null) {
            instance = new EmailConfig();
        }
        return instance;
    }

}
