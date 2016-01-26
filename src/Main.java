/**
 * Created with IntelliJ IDEA.
 * User: pmos
 * Date: 1/11/16
 * Time: 2:50 PM
 */

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

public class Main {

    MyClass myClass;

    @Before()
    public void before() {
        myClass = new MyClass(new MyClassSettings("myKeystorePath","myKeystorePassword"));
        SSLConnector connector = new SSLConnector(myClass);
        myClass.setSSLConnector(connector);
    }

    @Test
    public void testConnection(MyClass myClass) throws
                AbstractMethodError,
                IOException,
                UnrecoverableKeyException,
                NoSuchAlgorithmException,
                KeyStoreException,
                KeyManagementException {

        boolean result = false;
        String url = MyClassConstants.BASE_URI + "/" + MyClassConstants.VERSION;

        int responseCode = myClass.getSSLConnector().getConnection(new URL(url)).getResponseCode();
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            result = true;
        }

        Assert.assertTrue(result);
    }

    @Test
    public void testBasicConnection(MyClass myClass) throws
                    AbstractMethodError,
                    IOException,
                    UnrecoverableKeyException,
                    NoSuchAlgorithmException,
                    KeyStoreException,
                    KeyManagementException {
       
        boolean result = false;
        String url = MyClassConstants.BASE_URI + "/" + MyClassConstants.VERSION;
        int responseCode = myClass.getSSLConnector()
                .getBasicConnection(new URL(url), "myusername", "mypassword")
                .getResponseCode();
        if(responseCode == HttpsURLConnection.HTTP_OK) {
            result = true;
        }
        Assert.assertTrue(result);
    }

}
