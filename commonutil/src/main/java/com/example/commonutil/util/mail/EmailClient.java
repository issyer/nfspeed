package com.example.commonutil.util.mail;

import com.sun.mail.imap.IMAPFolder;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 邮件客户端，包含登陆、发送、接收方法
 * @author Kayll
 * @date 2018/12/21 17:04
 * 邮件客户端，用户可以调用登陆，发送和接收方法，
 */
public class EmailClient {

    public EmailClient() {
    }

    //TODO: change it
    public boolean login(String account, String password) {
        //throw new IllegalStateException("Not yet Implemented");
        if (account.equals("1234567")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 邮件发送
     *
     * @param email
     * @throws MessagingException
     */
    public void send(Email email) {
        Session session = Session.getInstance(EmailConfig.getInstance().getSendSessionProperties());
        MimeMessage mimeMessage = createEmail(session, email);
        Transport transport;
        try {
            transport = session.getTransport();
            transport.connect(EmailConfig.getInstance().getHost(), EmailConfig.getInstance().getUser(), EmailConfig.getInstance().getPass());
            if (mimeMessage.getRecipients(Message.RecipientType.TO) != null) {
                transport.sendMessage(mimeMessage, mimeMessage.getRecipients(Message.RecipientType.TO));
            }
            if (mimeMessage.getRecipients(Message.RecipientType.CC) != null) {
                transport.sendMessage(mimeMessage, mimeMessage.getRecipients(Message.RecipientType.CC));
            }
            if (mimeMessage.getRecipients(Message.RecipientType.BCC) != null) {
                transport.sendMessage(mimeMessage, mimeMessage.getRecipients(Message.RecipientType.BCC));
            }
            transport.close();
        }  catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 邮件接收，只接收未读邮件，每次收取前5封
     *
     * @return 返回未读邮件列表
     * @throws MessagingException
     * @throws IOException
     */
    public List<Email> receive() {
        Session session = Session.getDefaultInstance(EmailConfig.getInstance().getReceiveSessionProperties());
        try {
            Store store = session.getStore(EmailConfig.getInstance().getStore_protocol());
            store.connect(EmailConfig.getInstance().getReceive_host(), EmailConfig.getInstance().getUser(), EmailConfig.getInstance().getPass());
            IMAPFolder folder = (IMAPFolder) store.getFolder(EmailConfig.getInstance().getFolder());
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.getMessages(folder.getMessageCount() - folder.getUnreadMessageCount() + 1, folder.getMessageCount() - folder.getUnreadMessageCount() + 5);
            List<Email> emails;
            emails = parseMessage(folder, messages);  //邮件解析
            folder.close();
            store.close();
            return emails;
        }catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 邮件解析
     *
     * @param messages
     * @return 返回邮件列表
     * @throws MessagingException
     * @throws IOException
     */
    private List<Email> parseMessage(IMAPFolder folder, Message... messages) throws MessagingException, IOException {
        List<EmailAccount> to = new LinkedList<>();
        List<EmailAccount> cc = new LinkedList<>();
        List<EmailAccount> bcc = new LinkedList<>();
        List<Email> emails = new LinkedList<>();

//        String filePath = "C:\\Users\\Administrator\\Desktop\\attachments\\";
        //String filePath = "/root/filesave/";
        String filePath =EmailConfig.getInstance().getSavepath();
        if (messages == null || messages.length < 1)
            System.out.println("暂无未收取的邮件！！！");
        for (Message message : messages) {
            MimeMessage msg = (MimeMessage) message;
            msg.setFlag(Flags.Flag.SEEN, true);
            Address[] addressTo = msg.getRecipients(Message.RecipientType.TO);
            Address[] addressCc = msg.getRecipients(Message.RecipientType.CC);
            Address[] addressBcc = msg.getRecipients(Message.RecipientType.BCC);
            if (addressTo != null) {
                for (Address anAddressTo : addressTo) {
                    to.add(new EmailAccount((InternetAddress) anAddressTo));
                }
            }

            if (addressCc != null) {
                for (Address anAddressCc : addressCc) {
                    cc.add(new EmailAccount((InternetAddress) anAddressCc));
                }
            }
            if (addressBcc != null) {
                for (Address anAddressBcc : addressBcc) {
                    bcc.add(new EmailAccount((InternetAddress) anAddressBcc));
                }
            }
            Email email = new Email(new EmailAccount((InternetAddress) message.getFrom()[0], ((InternetAddress) message.getFrom()[0]).getPersonal()), to, message.getSubject());
            email.setCc(cc);
            email.setBcc(bcc);
            email.setSendTime(message.getSentDate());
            email.setPriority(getPriority((MimeMessage) message));
            if (message.isMimeType("text/plain")) {
                email.setTextType("text/plain");
            } else if (message.isMimeType("text/html")) {
                email.setTextType("text/html");
            } else if (message.isMimeType("multipart/mixed")) {
                email.setTextType("multipart/mixed");
            } else if (message.isMimeType("multipart/related")) {
                email.setTextType("multipart/related");
            } else {
                email.setTextType("other");
            }
            email.setBody(mimeBody(message));
            String uid = String.valueOf(folder.getUID(msg));
            if (isContainAttachment(msg)) {
                email.setFiles(saveAttachment(msg, filePath, ((InternetAddress) message.getFrom()[0]).getAddress(), uid));
            }
            emails.add(email);
        }
        return emails;
    }


    /**
     * 获取邮件优先级
     * @param msg
     * @return 返回优先级，1：紧急，3：普通，5：低级
     * @throws MessagingException
     */
    private  int getPriority(MimeMessage msg) throws MessagingException {
        int priority = 3;
        String[] headers = msg.getHeader("X-Priority");
        if (headers != null) {
            String headerPriority = headers[0];
            if (headerPriority.contains("1") || headerPriority.contains("High"))
                priority = 1;
            else if (headerPriority.contains("5") || headerPriority.contains("Low"))
                priority = 5;
            else
                priority = 3;
        }
        return priority;
    }
    /**
     * 获取邮件主体
     *
     * @param mimeMessage
     * @return 返回邮件主体
     * @throws MessagingException
     * @throws IOException
     */
    private String mimeBody(Message mimeMessage) throws MessagingException, IOException {
        String body = "";
        //获取的过程其实也是一种解析的过程，跟解析附件不一样的是，它解析时候判断邮件体的MmeType类型再决定是否往下一层拆
        //如果邮件是纯文本或者html文本，解析步骤到下面这个if语句就结束了
        if (mimeMessage.isMimeType("text/plain") || mimeMessage.isMimeType("text/html")) {
            body = mimeMessage.getContent().toString();
        } else if (mimeMessage.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) mimeMessage.getContent();
            int bodyNum = mp.getCount();
            for (int j = 0; j < bodyNum; j++) {
                //如果是内嵌图片，不包含附件，解析到下面这里就结束
                if (mp.getBodyPart(j).isMimeType("text/html")) {
                    body = (String) mp.getBodyPart(j).getContent();
                } else if (mp.getBodyPart(j).isMimeType("multipart/*")) {
                    //如果包含附件，前面那都是在拆附件体，到这里拆文本这层
                    Multipart mp2 = (Multipart) mp.getBodyPart(j).getContent();
                    for (int h = 0; h < mp2.getCount(); h++) {
                        if (mp2.getBodyPart(h).isMimeType("text/html")) {
                            body = (String) mp2.getBodyPart(h).getContent();
                        }
                    }
                }
            }
        }
        return body;
    }

    /**
     * 判断收到的邮件中是否含有附件或者内嵌图片
     *
     * @param part
     * @return true:有，false:没有
     * @throws MessagingException
     * @throws IOException
     */
    private boolean isContainAttachment(Part part) throws MessagingException, IOException {
        boolean flag = false;
        if (part.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    flag = true;
                } else if (bodyPart.isMimeType("multipart/*")) {
                    flag = isContainAttachment(bodyPart);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.contains("application")) {
                        flag = true;
                    }
                    if (contentType.contains("name")) {
                        flag = true;
                    }

                }
                if (flag) break;
            }
        } else if (part.isMimeType("message/rfc822")) {
            flag = isContainAttachment((Part) part.getContent());
        }
        return flag;
    }

    /**
     * 保存附件
     *
     * @param part    //邮件体
     * @param destDir //初始文件目录
     * @param email   //邮件账号
     * @param emailId //邮件id
     * @return 返回文件列表
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @throws FileNotFoundException
     * @throws IOException
     */
    private List<File> saveAttachment(Part part, String destDir, String email, String emailId) throws UnsupportedEncodingException, MessagingException,
            FileNotFoundException, IOException {
        List<File> files = new LinkedList<>();
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();    //复杂体邮件
            //复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                //获得复杂体邮件中其中一个邮件体
                BodyPart bodyPart = multipart.getBodyPart(i);
                //某一个邮件体也有可能是由多个邮件体组成的复杂体
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    InputStream is = bodyPart.getDataHandler().getInputStream();
                    files.add(this.saveFile(is, destDir, decodeText(bodyPart.getFileName()), email, emailId));
                } else if (bodyPart.isMimeType("multipart/*")) {
                    saveAttachment(bodyPart, destDir, email, emailId);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.contains("name") || contentType.contains("application")) {
                        files.add(this.saveFile(bodyPart.getDataHandler().getInputStream(), destDir, decodeText(bodyPart.getFileName()), email, emailId));
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachment((Part) part.getContent(), destDir, email, emailId);
        }
        return files;
    }

    /**
     * 文件保存
     *
     * @param is       文件流
     * @param destDir  初始文件目录
     * @param fileName 文件名
     * @param email    邮件账号
     * @param emailId  邮件id
     * @return 返回保存的文件本身
     * @throws FileNotFoundException
     * @throws IOException
     */
    private File saveFile(InputStream is, String destDir, String fileName, String email, String emailId)
            throws FileNotFoundException, IOException {

        if (fileName.matches("[\\s\\\\/:\\*\\?\\\"<>\\|]")) {
            Pattern pattern = Pattern.compile("[\\s\\\\/:\\*\\?\\\"<>\\|]");
            Matcher matcher = pattern.matcher(fileName);
            fileName = matcher.replaceAll("_"); // 将匹配到的非法字符以下划线替换
        }

        File file = new File(destDir + email);
        if (!file.exists()) {
            file.mkdir();
        }
        File attachFile = new File(destDir + email + "/" + emailId);
        if (!attachFile.exists()) {
            attachFile.mkdir();
        }
        BufferedInputStream bis = new BufferedInputStream(is);
        File file1 = new File(destDir + "/" + email + "/" + emailId + "/" + fileName);
        FileOutputStream outputStream = new FileOutputStream(file1);
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);
        int len;
        while ((len = bis.read()) != -1) {
            bos.write(len);
            bos.flush();
        }

        bos.close();
        bis.close();
        return file1;
    }

    /**
     * 文本编辑
     *
     * @param encodeText
     * @return
     * @throws UnsupportedEncodingException
     */
    private String decodeText(String encodeText) throws UnsupportedEncodingException {
        if (encodeText == null || "".equals(encodeText)) {
            return "";
        } else {
            return MimeUtility.decodeText(encodeText);
        }
    }

    /**
     * 创建邮件
     *
     * @param session 上下文环境
     * @param email   邮件属性
     * @return 可以发送的邮件体
     */
    private MimeMessage createEmail(Session session, Email email) {
        MimeMessage msg = new MimeMessage(session);

        try {
            for (int i = 0; i < email.getTo().size(); i++) {
                msg.addRecipient(Message.RecipientType.TO, email.getTo().get(i).getAddress());
            }
            if (email.getCc() != null) {
                for (int i = 0; i < email.getCc().size(); i++) {
                    msg.addRecipient(Message.RecipientType.CC, email.getCc().get(i).getAddress());
                }
            }
            if (email.getBcc() != null) {
                for (int i = 0; i < email.getBcc().size(); i++) {
                    msg.addRecipient(Message.RecipientType.BCC, email.getBcc().get(i).getAddress());
                }
            }
            String nick = "";
            try {
                nick = MimeUtility.encodeText(email.getFrom().getUserName());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String address = email.getFrom().getAddress().getAddress();
            InternetAddress from = new InternetAddress(nick + " <" + address + ">");
            msg.setFrom(from);
            msg.setSubject(email.getSubject());

            //判断类型是否是普通文本，plain代表普通文本，里面没有图片
            if (email.getTextType().equals("plain")) {
                msg.setText(email.getBody());
            } else if (email.getTextType().equals("html")) { //html代表是html类型的邮件，里面可能含有图片
                if ((email.getPicture() == null || email.getPicture().size() == 0) && (email.getFiles() == null || email.getFiles().size() == 0)) {
                    msg.setContent(email.getBody(), "text/html;charset=gb2312");
                }
                if (email.getPicture() != null && email.getPicture().size() > 0 && (email.getFiles() == null || email.getFiles().size() == 0)) {
                    msg.setContent(createContent(email));
                }
                if (email.getFiles() != null && email.getFiles().size() > 0) {
                    MimeMultipart allMultipart = new MimeMultipart("mixed");
                    for (int i = 0; i < email.getFiles().size(); i++) {
                        allMultipart.addBodyPart(createAttachmentFile(email.getFiles().get(i)));
                    }
                    if (email.getPicture() != null && email.getPicture().size() > 0) {
                        MimeBodyPart contentPart = new MimeBodyPart();
                        MimeMultipart contentMultipart = new MimeMultipart("related");
                        MimeBodyPart htmlBodyPart = new MimeBodyPart();
                        htmlBodyPart.setContent(email.getBody(), "text/html;charset=gb2312");
                        contentMultipart.addBodyPart(htmlBodyPart);
                        for (int i = 0; i < email.getPicture().size(); i++) {
                            contentMultipart.addBodyPart(createPhoto(email.getPicture().get(i)));
                        }
                        contentPart.setContent(contentMultipart);
                        allMultipart.addBodyPart(contentPart);
                    }
                    msg.setContent(allMultipart);
                }

            }
            msg.saveChanges();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * 创建邮件html文本
     *
     * @param email
     * @return 邮件体
     */
    private MimeMultipart createContent(Email email) {
        MimeMultipart contentMultipart = new MimeMultipart("related");
        MimeBodyPart htmlBodyPart = new MimeBodyPart();
        try {
            htmlBodyPart.setContent(email.getBody(), "text/html;charset=gb2312");
            contentMultipart.addBodyPart(htmlBodyPart);
            if (email.getPicture() != null) {
                for (int i = 0; i < email.getPicture().size(); i++) {
                    contentMultipart.addBodyPart(createPhoto(email.getPicture().get(i)));
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return contentMultipart;
    }

    /**
     * 创建内嵌图片
     *
     * @param file 图片文件
     * @return 邮件体
     */
    private MimeBodyPart createPhoto(File file) {
        MimeBodyPart photoBodyPart = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(file.getAbsolutePath());
        try {
            photoBodyPart.setDataHandler(new DataHandler(fds));
            String contentId = photoBodyPart.getDataHandler().getName();
            photoBodyPart.setContentID(contentId);
            photoBodyPart.setDescription(Part.INLINE);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return photoBodyPart;
    }

    /**
     * 创建附件
     *
     * @param file 附件文件
     * @return 邮件体
     */
    private MimeBodyPart createAttachmentFile(File file) {
        MimeBodyPart attachPart = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(file.getAbsolutePath());
        try {
            attachPart.setDataHandler(new DataHandler(fds));
            attachPart.setFileName(fds.getName());
            attachPart.setDescription(Part.ATTACHMENT);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return attachPart;
    }
}

