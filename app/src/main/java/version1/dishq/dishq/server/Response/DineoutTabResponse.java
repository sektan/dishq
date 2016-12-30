package version1.dishq.dishq.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dishq on 30-12-2016.
 * Package name version1.dishq.dishq.
 */

public class DineoutTabResponse {

    @SerializedName("data")
    @Expose
    public DineoutRestaurants dineoutRestaurants;

    @SerializedName("response")
    @Expose
    public String dineoutResponse;

    public class DineoutRestaurants {

        @SerializedName("dineout_restaurants")
        @Expose
        public ArrayList<DineoutRestInfo> dineoutRestInfo;

        @SerializedName("has_more_results")
        @Expose
        public boolean hasMoreResults;

        public ArrayList<DineoutRestInfo> getDineoutRestInfo() {
            return dineoutRestInfo;
        }

        public void setDineoutRestInfo(ArrayList<DineoutRestInfo> dineoutRestInfo) {
            this.dineoutRestInfo = dineoutRestInfo;
        }

        public boolean isHasMoreResults() {
            return hasMoreResults;
        }

        public void setHasMoreResults(boolean hasMoreResults) {
            this.hasMoreResults = hasMoreResults;
        }
    }

    public class DineoutRestInfo {

        @SerializedName("price_level")
        @Expose
        public int dineoutPriceLvl;

        @SerializedName("drive_time")
        @Expose
        public String dineDriveTime;

        @SerializedName("photo")
        @Expose
        public ArrayList<String> dineRestPhoto;

        @SerializedName("restaurant_id")
        @Expose
        public int dineRestId;

        @SerializedName("restaurant_name")
        @Expose
        public String dineRestName;

        @SerializedName("cuisine_text")
        @Expose
        public ArrayList<String> dineCuisineText;

        @SerializedName("restaurant_address")
        @Expose
        public ArrayList<String> dineRestAddr;

        public ArrayList<String> getDineRestAddr() {
            return dineRestAddr;
        }

        public void setDineRestAddr(ArrayList<String> dineRestAddr) {
            this.dineRestAddr = dineRestAddr;
        }

        public ArrayList<String> getDineCuisineText() {
            return dineCuisineText;
        }

        public void setDineCuisineText(ArrayList<String> dineCuisineText) {
            this.dineCuisineText = dineCuisineText;
        }

        public String getDineRestName() {
            return dineRestName;
        }

        public void setDineRestName(String dineRestName) {
            this.dineRestName = dineRestName;
        }

        public int getDineRestId() {
            return dineRestId;
        }

        public void setDineRestId(int dineRestId) {
            this.dineRestId = dineRestId;
        }

        public ArrayList<String> getDineRestPhoto() {
            return dineRestPhoto;
        }

        public void setDineRestPhoto(ArrayList<String> dineRestPhoto) {
            this.dineRestPhoto = dineRestPhoto;
        }

        public String getDineDriveTime() {
            return dineDriveTime;
        }

        public void setDineDriveTime(String dineDriveTime) {
            this.dineDriveTime = dineDriveTime;
        }

        public int getDineoutPriceLvl() {
            return dineoutPriceLvl;
        }

        public void setDineoutPriceLvl(int dineoutPriceLvl) {
            this.dineoutPriceLvl = dineoutPriceLvl;
        }
    }
}
