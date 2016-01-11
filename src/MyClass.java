/**
 * Created with IntelliJ IDEA.
 * User: pmos
 * Date: 1/11/16
 * Time: 12:44 PM
 */
public class MyClass {

    private MyClassSettings settings;
    private SSLConnector sslConnector;

    public MyClass () {}

    public MyClass(MyClassSettings settings) {
        this.settings = settings;
    }

    public MyClassSettings getSettings() {
        return settings;
    }

    public void setSettings(MyClassSettings settings) {
        this.settings = settings;
    }

    public SSLConnector getSSLConnector() {
        return sslConnector;
    }

    public void setSSLConnector(SSLConnector sslConnector) {
        this.sslConnector = sslConnector;
    }

}
