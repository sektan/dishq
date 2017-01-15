package version1.dishq.dishq.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dishq on 12-01-2017.
 * Package name version1.dishq.dishq.
 */

public class MenuFinderRestSuggestResponse {

    @SerializedName("data")
    @Expose
    public ArrayList<MenuFinderRestInfo> menuFinderRestInfos;

    @SerializedName("response")
    @Expose
    public String mfResponse;

    public class MenuFinderRestInfo {

        @SerializedName("restaurant_lat_lon")
        @Expose
        private String mfRestLatLon;

        @SerializedName("cost_for_two")
        @Expose
        private int mfCostForTwo;

        @SerializedName("restaurant_id")
        @Expose
        private int mfRestId;

        @SerializedName("open_now")
        @Expose
        private Boolean mfOpenNow;

        @SerializedName("price_level")
        @Expose
        private int mfPriceLvl;

        @SerializedName("drive_time")
        @Expose
        private String mfDriveTime;

        @SerializedName("restaurant_name")
        @Expose
        private String mfRestName;

        @SerializedName("restaurant_address")
        @Expose
        private ArrayList<String> mfRestAddr;

        @SerializedName("photo_thumbnail_menu_finder_url")
        @Expose
        private ArrayList<String> mfPhotoThumbnail;

        @SerializedName("cuisine_text")
        @Expose
        private ArrayList<String> mfRestCuisineText;

        public String getMfRestLatLon() {
            return mfRestLatLon;
        }

        public void setMfRestLatLon(String mfRestLatLon) {
            this.mfRestLatLon = mfRestLatLon;
        }

        public int getMfCostForTwo() {
            return mfCostForTwo;
        }

        public void setMfCostForTwo(int mfCostForTwo) {
            this.mfCostForTwo = mfCostForTwo;
        }

        public int getMfRestId() {
            return mfRestId;
        }

        public void setMfRestId(int mfRestId) {
            this.mfRestId = mfRestId;
        }

        public Boolean getMfOpenNow() {
            return mfOpenNow;
        }

        public void setMfOpenNow(Boolean mfOpenNow) {
            this.mfOpenNow = mfOpenNow;
        }

        public String getMfRestName() {
            return mfRestName;
        }

        public void setMfRestName(String mfRestName) {
            this.mfRestName = mfRestName;
        }

        public ArrayList<String> getMfPhotoThumbnail() {
            return mfPhotoThumbnail;
        }

        public void setMfPhotoThumbnail(ArrayList<String> mfPhotoThumbnail) {
            this.mfPhotoThumbnail = mfPhotoThumbnail;
        }

        public ArrayList<String> getMfRestCuisineText() {
            return mfRestCuisineText;
        }

        public void setMfRestCuisineText(ArrayList<String> mfRestCuisineText) {
            this.mfRestCuisineText = mfRestCuisineText;
        }


        public ArrayList<String> getMfRestAddr() {
            return mfRestAddr;
        }

        public void setMfRestAddr(ArrayList<String> mfRestAddr) {
            this.mfRestAddr = mfRestAddr;
        }

        public int getMfPriceLvl() {
            return mfPriceLvl;
        }

        public void setMfPriceLvl(int mfPriceLvl) {
            this.mfPriceLvl = mfPriceLvl;
        }

        public String getMfDriveTime() {
            return mfDriveTime;
        }

        public void setMfDriveTime(String mfDriveTime) {
            this.mfDriveTime = mfDriveTime;
        }
    }
}
