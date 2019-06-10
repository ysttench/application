package com.ysttench.application.common.mail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FromStringTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.AndTerm;
import javax.mail.search.SubjectTerm;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.sun.mail.pop3.POP3Folder;
import com.ysttench.application.common.mail.dto.MailAttachBean;
import com.ysttench.application.common.mail.dto.MailClientBean;

public class MailClientSupport {
    protected final Log log = LogFactory.getLog(this.getClass());
    private Session recieveSession;
    private Session sendSession;
    private Store store;
    private Folder folder;
    private Transport transport;
    private JavaMailSenderImpl javaMailSender;

    /**
     * 构造函数，数据初始化
     */
    public MailClientSupport() {
        if (this.sendSession == null) {
            this.sendSession = MailUtil.getSendSession();
            try {
                this.transport = this.getSendSession().getTransport();
            } catch (NoSuchProviderException ex) {
                log.error("创建发送邮件实例对象失败！");
                ex.printStackTrace();
            }
        }
        if (this.recieveSession == null) {
            this.recieveSession = MailUtil.getRecieveSession();
            if (this.store == null) {
                this.store = this.getStore(MailConstant.MAIL_STORE_PROTOCOL_POP3);
            }
        }

        if (this.javaMailSender == null) {
            this.javaMailSender = new JavaMailSenderImpl();
            this.javaMailSender.setJavaMailProperties(MailUtil.getSendProperties());
        }
    }

    /**
     * 邮件发送连接打开
     */
    public void initSmtpConnecttion() {
        Map<String, String> pro = new HashMap<String, String>();
        javaMailSender.setHost(pro.get("mail.smtp.host"));
        javaMailSender.setUsername(pro.get("mail.username"));
        javaMailSender.setPassword(pro.get("mail.password"));
    }

    /**
     * 邮件发送连接打开
     * 
     * @param user
     * @param password
     * @throws MessagingException
     */
    public void initSmtpConnecttion(String username, String password) throws MessagingException {
        // 连接服务器
        Map<String, String> pro = new HashMap<String, String>();
        javaMailSender.setHost(pro.get("mail.smtp.host"));
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
    }

    /**
     * 邮件发送连接打开
     * 
     * @param smtpHost
     * @param user
     * @param password
     * @throws MessagingException
     */
    public void initSmtpConnecttion(String smtpHost, String username, String password) throws MessagingException {
        // 连接服务器
        javaMailSender.setHost(smtpHost);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
    }

    /**
     * 邮件发送连接打开
     * 
     * @throws MessagingException
     */
    public void initTransportConnecttion() throws MessagingException {
        Map<String, String> pro = new HashMap<String, String>();
        // 连接服务器
        this.setTransport(this.sendSession.getTransport());
        this.transport.connect(pro.get("mail.username"), pro.get("mail.password"));
    }

    /**
     * 邮件发送连接打开
     * 
     * @param user
     * @param password
     * @throws MessagingException
     */
    public void initTransportConnecttion(String username, String password) throws MessagingException {
        // 连接服务器
        this.setTransport(this.sendSession.getTransport());
        this.transport.connect(username, password);
    }

    /**
     * 邮件发送连接打开
     * 
     * @param smtpHost
     * @param user
     * @param password
     * @throws MessagingException
     */
    public void initTransportConnecttion(String smtpHost, String username, String password) throws MessagingException {
        // 连接服务器
        this.setTransport(this.sendSession.getTransport());
        this.transport.connect(smtpHost, username, password);
    }

    /**
     * 邮件接收连接打开
     * 
     * @throws MessagingException
     */
    public void initPop3Connecttion() throws MessagingException {
        Map<String, String> pro = new HashMap<String, String>();
        // 连接服务器
        this.store.connect(pro.get("mail.username"), pro.get("mail.password"));
    }

    /**
     * 邮件接收连接打开
     * 
     * @param user
     * @param password
     * @throws MessagingException
     */
    public void initPop3Connecttion(String username, String password) throws MessagingException {
        // 连接服务器
        this.store.connect(username, password);
    }

    /**
     * 邮件接收连接打开
     * 
     * @param popHost
     * @param user
     * @param password
     * @throws MessagingException
     */
    public void initPop3Connecttion(String popHost, String username, String password) throws MessagingException {
        // 连接服务器
        this.store.connect(popHost, username, password);
    }

    /**
     * 邮件接收连接打开
     * 
     * @param storeProtocol
     * @param popHost
     * @param port
     * @param user
     * @param password
     * @throws MessagingException
     */
    public void initPop3Connecttion(String storeProtocol, String popHost, int port, String username, String password)
            throws MessagingException {
        // 连接服务器
        URLName urln = new URLName(storeProtocol, popHost, port, null, username, password);
        this.setStore(this.recieveSession.getStore(urln));
        this.store.connect(popHost, username, password);
    }

    /**
     * 资源释放
     */
    public void finalize() {
        // finalization code here
        try {
            // 释放资源
            if (this.folder != null && this.folder.isOpen()) {
                this.folder.close(true);
            }
            if (this.store != null && this.store.isConnected()) {
                this.store.close();
            }
            if (this.transport != null && this.transport.isConnected()) {
                this.transport.close();
            }
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取邮件列表信息
     * 
     * @return
     * @throws MessagingException
     */
    public Message[] getMessages() throws MessagingException {
        return this.getMessages(MailConstant.MAIL_FOLDER_INBOX);
    }

    /**
     * 获取邮件列表信息
     * 
     * @param folderName
     * @return
     * @throws MessagingException
     */
    public Message[] getMessages(String folderName) throws MessagingException {

        Message[] messages;
        if (this.store == null)
            throw new MessagingException("读取邮件前先连接邮件服务器！");
        // 打开文件夹
        this.getFolder(folderName);
        // 总的邮件数
        int mailCount = this.folder.getMessageCount();
        if (mailCount == 0) {
            return new Message[] {};
        } else {
            messages = this.folder.getMessages();
        }

        // 释放资源
        return messages;
    }

    /**
     * 删除邮件服务器上的邮件
     * 
     * @param folderName
     * @return
     * @throws MessagingException
     */
    public void deleteMessage(String fromAddress, String subject, String messageId, String mailBox) throws MessagingException {

        if (this.store == null)
            throw new MessagingException("读取邮件前先连接邮件服务器！");
        // 打开文件夹
        this.getFolder(mailBox);
        // 搜索发件人为 test_hao@sina.cn 和主题为"测试1"的邮件  
        SearchTerm st = new AndTerm(new FromStringTerm(fromAddress), new SubjectTerm(subject));
        // 不是像上面那样直接返回所有邮件，而是使用Folder.search()方法  
        Message[] messages = folder.search(st);
        int mailCounts = messages.length;
        log.info("搜索过滤到" + mailCounts + " 封符合条件的邮件！");

        for (int i = 0; i < mailCounts; i++) {

            MimeMessage msg = (MimeMessage) messages[i];

            if (messageId.equals(msg.getMessageID())) {
                // 设置删除标记，一定要记得调用saveChanges()方法
                messages[i].setFlag(Flags.Flag.DELETED, true);
                messages[i].saveChanges();
                log.info("成功设置了删除标记！");
            }
        }
    }

    /**
     * 获取邮件的UID信息
     * 
     * @param folderName
     * @return
     * @throws MessagingException
     */
    public String getUID(MimeMessage msg) throws MessagingException {
        
        POP3Folder inbox = (POP3Folder) this.getFolder(); 
        return inbox.getUID(msg);
    }

    /**
     * 获取服务器上所有文件夹
     * 
     * @return
     * @throws MessagingException
     */
    public Folder[] getFolders() throws MessagingException {
        if (this.store == null)
            throw new MessagingException("读取邮件前先连接邮件服务器！");
        Folder defaultFolder = store.getDefaultFolder();

        return defaultFolder.list();
    }

    /**
     * 判断服务器上文件夹是否存在
     * 
     * @return
     * @throws MessagingException
     */
    public boolean isExistsFolder(String folderName) throws MessagingException {
        boolean isExistsFolder = false;
        if (this.store == null)
            throw new MessagingException("读取邮件前先连接邮件服务器！");
        Folder defaultFolder = store.getDefaultFolder();

        for (Folder folder : defaultFolder.list()) {
            if (folder.getName().toLowerCase().equals(folderName.toLowerCase())) {
                isExistsFolder = true;
            }
        }
        return isExistsFolder;
    }

    /**
     * 解析邮件列表信息
     * 
     * @param messages
     *            要解析的邮件列表
     */
    public List<MailClientBean> parseMessage(Message... messages) throws MessagingException, IOException {
        if (messages == null || messages.length < 1)
            throw new MessagingException("未找到要解析的邮件!");

        List<MailClientBean> listReceiveMailBean = new ArrayList<MailClientBean>();
        // 解析所有邮件
        for (int i = 0, count = messages.length; i < count; i++) {
            MimeMessage msg = (MimeMessage) messages[i];
            System.out.println("------------------解析第" + msg.getMessageNumber() + "封邮件-------------------- ");
            System.out.println("主题: " + ReceiveMailUtil.getSubject(msg));
            System.out.println("发件人: " + ReceiveMailUtil.getFrom(msg));
            System.out.println("收件人：" + ReceiveMailUtil.getReceiveAddress(msg, null));
            System.out.println("发送时间：" + ReceiveMailUtil.getSentDate(msg, null));
            System.out.println("是否已读：" + ReceiveMailUtil.isSeen(msg));
            System.out.println("邮件优先级：" + ReceiveMailUtil.getPriority(msg));
            System.out.println("是否需要回执：" + ReceiveMailUtil.isReplySign(msg));
            System.out.println("邮件大小：" + msg.getSize() * 1024 + "kb");
            boolean isContainAttachment = ReceiveMailUtil.isContainAttachment(msg);
            System.out.println("是否包含附件：" + isContainAttachment);
            Map<String, String> mapAttachment = new HashMap<String, String>();
            if (isContainAttachment) {
                String mailAddress = ReceiveMailUtil.getFromAddress(msg);
                ReceiveMailUtil.saveAttachment(msg, MailUtil.getAttachmentFilePath(mailAddress), mapAttachment); // 保存附件
            }
            StringBuffer content = new StringBuffer(30);
            ReceiveMailUtil.getMailTextContent(msg, content);
            System.out.println("邮件正文：" + (content.length() > 100 ? content.substring(0, 100) + "..." : content));
            System.out.println("------------------第" + msg.getMessageNumber() + "封邮件解析结束-------------------- ");
            System.out.println();

            MailClientBean receiveMailBean = new MailClientBean();
            receiveMailBean.setFromName(ReceiveMailUtil.getFromName(msg));
            receiveMailBean.setFromAddress(ReceiveMailUtil.getFromAddress(msg));
            receiveMailBean.setReceiveAddressTO(ReceiveMailUtil.getReceiveAddressTO(msg));
            receiveMailBean.setReceiveAddressCC(ReceiveMailUtil.getReceiveAddressCC(msg));
            receiveMailBean.setReceiveAddressBCC(ReceiveMailUtil.getReceiveAddressBCC(msg));
            receiveMailBean.setSubject(ReceiveMailUtil.getSubject(msg));
            receiveMailBean.setText(ReceiveMailUtil.getText(msg));
            receiveMailBean.setContent(ReceiveMailUtil.getMailTextContent(msg));
            receiveMailBean.setContentType(ReceiveMailUtil.getContentType(msg));
            receiveMailBean.setSentDate(ReceiveMailUtil.getSentDate(msg));
            receiveMailBean.setReceivedDate(ReceiveMailUtil.getReceivedDate(msg));
            receiveMailBean.setSeen(ReceiveMailUtil.isSeen(msg));
            receiveMailBean.setPriority(ReceiveMailUtil.getPriority(msg));
            receiveMailBean.setReplySign(ReceiveMailUtil.isReplySign(msg));
            receiveMailBean.setMailSize(msg.getSize());
            receiveMailBean.setLineCount(msg.getLineCount());
            receiveMailBean.setContainAttachment(ReceiveMailUtil.isContainAttachment(msg));
            receiveMailBean.setMapAttachment(mapAttachment);
            receiveMailBean.setSender(ReceiveMailUtil.getSender(msg));
            receiveMailBean.setReplyTo(ReceiveMailUtil.getReplyTo(msg));
            receiveMailBean.setDisposition(msg.getDisposition());
            receiveMailBean.setEncoding(msg.getEncoding());
            receiveMailBean.setContentId(msg.getContentID());
            receiveMailBean.setContentMd5(msg.getContentMD5());
            receiveMailBean.setDescription(msg.getDescription());
            receiveMailBean.setContentLanguage(ReceiveMailUtil.getContentLanguage(msg));
            receiveMailBean.setMessageId(msg.getMessageID());
            receiveMailBean.setFileName(msg.getFileName());

            listReceiveMailBean.add(receiveMailBean);
        }
        return listReceiveMailBean;
    }

    /**
     * 解析邮件列表信息
     * 
     * @param messages
     *            要解析的邮件列表
     */
    public MailClientBean parseMessage(MimeMessage msg) throws MessagingException, IOException {
        if (msg == null)
            throw new MessagingException("未找到要解析的邮件!");

        // 解析所有邮件
        System.out.println("------------------解析第" + msg.getMessageNumber() + "封邮件-------------------- ");
        System.out.println("主题: " + ReceiveMailUtil.getSubject(msg));
        System.out.println("发件人: " + ReceiveMailUtil.getFrom(msg));
        System.out.println("收件人：" + ReceiveMailUtil.getReceiveAddress(msg, null));
        System.out.println("发送时间：" + ReceiveMailUtil.getSentDate(msg, null));
        System.out.println("是否已读：" + ReceiveMailUtil.isSeen(msg));
        System.out.println("邮件优先级：" + ReceiveMailUtil.getPriority(msg));
        System.out.println("是否需要回执：" + ReceiveMailUtil.isReplySign(msg));
        System.out.println("邮件大小：" + msg.getSize() * 1024 + "kb");
        boolean isContainAttachment = ReceiveMailUtil.isContainAttachment(msg);
        System.out.println("是否包含附件：" + isContainAttachment);
        List<MailAttachBean> listAttachment = new ArrayList<MailAttachBean>();
        if (isContainAttachment) {
            String mailAddress = ReceiveMailUtil.getFromAddress(msg);
            ReceiveMailUtil.saveAttachment(msg, MailUtil.getAttachmentFilePath(mailAddress), listAttachment); // 保存附件
        }
        StringBuffer content = new StringBuffer(30);
        ReceiveMailUtil.getMailTextContent(msg, content);
        System.out.println("邮件正文：" + (content.length() > 100 ? content.substring(0, 100) + "..." : content));
        System.out.println("------------------第" + msg.getMessageNumber() + "封邮件解析结束-------------------- ");
        System.out.println();

        MailClientBean receiveMailBean = new MailClientBean();
        receiveMailBean.setFromName(ReceiveMailUtil.getFromName(msg));
        receiveMailBean.setFromAddress(ReceiveMailUtil.getFromAddress(msg));
        receiveMailBean.setListReceiveAddress(ReceiveMailUtil.getReceiveAddress(msg));
        receiveMailBean.setReceiveAddressTO(ReceiveMailUtil.getReceiveAddressTO(msg));
        receiveMailBean.setReceiveAddressCC(ReceiveMailUtil.getReceiveAddressCC(msg));
        receiveMailBean.setReceiveAddressBCC(ReceiveMailUtil.getReceiveAddressBCC(msg));
        receiveMailBean.setSubject(ReceiveMailUtil.getSubject(msg));
        receiveMailBean.setText(ReceiveMailUtil.getText(msg));
        receiveMailBean.setContent(ReceiveMailUtil.getMailTextContent(msg));
        receiveMailBean.setContentType(ReceiveMailUtil.getContentType(msg));
        receiveMailBean.setSentDate(ReceiveMailUtil.getSentDate(msg));
        receiveMailBean.setReceivedDate(ReceiveMailUtil.getReceivedDate(msg));
        receiveMailBean.setSeen(ReceiveMailUtil.isSeen(msg));
        receiveMailBean.setPriority(ReceiveMailUtil.getPriority(msg));
        receiveMailBean.setReplySign(ReceiveMailUtil.isReplySign(msg));
        receiveMailBean.setMailSize(msg.getSize());
        receiveMailBean.setLineCount(msg.getLineCount());
        receiveMailBean.setContainAttachment(ReceiveMailUtil.isContainAttachment(msg));
        receiveMailBean.setListAttachment(listAttachment);
        receiveMailBean.setSender(ReceiveMailUtil.getSender(msg));
        receiveMailBean.setReplyTo(ReceiveMailUtil.getReplyTo(msg));
        receiveMailBean.setDisposition(msg.getDisposition());
        receiveMailBean.setEncoding(msg.getEncoding());
        receiveMailBean.setContentId(msg.getContentID());
        receiveMailBean.setContentMd5(msg.getContentMD5());
        receiveMailBean.setDescription(msg.getDescription());
        receiveMailBean.setContentLanguage(ReceiveMailUtil.getContentLanguage(msg));
        receiveMailBean.setMessageId(msg.getMessageID());
        receiveMailBean.setFileName(msg.getFileName());

        return receiveMailBean;
    }

    /**
     * 向人发送简单邮件。
     * 
     * @param mailto
     * @param text
     * @param title
     * @throws MessagingException
     */
    public MimeMessage sendMail(MailClientBean mailClientBean) throws MessagingException {

        MimeMessage mimeMessage = this.getJavaMailSender().createMimeMessage();
        MimeMessageHelper messageHelper = SendMailUtil.prepare(mimeMessage, mailClientBean, false);
        mimeMessage = messageHelper.getMimeMessage();
        // 发送邮件
        this.javaMailSender.send(mimeMessage);

        return mimeMessage;
    }

    /**
     * 向人发送简单邮件。
     * 
     * @param mailto
     * @param text
     * @param title
     * @throws MessagingException
     */
    public void sendMail(JSONObject jsonObject) throws MessagingException {

        MimeMessage mimeMessage = this.getJavaMailSender().createMimeMessage();
        MimeMessageHelper messageHelper = SendMailUtil.prepare(mimeMessage, jsonObject);
        mimeMessage = messageHelper.getMimeMessage();
        // 发送邮件
        this.javaMailSender.send(mimeMessage);

    }

    /**
     * 向人发送简单邮件
     * 
     * @param jsonObject
     * @throws MessagingException
     */
    public void sendSimpleMail(JSONObject jsonObject) throws MessagingException {
        MimeMessage mimeMessage = this.getJavaMailSender().createMimeMessage();
        MimeMessageHelper messageHelper = SendMailUtil.simplePrepare(mimeMessage, jsonObject, false);
        mimeMessage = messageHelper.getMimeMessage();

        // 发送邮件
        this.javaMailSender.send(mimeMessage);
    }

    /**
     * 向单人发送简单邮件。
     * 
     * @param mailto
     * @param text
     * @param title
     * @throws MessagingException
     */
    public void sendSimpleMail(String mailto, String text, String title) throws MessagingException {

        MimeMessage mimeMessage = this.getJavaMailSender().createMimeMessage();
        MimeMessageHelper messageHelper = SendMailUtil.prepare(mimeMessage, mailto, text, title);
        mimeMessage = messageHelper.getMimeMessage();
        // 发送邮件
        this.javaMailSender.send(mimeMessage);

    }

    /**
     * 向多人发送简单邮件
     * 
     * @param mailto
     * @param text
     * @param title
     * @throws MessagingException
     */
    public void sendBatchSimpleMail(String[] mailto, String text, String title) throws MessagingException {

        MimeMessage mimeMessage = this.getJavaMailSender().createMimeMessage();
        MimeMessageHelper messageHelper = SendMailUtil.prepare(mimeMessage, mailto, text, title);
        mimeMessage = messageHelper.getMimeMessage();
        // 发送邮件
        this.javaMailSender.send(mimeMessage);
    }

    /**
     * 向多人发送带附件的邮件
     * 
     * @param text
     * @param title
     * @param filePath
     * @throws MessagingException
     */
    public void sendBatchMailWithFile(String[] mailto, String text, String title, String[] filePath)
            throws MessagingException, UnsupportedEncodingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, MailConstant.MAIL_ENCODING);

        messageHelper.setFrom(new InternetAddress(MailUtil.encodeText(javaMailSender.getUsername())));
        messageHelper.setSubject(title);

        if (filePath != null) {
            BodyPart mdp = new MimeBodyPart();// 新建一个存放信件内容的BodyPart对象
            mdp.setContent(text, MailConstant.MAIL_CONTENT_TYPE_HTML);// 给BodyPart对象设置内容和格式/编码方式
            Multipart mm = new MimeMultipart();// 新建一个MimeMultipart对象用来存放BodyPart对象
            mm.addBodyPart(mdp);// 将BodyPart加入到MimeMultipart对象中(可以加入多个BodyPart)
            // 把mm作为消息对象的内容
            MimeBodyPart filePart;
            FileDataSource filedatasource;
            // 逐个加入附件
            for (int j = 0; j < filePath.length; j++) {
                filePart = new MimeBodyPart();
                filedatasource = new FileDataSource(filePath[j]);
                filePart.setDataHandler(new DataHandler(filedatasource));
                try {
                    filePart.setFileName(MailUtil.encodeText(filedatasource.getName()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mm.addBodyPart(filePart);
            }
            mimeMessage.setContent(mm);
        } else {
            messageHelper.setText(text, true);
        }

        List<InternetAddress> list = new ArrayList<InternetAddress>();// 不能使用string类型的类型，这样只能发送一个收件人
        for (int i = 0; i < mailto.length; i++) {
            list.add(new InternetAddress(mailto[i]));
        }
        InternetAddress[] address = list.toArray(new InternetAddress[list.size()]);

        mimeMessage.setRecipients(Message.RecipientType.TO, address);
        mimeMessage = messageHelper.getMimeMessage();

        // 发送邮件
        javaMailSender.send(mimeMessage);

    }

    public Store getStore() {
        if (this.store == null) {
            return this.getStore(MailConstant.MAIL_STORE_PROTOCOL_POP3);
        } else {
            return this.store;
        }
    }

    private Store getStore(String protocol) {
        try {
            return this.recieveSession.getStore(protocol);
        } catch (NoSuchProviderException ex) {
            log.error("创建接手实例对象失败！");
            ex.printStackTrace();
        }
        return null;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    /**
     * 获取邮件服务器上的文件夹
     * @return
     * panyimeng
     */
    public Folder getFolder() {
        if (this.folder == null) {
            return this.getFolder(MailConstant.MAIL_FOLDER_INBOX);
        } else {
            return this.folder;
        }
    }

    /**
     * 获取邮件服务器上的只读文件夹
     * @param folderName
     * @return
     * panyimeng
     */
    public Folder getFolder(String folderName) {
        try {
            if (this.store == null)
                return null;
            // 打开文件夹
            this.setFolder(this.store.getFolder(folderName));
            this.folder.open(Folder.READ_ONLY);
            return this.folder;
        } catch (MessagingException ex) {
            log.error("读取邮件前先连接邮件服务器！");
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 获取邮件服务器上的可读写文件夹
     * @param folderName
     * @return
     * panyimeng
     */
    public Folder getWriteFolder(String folderName) {
        try {
            if (this.store == null)
                return null;
            // 打开文件夹
            this.setFolder(this.store.getFolder(folderName));
            this.folder.open(Folder.READ_WRITE);
            return this.folder;
        } catch (MessagingException ex) {
            log.error("读取邮件前先连接邮件服务器！");
            ex.printStackTrace();
        }
        return null;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public Transport getTransport() {
        return this.transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public Session getRecieveSession() {
        return recieveSession;
    }

    public void setRecieveSession(Session recieveSession) {
        this.recieveSession = recieveSession;
    }

    public Session getSendSession() {
        return sendSession;
    }

    public void setSendSession(Session sendSession) {
        this.sendSession = sendSession;
    }

    public JavaMailSenderImpl getJavaMailSender() {
        return this.javaMailSender;
    }

    public void setJavaMailSender(JavaMailSenderImpl javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

}
