/*
 * KiFtpClient.java
 * 
 * FTPを処理する用クライアント.
 * 
 * 2008/12/14／version 1.0 ／楊飛／全体作成
 * Copyright(c) 2008 Mitsubishi Electric Corporation.
 */
package com.ysttench.application.common.server;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.ysttench.application.common.exception.SystemException;


public class FtpClient {

    private FTPClient client;

    private String host;

    private int port;

    private String username;

    private String password;

    private String encoding;

    private int timeout;

    /**
     * Timeoutを設定する
     * 
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * FTPクライアント用ホストを設定する
     * 
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * FTPクライアント用portを設定する
     * 
     * @param port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * FTPクライアント用ユーザ名を設定する
     * 
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * FTPクライアント用パスワードを設定する
     * 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * FTPクライアント用encodingを設定する
     * 
     * @param encoding
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * FTPクライアントを初期化する用メソッド
     */
    public void initialize() {
        try {
            this.client = new FTPClient();
            this.client.setControlEncoding(this.encoding);
            this.client.setDataTimeout(this.timeout * 1000);
            this.client.connect(this.host, this.port);
            int reply = this.client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                StringBuilder builder = new StringBuilder();
                builder.append("The host \"");
                builder.append(host);
                builder.append("\" with port \"");
                builder.append(port);
                builder.append("\" can not be connected!");
                throw new SystemException(builder.toString());
            }
            if (!this.client.login(this.username, this.password)) {
                StringBuilder builder = new StringBuilder();
                builder.append("The user \"");
                builder.append(this.username);
                builder.append("\" is not permitted login the host \"");
                builder.append(this.host);
                builder.append("\"! May be the password is wrong!");
                throw new SystemException(builder.toString());
            }

        } catch (SystemException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SystemException(ex);
        }
    }

    /**
     * FTPクライアントを破棄する
     */
    public void destroy() {
        if (this.client != null) {
            try {
                if (client.isConnected()) {
                    this.client.disconnect();
                }
            } catch (Exception ex) {
                // do nothing
            }

        }
    }

    public String[] testGetListName() {
        try {
            return this.client.listNames();
        } catch (IOException ex) {
            throw new SystemException(ex);
        }
    }

}
