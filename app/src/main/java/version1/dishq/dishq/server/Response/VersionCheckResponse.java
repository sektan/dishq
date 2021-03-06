package version1.dishq.dishq.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dishq on 03-11-2016.
 * Package name version1.dishq.dishq.
 */

public class VersionCheckResponse {

    @SerializedName("response")
    @Expose
    public String response;

    @SerializedName("data")
    @Expose
    public VersionCheckData versionCheckData;

    public class VersionCheckData {
        @SerializedName("version_number")
        @Expose
        public String lVersionNumber;
        @SerializedName("show_update_popup")
        @Expose
        public boolean showUpdatePopup;
        @SerializedName("version_name")
        @Expose
        public String lVersionName;
        @SerializedName("do_force_update")
        @Expose
        public boolean doForceUpdate;

        public String getlVersionNumber() {
           return lVersionNumber;
        }

        public void setlVersionNumber(String lVersionNumber) {
            this.lVersionNumber = lVersionNumber;
        }

        public boolean getShowUpdatePopup() {
            return showUpdatePopup;
        }

        public void setShowUpdatePopup(Boolean showUpdatePopup) {
            this.showUpdatePopup = showUpdatePopup;
        }

        public String getlVersionName() {
            return lVersionName;
        }

        public void setlVersionName(String lVersionName) {
            this.lVersionName = lVersionName;
        }

        public Boolean getDoForceUpdate() {
            return doForceUpdate;
        }

        public void setDoForceUpdate(Boolean doForceUpdate) {
            this.doForceUpdate = doForceUpdate;
        }
    }
}
