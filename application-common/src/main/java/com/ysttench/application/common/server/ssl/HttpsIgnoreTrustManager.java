package com.ysttench.application.common.server.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class HttpsIgnoreTrustManager implements X509TrustManager {

    private X509Certificate[] certificates;
    @Override
    public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
        if (this.certificates == null) {
            this.certificates = certificates;
        }
    }

    @Override
    public void checkServerTrusted(X509Certificate[] ax509certificates, String s) throws CertificateException {
        if (this.certificates == null) {
            this.certificates = ax509certificates;
        }
        
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

}
