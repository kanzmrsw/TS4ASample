package com.kanzmrsw.example.ts4asample;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

public class ModHttpClient extends DefaultHttpClient {
    public ModHttpClient() {
        super();
        HttpConnectionParams.setConnectionTimeout(this.getParams(), 15000);
    }

    public ModHttpClient(Credentials cred) {
        super();
        setCredentials(cred);
        HttpConnectionParams.setConnectionTimeout(this.getParams(), 15000);
    }

    public ModHttpClient(Credentials cred, int port)
            throws KeyManagementException, NoSuchAlgorithmException,
            KeyStoreException, UnrecoverableKeyException {
        super();
        registerTrustAllScheme(port);
        setCredentials(cred);
    }

    public ModHttpClient(int port) throws KeyManagementException,
            NoSuchAlgorithmException, KeyStoreException,
            UnrecoverableKeyException {
        super();
        registerTrustAllScheme(port);
    }

    private void registerTrustAllScheme(int port)
            throws KeyManagementException, NoSuchAlgorithmException,
            KeyStoreException, UnrecoverableKeyException {

        TrustAllSSLSocketFactory tasslf = new TrustAllSSLSocketFactory();
        Scheme sch = new Scheme("https", tasslf, port);
        getConnectionManager().getSchemeRegistry().register(sch);
    }

    private void setCredentials(Credentials cred) {
        BasicCredentialsProvider cP = new BasicCredentialsProvider();
        cP.setCredentials(AuthScope.ANY, cred);
        setCredentialsProvider(cP);
    }
}
