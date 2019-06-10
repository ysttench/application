package com.ysttench.application.common.mail.dto;

//import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

//@Configuration
//@PropertySource("classpath:config/mail-config.properties")
public class MailConfig {
//    @Value("${mail.debug}")
    private boolean mailDebug;
//    @Value("${mail.pop3.host}")
    private String mailPop3Host;
//    @Value("${mail.pop3.port}")
    private String mailPop3Port;
//    @Value("${mail.store.protocol}")
    private String mailStoreProtocol;
//    @Value("${mail.pop3.timeout}")
    private String mailPop3Timeout;
//    @Value("${mail.pop3.socketFactory.class}")
    private String mailPop3SocketFactoryClass;

//    @Value("${mail.host}")
    private String mailHost;
//    @Value("${mail.smtp.auth}")
    private String mailSmtpAuth;
//    @Value("${mail.smtp.timeout}")
    private String mailSmtpTimeout;
//    @Value("${mail.transport.protocol}")
    private String mailTransportProtocol;
//    @Value("${mail.smtp.socketFactory.class}")
    private String mailSmtpSocketFactoryClass;


//    @Value("${mail.from}")
    private String mailFrom;
//    @Value("${mail.smtp.host}")
    private String mailSmtpHost;
//    @Value("${mail.username}")
    private String mailUsername;
//    @Value("${mail.password}")
    private String mailPassword;

//    @Value("${mail.to}")
    private String mailTo;

    //注意！配置一个PropertySourcesPlaceholderConfigurer的Bean
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigure() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public boolean isMailDebug() {
        return mailDebug;
    }

    public void setMailDebug(boolean mailDebug) {
        this.mailDebug = mailDebug;
    }

    public String getMailPop3Host() {
        return mailPop3Host;
    }

    public void setMailPop3Host(String mailPop3Host) {
        this.mailPop3Host = mailPop3Host;
    }

    public String getMailPop3Port() {
        return mailPop3Port;
    }

    public void setMailPop3Port(String mailPop3Port) {
        this.mailPop3Port = mailPop3Port;
    }

    public String getMailStoreProtocol() {
        return mailStoreProtocol;
    }

    public void setMailStoreProtocol(String mailStoreProtocol) {
        this.mailStoreProtocol = mailStoreProtocol;
    }

    public String getMailPop3Timeout() {
        return mailPop3Timeout;
    }

    public void setMailPop3Timeout(String mailPop3Timeout) {
        this.mailPop3Timeout = mailPop3Timeout;
    }

    public String getMailPop3SocketFactoryClass() {
        return mailPop3SocketFactoryClass;
    }

    public void setMailPop3SocketFactoryClass(String mailPop3SocketFactoryClass) {
        this.mailPop3SocketFactoryClass = mailPop3SocketFactoryClass;
    }

    public String getMailHost() {
        return mailHost;
    }

    public void setMailHost(String mailHost) {
        this.mailHost = mailHost;
    }

    public String getMailSmtpAuth() {
        return mailSmtpAuth;
    }

    public void setMailSmtpAuth(String mailSmtpAuth) {
        this.mailSmtpAuth = mailSmtpAuth;
    }

    public String getMailSmtpTimeout() {
        return mailSmtpTimeout;
    }

    public void setMailSmtpTimeout(String mailSmtpTimeout) {
        this.mailSmtpTimeout = mailSmtpTimeout;
    }

    public String getMailTransportProtocol() {
        return mailTransportProtocol;
    }

    public void setMailTransportProtocol(String mailTransportProtocol) {
        this.mailTransportProtocol = mailTransportProtocol;
    }

    public String getMailSmtpSocketFactoryClass() {
        return mailSmtpSocketFactoryClass;
    }

    public void setMailSmtpSocketFactoryClass(String mailSmtpSocketFactoryClass) {
        this.mailSmtpSocketFactoryClass = mailSmtpSocketFactoryClass;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getMailSmtpHost() {
        return mailSmtpHost;
    }

    public void setMailSmtpHost(String mailSmtpHost) {
        this.mailSmtpHost = mailSmtpHost;
    }

    public String getMailUsername() {
        return mailUsername;
    }

    public void setMailUsername(String mailUsername) {
        this.mailUsername = mailUsername;
    }

    public String getMailPassword() {
        return mailPassword;
    }

    public void setMailPassword(String mailPassword) {
        this.mailPassword = mailPassword;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

}
