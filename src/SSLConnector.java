/**
 * Created with IntelliJ IDEA.
 * User: pmos
 * Date: 1/11/16
 * Time: 12:02 PM
 */

import org.apache.commons.codec.binary.Base64;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SSLConnector {

    private final static Logger logger = Logger.getLogger(SSLConnector.class.getName());
    private final MyClass myClass;


    public SSLConnector(MyClass myClass) {
        this.myClass = myClass;
    }

    private  KeyStore getKeyStore() throws IOException {

        String keyStoreName = myClass.getSettings().getKeyStorePath();
        String keyStorePassword = myClass.getSettings().getKeyStorePassword();
        KeyStore ks = null;
        FileInputStream fis = null;
        try {
            ks = KeyStore.getInstance(SecurityConstants.KEYSTORE_TYPE);
            char[] passwordArray = keyStorePassword.toCharArray();
            fis = new java.io.FileInputStream(keyStoreName);
            ks.load(fis, passwordArray);
            fis.close();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unable to load keystore : {}" + keyStoreName);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

        return ks;
    }

    private  SSLSocketFactory getSSLSocketFactory(String keyStorePassword) throws
            UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException, IOException {

        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

        } };

        HostnameVerifier hv = new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        };

        HttpsURLConnection.setDefaultHostnameVerifier(hv);

        KeyStore ks = getKeyStore();
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(SecurityConstants.KEYMANAGER_ALGORITHM);
        keyManagerFactory.init(ks, keyStorePassword.toCharArray());
        SSLContext context = SSLContext.getInstance(SecurityConstants.SECURITY_PROTOCOL);
        context.init(keyManagerFactory.getKeyManagers(), trustAllCerts, new SecureRandom());

        return context.getSocketFactory();
    }


    private SSLSocketFactory getTrustedSSLSocketFactory(String keyStorePassword)
            throws UnrecoverableKeyException, KeyStoreException,
            NoSuchAlgorithmException, KeyManagementException, IOException {

        TrustManager trustManagers[] =  new TrustManager[1];
        CustomTrustManager trustManager =  new CustomTrustManager();
        trustManagers[0] = trustManager;

        KeyStore ks = getKeyStore();
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(SecurityConstants.KEYMANAGER_ALGORITHM);
        keyManagerFactory.init(ks, keyStorePassword.toCharArray());
        SSLContext context = SSLContext.getInstance(SecurityConstants.SECURITY_PROTOCOL);
        context.init(keyManagerFactory.getKeyManagers(), trustManagers, new SecureRandom());

        return context.getSocketFactory();
    }

    /*
    * rest call to the the services requiring ssl connection
    *
    * @param url - the url to query, e.g https://www.mywebiste.com/
    * @return - opened connection to the provided url
    * */
    public HttpsURLConnection getConnection(URL url) throws
            UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException {

        SSLSocketFactory sslFactory = null;
        if (myClass.getSettings().isCertificateVerificationEnabled()) {
            sslFactory = getTrustedSSLSocketFactory(myClass.getSettings().getKeyStorePassword());
        } else {
            sslFactory = getSSLSocketFactory(myClass.getSettings().getKeyStorePassword());
        }

        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setSSLSocketFactory(sslFactory);
        con.setRequestMethod("GET");
        con.addRequestProperty("Accept", SecurityConstants.HTTPS_RESPONSE_TYPE);

        return con;
    }

    /*
    * handy method for rest calls with basic authentication
    *
    * @param url - the url to query, e.g https://www.mywebiste.com/
    * @param username - your username
    * @param password - your password
    * @return - opened connection to the provided url
    * */
    public HttpsURLConnection getBasicConnection(URL url, String username, String password) throws
            UnrecoverableKeyException,
            KeyManagementException,
            KeyStoreException,
            NoSuchAlgorithmException,
            IOException {

        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        String auth = username + ":" + password;
        String authCrypt = new String(Base64.encodeBase64(auth.getBytes()));
        con.setRequestProperty("Authorization", "Basic " + authCrypt);

        return con;
    }

}