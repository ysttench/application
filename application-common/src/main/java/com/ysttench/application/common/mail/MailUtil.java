package com.ysttench.application.common.mail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.MimeUtility;

import com.ysttench.application.common.mail.dto.MailConfig;
import com.ysttench.application.common.server.SpringContextUtil;
import com.ysttench.application.common.util.DatetimeUtil;
import com.ysttench.application.common.util.StringUtil;
import com.ysttench.application.common.util.UrlUtils;


public class MailUtil {
    private static Properties recieveProperties;
    private static Session recieveSession;
    private static Properties sendProperties;
    private static Session sendSession;
    private static MailConfig mailConfig = (MailConfig)SpringContextUtil.getBean("mailConfig");

    public static Properties getRecieveProperties() {
        // 准备连接服务器的会话信息
        if (recieveProperties == null) {
            recieveProperties = System.getProperties();
            if (mailConfig == null) return recieveProperties;
            // 开启debug调试
            recieveProperties.setProperty("mail.debug", String.valueOf(mailConfig.isMailDebug()));
//            properties.setProperty("mail.pop3.port", pro.get("mail.pop3.port")); // 端口
            recieveProperties.setProperty("mail.pop3.host", mailConfig.getMailPop3Host()); // pop3服务器
            recieveProperties.setProperty("mail.store.protocol", mailConfig.getMailStoreProtocol()); // 协议
//            properties.setProperty("mail.pop3.socketFactory.class", pro.get("mail.pop3.socketFactory.class")); 
            recieveProperties.setProperty("mail.pop3.timeout", mailConfig.getMailPop3Timeout());//超时时间 
        }

        return recieveProperties;
    }

    public static Properties getSendProperties() {
        // 准备连接服务器的会话信息
        if (sendProperties == null) {
            sendProperties = System.getProperties();
            if (mailConfig == null) return recieveProperties;
            // 开启debug调试
            sendProperties.setProperty("mail.debug", String.valueOf(mailConfig.isMailDebug()));
            // 设置邮件服务器主机名
//            properties.setProperty("mail.host", pro.get("mail.host"));
            sendProperties.setProperty("mail.smtp.host", mailConfig.getMailHost());
            // 发送服务器需要身份验证
            sendProperties.setProperty("mail.smtp.auth", mailConfig.getMailSmtpAuth());
            // 发送邮件协议名称
            sendProperties.setProperty("mail.transport.protocol", mailConfig.getMailTransportProtocol());
//            properties.setProperty("mail.smtp.socketFactory.class", pro.get("mail.smtp.socketFactory.class"));
            sendProperties.setProperty("mail.smtp.timeout", mailConfig.getMailSmtpTimeout());//超时时间 
        }

        return sendProperties;
    }

    private static void initRecieveSession() {
        // 创建Session实例对象
        if (recieveSession == null) {
            recieveSession = Session.getDefaultInstance(getRecieveProperties(), null);
            recieveSession.setDebug(true);
        }
    }

    public static Session getRecieveSession() {
        MailUtil.initRecieveSession();
        return recieveSession;
    }

    private static void initSendSession() {
        // 创建Session实例对象
        if (sendSession == null) {
            sendSession = Session.getDefaultInstance(getSendProperties(), null);
            sendSession.setDebug(true);
        }
    }

    public static Session getSendSession() {
        MailUtil.initSendSession();
        return sendSession;
    }

    public static Store getStore(String protocol) throws NoSuchProviderException {
        if (StringUtil.isEmptyString(protocol)) {
            return MailUtil.getRecieveSession().getStore(MailConstant.MAIL_STORE_PROTOCOL_POP3);
        }
        return MailUtil.getRecieveSession().getStore(protocol);
    }

    public static Store getStore(URLName urln) throws NoSuchProviderException {
        return MailUtil.getRecieveSession().getStore(urln);
    }

    public static Transport getTransport() throws NoSuchProviderException {
        return MailUtil.getSendSession().getTransport();
    }

    /**
     * 文本解码
     * 
     * @param encodeText
     *            解码MimeUtility.encodeText(String text)方法编码后的文本
     * @return 解码后的文本
     * @throws UnsupportedEncodingException
     */
    public static String decodeText(String encodeText) throws UnsupportedEncodingException {
        if (encodeText == null || "".equals(encodeText)) {
            return "";
        } else {
            return MimeUtility.decodeText(encodeText);
        }
    }

    /**
     * 文本编码
     * 
     * @param decodeText
     *            解码MimeUtility.encodeText(String text)方法编码后的文本
     * @return 解码后的文本
     * @throws UnsupportedEncodingException
     */
    public static String encodeText(String decodeText) throws UnsupportedEncodingException {
        if (decodeText == null || "".equals(decodeText)) {
            return "";
        } else {
            return MimeUtility.encodeText(decodeText);
        }
    }

    /**
     * 文本编码
     * 
     * @param decodeText
     *            解码MimeUtility.encodeWord(String text)方法编码后的文本
     * @return 解码后的文本
     * @throws UnsupportedEncodingException
     */
    public static String encodeWord(String decodeWord) throws UnsupportedEncodingException {
        if (decodeWord == null || "".equals(decodeWord)) {
            return "";
        } else {
            return MimeUtility.encodeWord(decodeWord);
        }
    }

    /**
     * 设定附件保存路劲
     * 
     * @param mailAddress
     * @return
     * @throws MessagingException
     */
    public static String getAttachmentFilePath(String mailAddress){
        StringBuffer sb = new StringBuffer();
        try {
            sb.append(UrlUtils.getSysRootPath());
            sb.append(MailConstant.MAIL_ATTACHMENT_FILE);
            sb.append(MimeUtility.decodeText(mailAddress));
            sb.append(File.separator);
            sb.append(DatetimeUtil.getDate());
            sb.append(File.separator);
            sb.append(UUID.randomUUID().toString());
            sb.append(File.separator);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return sb.toString().replace("<", "").replace(">", "");
    }
}
