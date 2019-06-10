package com.ysttench.application.common.mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.ysttench.application.common.mail.dto.MailClientBean;
import com.ysttench.application.common.util.JsonUtils;

import net.sf.json.JSONObject;

public class SendMailUtil {
    private static Log logger = LogFactory.getLog(SendMailUtil.class);
    
    /**
     * 邮件发送前数据设定
     * @param mimeMessage
     * @param jsonObject
     * @return
     * @throws MessagingException
     */
    public static MimeMessageHelper simplePrepare(MimeMessage mimeMessage, JSONObject jsonObject, boolean isHtml) throws MessagingException {
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, MailConstant.MAIL_ENCODING);
        //设置发信人地址
        messageHelper.setFrom(new InternetAddress(jsonObject.getString("from")));
        //设置收信人地址
        messageHelper.setTo(jsonObject.getString("receiveAddressTO").split(";"));
        //设置e-mail标题
        messageHelper.setSubject(jsonObject.getString("subject"));
        //设置e-mail发送时间
        messageHelper.setSentDate(new Date());
        //设置e-mail内容
        messageHelper.setText(jsonObject.getString("text"), isHtml);
        
        return messageHelper;
    }
    
    /**
     * 邮件发送前数据设定
     * @param mimeMessage
     * @param jsonObject
     * @return
     * @throws MessagingException
     */
    public static MimeMessageHelper prepare(MimeMessage mimeMessage, MailClientBean mailClientBean, boolean isHtml) throws MessagingException {
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, MailConstant.MAIL_ENCODING);
        //设置发信人地址
        try {
            messageHelper.setFrom(new InternetAddress(mailClientBean.getFromAddress(), mailClientBean.getFromName()));
        } catch (UnsupportedEncodingException ex) {
            logger.error("发件人设定出错! 参数：" + mailClientBean.getFromAddress()
                    + ","+ mailClientBean.getFromName());
        }
        //设置收信人地址
        messageHelper.setTo(mailClientBean.getReceiveAddressTO().split(";"));
        if (mailClientBean.getReceiveAddressCC() != null && !"".equals(mailClientBean.getReceiveAddressCC())) {
            messageHelper.setCc(mailClientBean.getReceiveAddressCC().split(";"));
        }
        if (mailClientBean.getReceiveAddressBCC() != null && !"".equals(mailClientBean.getReceiveAddressBCC())) {
            messageHelper.setBcc(mailClientBean.getReceiveAddressBCC().split(";"));
        }
        //设置e-mail标题
        messageHelper.setSubject(mailClientBean.getSubject());
        //设置e-mail发送时间
        messageHelper.setSentDate(new Date());
        //设置e-mail内容
        messageHelper.setText(mailClientBean.getText(), isHtml);
        //添加附件
        if (mailClientBean.isContainAttachment()) {
            setAttachment(mailClientBean.getMapAttachment(), messageHelper);
        }
        
        return messageHelper;
    }

    /**
     * 附件设定
     * @param mailClientBean
     * @param messageHelper
     * @throws MessagingException
     */
    public static void setAttachment(MailClientBean mailClientBean, MimeMessageHelper messageHelper)
            throws MessagingException {
        if (mailClientBean.isContainAttachment()) {
            Map<String, String> mapAttachment = mailClientBean.getMapAttachment();
            String[] keys = (String[])mapAttachment.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                FileSystemResource file = new FileSystemResource(mapAttachment.get(keys[i]));
                try {
                    messageHelper.addAttachment(MailUtil.encodeWord(keys[i]), file);
                } catch (UnsupportedEncodingException ex) {
                    logger.error("附件文件名编码出错！");
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * 附件设定
     * @param mailClientBean
     * @param messageHelper
     * @throws MessagingException
     */
    public static void setAttachment(Map<String, String> mapAttachment, MimeMessageHelper messageHelper)
            throws MessagingException {
        Iterator<String> itKeys = mapAttachment.keySet().iterator();
        while(itKeys.hasNext()) {
            String key = itKeys.next();
            FileSystemResource file = new FileSystemResource(mapAttachment.get(key));
            try {
                messageHelper.addAttachment(MailUtil.encodeWord(key), file);
            } catch (UnsupportedEncodingException ex) {
                logger.error("附件文件名编码出错！");
                ex.printStackTrace();
            }
        }
    }

    /**
     * 附件设定
     * @param mailClientBean
     * @param messageHelper
     * @throws MessagingException
     */
    public static void setAttachmentByJson(JSONObject objAttachment, MimeMessageHelper messageHelper)
            throws MessagingException {
        Map<String, Object> mapAttachment = JsonUtils.parseJSON2Map(objAttachment.toString());
        Iterator<String> itKeys = mapAttachment.keySet().iterator();
        while(itKeys.hasNext()) {
            String key = itKeys.next();
            FileSystemResource file = new FileSystemResource(mapAttachment.get(key).toString());
            try {
                messageHelper.addAttachment(MailUtil.encodeWord(key), file);
            } catch (UnsupportedEncodingException ex) {
                logger.error("附件文件名编码出错！");
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * 邮件发送前数据设定
     * @param mimeMessage
     * @param jsonObject
     * @return
     * @throws MessagingException
     */
    public static MimeMessageHelper prepare(MimeMessage mimeMessage, JSONObject jsonObject) throws MessagingException {
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, MailConstant.MAIL_ENCODING);
        //设置发信人地址
        messageHelper.setFrom(new InternetAddress(jsonObject.getString("from")));
        //设置收信人地址
        messageHelper.setTo(jsonObject.getString("receiveAddressTO").split(";"));
        if (jsonObject.getString("receiveAddressCC") != null && !"".equals(jsonObject.getString("receiveAddressCC"))) {
            messageHelper.setCc(jsonObject.getString("receiveAddressCC").split(";"));
        }
        if (jsonObject.getString("receiveAddressBCC") != null && !"".equals(jsonObject.getString("receiveAddressBCC"))) {
            messageHelper.setBcc(jsonObject.getString("receiveAddressBCC").split(";"));
        }
        //设置e-mail标题
        messageHelper.setSubject(jsonObject.getString("subject"));
        //设置e-mail发送时间
        messageHelper.setSentDate(new Date());
        //设置e-mail内容
        messageHelper.setText(jsonObject.getString("text"), false);
        //添加附件
        if (jsonObject.getString("isContainAttachment") == "true") {
            JSONObject objAttachment = jsonObject.getJSONObject("mapAttachment");
            setAttachmentByJson(objAttachment, messageHelper);
        }
        
        return messageHelper;
    }
    
    /**
     * 邮件发送前数据设定
     * @param mimeMessage
     * @param jsonObject
     * @return
     * @throws MessagingException
     */
    public static MimeMessageHelper prepare(MimeMessage mimeMessage, String mailto, String text, String title) throws MessagingException {
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, MailConstant.MAIL_ENCODING);
        //设置发信人地址
        messageHelper.setFrom(new InternetAddress(mimeMessage.getSender().toString()));
        //设置收信人地址
        messageHelper.setTo(mailto.split(";"));
        //设置e-mail标题
        messageHelper.setSubject(title);
        //设置e-mail发送时间
        messageHelper.setSentDate(new Date());
        //设置e-mail内容
        messageHelper.setText(text, true);
        
        return messageHelper;
    }
    
    /**
     * 邮件发送前数据设定
     * @param mimeMessage
     * @param jsonObject
     * @return
     * @throws MessagingException
     */
    public static MimeMessageHelper prepare(MimeMessage mimeMessage, String[] mailto, String text, String title) throws MessagingException {
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, MailConstant.MAIL_ENCODING);
        //设置发信人地址
        messageHelper.setFrom(new InternetAddress(mimeMessage.getSender().toString()));
        //设置收信人地址
        messageHelper.setTo(mailto);
        //设置e-mail标题
        messageHelper.setSubject(title);
        //设置e-mail发送时间
        messageHelper.setSentDate(new Date());
        //设置e-mail内容
        messageHelper.setText(text, true);
        
        return messageHelper;
    }
}
