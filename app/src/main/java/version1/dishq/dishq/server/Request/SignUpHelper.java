package version1.dishq.dishq.server.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by dishq on 02-11-2016.
 * Package name version1.dishq.dishq.
 */

public class SignUpHelper {
    @SerializedName("grant_type")
    @Expose
    private String grantType;

    @SerializedName("backend")
    @Expose
    private String backendAuth;

    @SerializedName("client_id")
    @Expose
    private String clientId;

    @SerializedName("client_secret")
    @Expose
    private String clientSecret;

    @SerializedName("uid")
    @Expose
    private String uniqueIdentifier;

    @SerializedName("gcm_device_registration_id")
    @Expose
    private String gcmDeviceRegId;

    @SerializedName("token")
    @Expose
    private String accessToken;

    //Constructor for the class
    public SignUpHelper(String grantType, String backendAuth, String clientId, String clientSecret, String uniqueIdentifier, String gcmDeviceRegId, String accessToken){
        this.grantType = grantType;
        this.backendAuth = backendAuth;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.uniqueIdentifier = uniqueIdentifier;
        this.gcmDeviceRegId = gcmDeviceRegId;
        this.accessToken = accessToken;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getBackendAuth() {
        return backendAuth;
    }

    public void setBackendAuth(String backendAuth) {
        this.backendAuth = backendAuth;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGcmDeviceRegId() {
        return gcmDeviceRegId;
    }

    public void setGcmDeviceRegId(String gcmDeviceRegId) {
        this.gcmDeviceRegId = gcmDeviceRegId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}