package version1.dishq.dishq.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dishq on 02-11-2016.
 * Package name version1.dishq.dishq.
 */

public class SignUpResponse {

    @SerializedName("response")
    @Expose
    private String signupResponse;

    @SerializedName("data")
    @Expose
    public NewUserDataInfo newUserDataInfo;

    @SerializedName("message")
    @Expose
    private String newUserMessage;

    public class NewUserDataInfo {
        @SerializedName("is_new_user")
        @Expose
        private Boolean isNewUser;

        @SerializedName("access_token")
        @Expose
        private String accessToken;

        @SerializedName("expires_in")
        @Expose
        private int expiresIn;

        @SerializedName("user_data")
        @Expose
        public UserDataInfo userDataInfo;

        @SerializedName("token_type")
        @Expose
        private String tokenType;

        @SerializedName("refresh_token")
        @Expose
        private String refreshToken;

        @SerializedName("scope")
        @Expose
        private String responseScope;

        public Boolean getIsNewUser() {
            return isNewUser;
        }

        public void setIsNewUser(Boolean isNewUser) {
            this.isNewUser = isNewUser;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public int getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(int expiresIn) {
            this.expiresIn = expiresIn;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getResponseScope() {
            return responseScope;
        }

        public void setResponseScope(String responseScope) {
            this.responseScope = responseScope;
        }
    }

    public class UserDataInfo {
        @SerializedName("user_id")
        @Expose
        private int userId;

        @SerializedName("firstname")
        @Expose
        private String firstName;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
    }
}
