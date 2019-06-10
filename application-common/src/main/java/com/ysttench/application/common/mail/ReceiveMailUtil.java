package com.ysttench.application.common.mail;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ysttench.application.common.mail.dto.MailAttachBean;
import com.ysttench.application.common.mail.dto.MailReceiverBean;
import com.ysttench.application.common.util.UrlUtils;
import com.ysttench.application.common.util.file.FileUtil;


public class ReceiveMailUtil {
    private static Log log = LogFactory.getLog(ReceiveMailUtil.class);
    private static String DATE_FORMAT = "yy-MM-dd HH:mm";

    /**
     * 获得邮件主题
     * 
     * @param msg
     *            邮件内容
     * @return 解码后的邮件主题
     */
    public static String getSubject(MimeMessage msg) throws MessagingException {
        try{
            return MimeUtility.decodeText(msg.getSubject());
        } catch (UnsupportedEncodingException ex) {
            throw new MessagingException("邮件主题解码出错！");
        }
    }

    /**
     * 获得邮件发件人
     * 
     * @param msg
     *            邮件内容
     * @return 姓名 <Email地址>
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static String getFrom(MimeMessage msg) throws MessagingException {
        String from = "";
        Address[] froms = msg.getFrom();
        if (froms.length < 1)
            throw new MessagingException("没有发件人!");

        InternetAddress address = (InternetAddress) froms[0];
        String addr = address.getAddress();
        String name = address.getPersonal();
        if (addr == null) {
            addr = "";
        }
        if (name == null) {
            name = "";
        }
        from = name + "<" + addr + ">";

        return from;
    }

    /**
     * 获得邮件发件人
     * 
     * @param msg
     *            邮件内容
     * @return 姓名 <Email地址>
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static String getFromName(MimeMessage msg) throws MessagingException {
        Address[] froms = msg.getFrom();
        if (froms.length < 1)
            throw new MessagingException("没有发件人!");

        InternetAddress address = (InternetAddress) froms[0];
        return address.getPersonal();
    }

    /**
     * 获得邮件发件人
     * 
     * @param msg
     *            邮件内容
     * @return 姓名 <Email地址>
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static String getFromAddress(MimeMessage msg) throws MessagingException {
        Address[] froms = msg.getFrom();
        if (froms.length < 1)
            throw new MessagingException("没有发件人!");

        InternetAddress address = (InternetAddress) froms[0];
        return address.getAddress();
    }

    /**
     * 获得邮件发件人
     * 
     * @param msg
     *            邮件内容
     * @return 姓名 <Email地址>
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static String getSender(MimeMessage msg) throws MessagingException {
        Address sender = msg.getSender();
        if (sender == null)
            return "";

        InternetAddress address = (InternetAddress) sender;
        String addr = address.getAddress();
        String name = address.getPersonal();
        if (addr == null) {
            addr = "";
        }
        if (name == null) {
            name = "";
        }
        return name + "<" + addr + ">";
    }

    /**
     * 获得邮件发件人
     * 
     * @param msg
     *            邮件内容
     * @return 姓名 <Email地址>
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static MailReceiverBean getSenderForBean(MimeMessage msg) throws MessagingException {
        Address sender = msg.getSender();
        if (sender == null)
            return new MailReceiverBean();

        InternetAddress address = (InternetAddress) sender;
        MailReceiverBean receiverBean = new MailReceiverBean();
        receiverBean.setReceiverMail(address.getAddress());
        receiverBean.setReceiverName(address.getPersonal());
        
        return receiverBean;
    }

    /**
     * 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人
     * <p>
     * Message.RecipientType.TO 收件人
     * </p>
     * <p>
     * Message.RecipientType.CC 抄送
     * </p>
     * <p>
     * Message.RecipientType.BCC 密送
     * </p>
     * 
     * @param msg
     *            邮件内容
     * @param type
     *            收件人类型
     * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ...
     * @throws MessagingException
     */
    public static String getReceiveAddress(MimeMessage msg, Message.RecipientType type) throws MessagingException {
        StringBuffer receiveAddress = new StringBuffer();
        Address[] addresss = null;
        if (type == null) {
            addresss = msg.getAllRecipients();
        } else {
            addresss = msg.getRecipients(type);
        }

        if (addresss == null || addresss.length < 1)
            throw new MessagingException("没有收件人!");
        return address2String(receiveAddress, addresss);
    }

    /**
     * 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人
     * <p>
     * Message.RecipientType.TO 收件人
     * </p>
     * <p>
     * Message.RecipientType.CC 抄送
     * </p>
     * <p>
     * Message.RecipientType.BCC 密送
     * </p>
     * 
     * @param msg
     *            邮件内容
     * @param type
     *            收件人类型
     * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ...
     * @throws MessagingException
     */
    public static List<MailReceiverBean> getReceiveAddress(MimeMessage msg) throws MessagingException {
        List<MailReceiverBean> listReceiverBean = new ArrayList<MailReceiverBean>();
        
        List<MailReceiverBean> listReceiverBeanTo = getReceiveAddressForBean(msg, Message.RecipientType.TO);
        List<MailReceiverBean> listReceiverBeanCc = getReceiveAddressForBean(msg, Message.RecipientType.CC);
        List<MailReceiverBean> listReceiverBeanBcc = getReceiveAddressForBean(msg, Message.RecipientType.BCC);
        
        listReceiverBean.addAll(listReceiverBeanTo);
        if (listReceiverBeanCc != null) {
            listReceiverBean.addAll(listReceiverBeanCc);
        }
        if (listReceiverBeanBcc != null) {
            listReceiverBean.addAll(listReceiverBeanBcc);
        }
        
        return listReceiverBean;
    }
    
    /**
     * 根据收件人类型，获取邮件收件人。
     * <p>
     * Message.RecipientType.TO 收件人
     * </p>
     * </p>
     * 
     * @param msg
     *            邮件内容
     * @param type
     *            收件人类型
     * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ...
     * @throws MessagingException
     */
    public static String getReceiveAddressTO(MimeMessage msg) throws MessagingException {
        StringBuffer receiveAddress = new StringBuffer();
        Address[] addresss = msg.getRecipients(Message.RecipientType.TO);

        if (addresss == null || addresss.length < 1)
            throw new MessagingException("没有收件人!");
        return address2String(receiveAddress, addresss);
    }

    /**
     * 根据收件人类型，获取邮件收件人。
     * <p>
     * Message.RecipientType.TO 收件人
     * </p>
     * </p>
     * 
     * @param msg
     *            邮件内容
     * @param type
     *            收件人类型
     * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ...
     * @throws MessagingException
     */
    public static List<MailReceiverBean> getReceiveAddressForBean(MimeMessage msg, Message.RecipientType type) throws MessagingException {
        Address[] addresss = null;
        if (type == null) {
            addresss = msg.getAllRecipients();
        } else {
            addresss = msg.getRecipients(type);
        }

        if (addresss == null || addresss.length < 1)
            return null;
        
        String tempType = "";
        if (type.equals(Message.RecipientType.TO) ) {
            tempType = "1";
        } else if (type.equals(Message.RecipientType.CC)) {
            tempType = "2";
        } else if (type.equals(Message.RecipientType.BCC)) {
            tempType = "3";
        }
        
        return address2List(tempType, addresss);
    }

    /**
     * 根据收件人类型，获取邮件抄送地址
     * <p>
     * Message.RecipientType.CC 抄送
     * </p>
     * 
     * @param msg
     *            邮件内容
     * @param type
     *            收件人类型
     * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ...
     * @throws MessagingException
     */
    public static String getReceiveAddressCC(MimeMessage msg) throws MessagingException {
        StringBuffer receiveAddress = new StringBuffer();
        Address[] addresss = msg.getRecipients(Message.RecipientType.CC);

        if (addresss == null || addresss.length < 1)
            return "";
        return address2String(receiveAddress, addresss);
    }

    private static String address2String(StringBuffer receiveAddress, Address[] addresss) {
        for (Address address : addresss) {
            InternetAddress internetAddress = (InternetAddress) address;
            receiveAddress.append(internetAddress.toUnicodeString()).append(";");
        }

        if (receiveAddress.length() > 1)
            receiveAddress.deleteCharAt(receiveAddress.length() - 1); // 删除最后一个逗号

        return receiveAddress.toString();
    }

    private static List<MailReceiverBean> address2List(String type, Address[] addresss) {
        List<MailReceiverBean> listReceiverBean = new ArrayList<MailReceiverBean>();
        for (Address address : addresss) {
            InternetAddress internetAddress = (InternetAddress) address;
            MailReceiverBean receiverBean = new MailReceiverBean();
            receiverBean.setReceiverType(type);
            receiverBean.setReceiverMail(internetAddress.getAddress());
            receiverBean.setReceiverName(internetAddress.getPersonal());
            
            listReceiverBean.add(receiverBean);
        }

        return listReceiverBean;
    }

    /**
     * 根据收件人类型，获取邮件密送地址。
     * <p>
     * Message.RecipientType.BCC 密送
     * </p>
     * 
     * @param msg
     *            邮件内容
     * @param type
     *            收件人类型
     * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ...
     * @throws MessagingException
     */
    public static String getReceiveAddressBCC(MimeMessage msg) throws MessagingException {
        StringBuffer receiveAddress = new StringBuffer();
        Address[] addresss = msg.getRecipients(Message.RecipientType.BCC);

        if (addresss == null || addresss.length < 1)
            return "";
        return address2String(receiveAddress, addresss);
    }

    /**
     * 根据收件人类型，获取邮件回复人。
     * <p>
     * 回复人
     * </p>
     * </p>
     * 
     * @param msg 邮件内容
     * @param type 收件人类型
     * @return 回复人
     * @throws MessagingException
     */
    public static String getReplyTo(MimeMessage msg) throws MessagingException {
        StringBuffer replyToAddress = new StringBuffer();
        Address[] addresss = msg.getReplyTo();

        if (addresss == null || addresss.length < 1)
            return "";
        return address2String(replyToAddress, addresss);
    }

    /**
     * 获得邮件发送时间
     * 
     * @param msg
     *            邮件内容
     * @return yy-MM-dd HH:mm
     * @throws MessagingException
     */
    public static String getSentDate(MimeMessage msg, String pattern) throws MessagingException {
        Date sentDate = msg.getSentDate();
        if (sentDate == null)
            return "";

        if (pattern == null || "".equals(pattern))
            pattern = DATE_FORMAT;

        return new SimpleDateFormat(pattern).format(sentDate);
    }

    /**
     * 获得邮件发送时间
     * 
     * @param msg
     *            邮件内容
     * @return yy-MM-dd HH:mm
     * @throws MessagingException
     */
    public static Date getSentDate(MimeMessage msg) throws MessagingException {
        return msg.getSentDate();
    }

    /**
     * 获得邮件接收时间
     * 
     * @param msg
     *            邮件内容
     * @return yy-MM-dd HH:mm
     * @throws MessagingException
     */
    public static String getReceivedDate(MimeMessage msg, String pattern) throws MessagingException {
        Date receivedDate = msg.getReceivedDate();
        if (receivedDate == null)
            return "";

        if (pattern == null || "".equals(pattern))
            pattern = DATE_FORMAT;

        return new SimpleDateFormat(pattern).format(receivedDate);
    }

    /**
     * 获得邮件接收时间
     * 
     * @param msg
     *            邮件内容
     * @return yy-MM-dd HH:mm
     * @throws MessagingException
     */
    public static Date getReceivedDate(MimeMessage msg) throws MessagingException {
        return msg.getReceivedDate();
    }

    /**
     * 判断邮件中是否包含附件
     * 
     * @param msg
     *            邮件内容
     * @return 邮件中存在附件返回true，不存在返回false
     * @throws MessagingException
     * @throws IOException
     */
    public static boolean isContainAttachment(Part part) throws MessagingException, IOException {
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
                    if (contentType.indexOf("application") != -1) {
                        flag = true;
                    }

                    if (contentType.indexOf("name") != -1) {
                        flag = true;
                    }
                }

                if (flag)
                    break;
            }
        } else if (part.isMimeType("message/rfc822")) {
            flag = isContainAttachment((Part) part.getContent());
        }
        return flag;
    }

    /**
     * 判断邮件是否已读
     * 
     * @param msg
     *            邮件内容
     * @return 如果邮件已读返回true,否则返回false
     * @throws MessagingException
     */
    public static boolean isSeen(MimeMessage msg) throws MessagingException {
        return msg.getFlags().contains(Flags.Flag.SEEN);
    }

    /**
     * 判断邮件是否需要阅读回执
     * 
     * @param msg
     *            邮件内容
     * @return 需要回执返回true,否则返回false
     * @throws MessagingException
     */
    public static boolean isReplySign(MimeMessage msg) throws MessagingException {
        boolean replySign = false;
        String[] headers = msg.getHeader("Disposition-Notification-To");
        if (headers != null)
            replySign = true;
        return replySign;
    }

    /**
     * 获得邮件的优先级
     * 
     * @param msg
     *            邮件内容
     * @return 1(High):紧急 3:普通(Normal) 5:低(Low)
     * @throws MessagingException
     */
    public static String getPriority(MimeMessage msg) throws MessagingException {
        String priority = "普通";
        String[] headers = msg.getHeader("X-Priority");
        if (headers != null) {
            String headerPriority = headers[0];
            if (headerPriority.indexOf("1") != -1 || headerPriority.indexOf("High") != -1)
                priority = "紧急";
            else if (headerPriority.indexOf("5") != -1 || headerPriority.indexOf("Low") != -1)
                priority = "低";
            else
                priority = "普通";
        }
        return priority;
    }

    /**
     * 获得邮件的语言
     * 
     * @param msg邮件内容
     * @return 
     * @throws MessagingException
     */
    public static String getContentLanguage(MimeMessage msg) throws MessagingException {
        StringBuffer sb = new StringBuffer();
        String[] ContentLanguages = msg.getContentLanguage();
        if (ContentLanguages != null) {
            for (String ContentLanguage : ContentLanguages) {
                sb.append(ContentLanguage);
            }
        }
        return sb.toString();
    }

    /**
     * 获得邮件文本内容
     * 
     * @param part
     *            邮件体
     * @param content
     *            存储邮件文本内容的字符串
     * @throws MessagingException
     * @throws IOException
     */
    public static String getMailTextContent(Part part) throws MessagingException, IOException {
        StringBuffer content = new StringBuffer();
        getMailTextContent(part, content);
        return content.toString();
    }

    /**
     * 获得邮件文本内容
     * 
     * @param part
     *            邮件体
     * @param content
     *            存储邮件文本内容的字符串
     * @throws MessagingException
     * @throws IOException
     */
    public static void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {
        // 如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
//        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
        boolean isContainTextAttach = isContainAttachment(part);
        if (part.isMimeType("text/*") && !isContainTextAttach) {
            content.append(part.getContent().toString());
        } else if (part.isMimeType("message/rfc822")) {
            getMailTextContent((Part) part.getContent(), content);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                getMailTextContent(bodyPart, content);
            }
        } else {
            /**
             * 否则的话,死马当成活马医,直接解析第一部分,呜呜~
             */
            Object obj = part.getContent();
            if (obj instanceof String) {
                content.append(obj);
            } else if (obj instanceof Multipart) {
                Multipart mp = (Multipart) obj;
                Part tmp = mp.getBodyPart(0);
                getMailTextContent(tmp, content);
            } else {
                // application/octet-stream
            }
        }
    }

    /**
     * 获得邮件文本内容
     * 
     * @param part
     *            邮件体
     * @param content
     *            存储邮件文本内容的字符串
     * @throws MessagingException
     * @throws IOException
     */
    public static String getContentType(Part part) throws MessagingException, IOException {
        return part.getContentType();
    }
    
    /**
     * 得到邮件正文内容 邮件的正文可能是多种类型:
     * <ul>
     * <li> text/plain </li>
     * <li> text/html</li>
     * <li> multipart/alternative</li>
     * <li> multipart/related:内有内嵌的文件,噢噢~</li>
     * <li> mutilpart/* </li>
     * <li> message/rfc822 </li>
     * </ul>
     * 
     * @param msg:待解析正文的邮件对象或邮件体(邮件的一部分)对象
     * @author M.Liang Liu
     * @version 1.0
     * @since 1.6
     * @return 根据邮件类型返回不同的邮件正文
     */
    public static String getBody(Part part, String userName) {
        StringBuffer sb = new StringBuffer();
        sb.append(new String(""));
        try {
            log.debug("The type of the part is:" + part.getContentType());
            /**
             * 纯文本或者html格式的,可以直接解析掉
             */
            if (part.isMimeType("text/plain") || part.isMimeType("text/html")) {
                sb.append(part.getContent());
            } else if (part.isMimeType("multipart/*")) {
                /**
                 * 可供选择的,一般情况下第一个是plain,第二个是html格式的
                 */
                if (part.isMimeType("multipart/alternative")) {
                    Multipart mp = (Multipart) part.getContent();
                    int index = 0;// 兼容不正确的格式,返回第一个部分
                    if (mp.getCount() > 1)
                        index = 1;// 第2个部分为html格式的哦~
                    log.debug("Now will choose the index(start from 0):" + index);
                    /**
                     * 已经根据情况进行了判断,就算不符合标准格式也不怕了.
                     */
                    Part tmp = mp.getBodyPart(index);
                    sb.append(tmp.getContent());
                } else if (part.isMimeType("multipart/related")) {
                    /**
                     * related格式的,那么第一个部分包含了body,里面含有内嵌的内容的链接.
                     */
                    Multipart mp = (Multipart) part.getContent();
                    Part tmp = mp.getBodyPart(0);
                    String body = getBody(tmp, userName);
                    int count = mp.getCount();
                    /**
                     * 要把那些可能的内嵌对象都先读出来放在服务器上,然后在替换相对地址为绝对地址
                     */
                    for (int k = 1; count > 1 && k < count; k++) {
                        Part att = mp.getBodyPart(k);
                        String attname = att.getFileName();
                        attname = MimeUtility.decodeText(attname);
                        try {
                            String fullPath = UrlUtils.getFileFullPath(MailUtil.getAttachmentFilePath(attname), userName.concat(attname));
                            saveFile(att.getInputStream(), fullPath);
                        } catch (Exception e) {
                            log.error("Error occurred when to get the photos from server");
                        }
                        String Content_ID[] = att.getHeader("Content-ID");
                        if (Content_ID != null && Content_ID.length > 0) {
                            String cid_name = Content_ID[0].replaceAll("<", "").replaceAll(">", "");
                            body = body.replaceAll("cid:" + cid_name,
                                    MailUtil.getAttachmentFilePath(attname).concat("/").concat(userName.concat(attname)));
                        }
                    }

                    sb.append(body);
                    return sb.toString();
                } else {
                    /**
                     * 其他multipart/*格式的如mixed格式,那么第一个部分包含了body,用递归解析第一个部分就可以了
                     */
                    Multipart mp = (Multipart) part.getContent();
                    Part tmp = mp.getBodyPart(0);
                    return getBody(tmp, userName);
                }
            } else if (part.isMimeType("message/rfc822")) {
                return getBody((Message) part.getContent(), userName);
            } else {
                /**
                 * 否则的话,死马当成活马医,直接解析第一部分,呜呜~
                 */
                Object obj = part.getContent();
                if (obj instanceof String) {
                    sb.append(obj);
                } else {
                    Multipart mp = (Multipart) obj;
                    Part tmp = mp.getBodyPart(0);
                    return getBody(tmp, userName);
                }
            }
        } catch (Exception e) {
            return "解析正文错误!";
        }
        return sb.toString();
    }

    public static String getBody(Message msg, String userName) {
        return getBody((Part) msg, userName);
    }

    /**
     * 获得邮件文本内容
     * 
     * @param part
     *            邮件体
     * @param content
     *            存储邮件文本内容的字符串
     * @throws MessagingException
     * @throws IOException
     */
    public static String getText(Part part) throws IOException, MessagingException {
        // 如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
        if (part.isMimeType("text/*") && !isContainTextAttach) {
            return part.getContent().toString();
        }
        return "";
    }

    /**
     * 保存附件
     * 
     * @param part
     *            邮件中多个组合体中的其中一个组合体
     * @param destDir
     *            附件保存目录
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @throws IOException
     */
    public static void saveAttachment(Part part, String destDir, Map<String, String> mapAttachment) throws MessagingException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent(); // 复杂体邮件
            // 复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                // 获得复杂体邮件中其中一个邮件体
                BodyPart bodyPart = multipart.getBodyPart(i);
                // 某一个邮件体也有可能是由多个邮件体组成的复杂体
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    String fullPath = UrlUtils.getFileFullPath(destDir, MailUtil.decodeText(bodyPart.getFileName()));
                    saveFile(bodyPart.getInputStream(), fullPath);
                    mapAttachment.put(MailUtil.decodeText(bodyPart.getFileName()), fullPath);
                } else if (bodyPart.isMimeType("multipart/*")) {
                    saveAttachment(bodyPart, destDir, mapAttachment);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
                        String fullPath = UrlUtils.getFileFullPath(destDir, MailUtil.decodeText(bodyPart.getFileName()));
                        saveFile(bodyPart.getInputStream(), fullPath);
                        mapAttachment.put(MailUtil.decodeText(bodyPart.getFileName()), fullPath);
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachment((Part) part.getContent(), destDir, mapAttachment);
        }
    }

    /**
     * 保存附件
     * 
     * @param part
     *            邮件中多个组合体中的其中一个组合体
     * @param destDir
     *            附件保存目录
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @throws IOException
     */
    public static void saveAttachment(Part part, String destDir, List<MailAttachBean> listAttachment) throws MessagingException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent(); // 复杂体邮件
            // 复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                // 获得复杂体邮件中其中一个邮件体
                BodyPart bodyPart = multipart.getBodyPart(i);
                // 某一个邮件体也有可能是由多个邮件体组成的复杂体
                String disp = bodyPart.getDisposition();
                if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
                    setMailAttach(listAttachment, bodyPart, destDir);
                } else if (bodyPart.isMimeType("multipart/*")) {
                    saveAttachment(bodyPart, destDir, listAttachment);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
                        setMailAttach(listAttachment, bodyPart, destDir);
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachment((Part) part.getContent(), destDir, listAttachment);
        }
    }

    /**
     * 附件信息设定
     * @param listAttachment
     * @param bodyPart
     * @throws MessagingException
     * @throws IOException
     * @throws UnsupportedEncodingException
     * panyimeng
     */
    private static void setMailAttach(List<MailAttachBean> listAttachment, BodyPart bodyPart, String destDir)
            throws MessagingException, IOException, UnsupportedEncodingException {
        MailAttachBean mailAttachBean = new MailAttachBean();
        mailAttachBean.setAttachType(bodyPart.getContentType());
        byte[] attach = FileUtil.input2byte(bodyPart.getInputStream());
        mailAttachBean.setAttachSize(new BigDecimal(attach.length/1024));
        mailAttachBean.setAttachFileName(MailUtil.decodeText(bodyPart.getFileName()));
        mailAttachBean.setAttachDescription(bodyPart.getDescription());
        mailAttachBean.setAttachContent(attach);
        
        String attachPath = UrlUtils.getFileFullPath(destDir, MailUtil.decodeText(bodyPart.getFileName()));
        mailAttachBean.setAttachPath(attachPath);
        saveFile(bodyPart.getInputStream(), attachPath);
        
        listAttachment.add(mailAttachBean);
    }

    /**
     * 读取输入流中的数据保存至指定目录
     * 
     * @param is
     *            输入流
     * @param fileName
     *            文件名
     * @param destDir
     *            文件存储目录
     * @throws IOException
     */
    private static void saveFile(InputStream is, String fullPath) throws IOException {
        
        FileUtil.streamWrite(fullPath, is);
    }
}
