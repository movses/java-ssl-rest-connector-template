/**
 * Created with IntelliJ IDEA.
 * User: pmos
 * Date: 1/11/16
 * Time: 12:50 PM
 */
public class MyClassSettings {

    private String keyStorePath;
    private String keyStorePassword;
    private boolean certificateVerificationEnabled = false;

    MyClassSettings() {}

    MyClassSettings(String keyStorePath, String keyStorePassword) {
        this.keyStorePath = keyStorePath;
        this.keyStorePassword = keyStorePassword;
    }

    MyClassSettings(String keyStorePath, String keyStorePassword, boolean certificateVerificationEnabled) {
        this.keyStorePath = keyStorePath;
        this.keyStorePassword = keyStorePassword;
        this.certificateVerificationEnabled = certificateVerificationEnabled;
    }


    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public boolean isCertificateVerificationEnabled() {
        return certificateVerificationEnabled;
    }

    public void setCertificateVerificationEnabled(boolean certificateVerificationEnabled) {
        this.certificateVerificationEnabled = certificateVerificationEnabled;
    }
}
