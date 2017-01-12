package version1.dishq.dishq.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dishq on 12-01-2017.
 * Package name version1.dishq.dishq.
 */

public class MenuFinderNearbyRestResponse {

    @SerializedName("data")
    @Expose
    public ArrayList<NearbyRestInfo> nearbyRestInfos;

    @SerializedName("response")
    @Expose
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public class NearbyRestInfo {

        @SerializedName("restaurant_lat_lon")
        @Expose
        private String restaurantLatLon;
        @SerializedName("drive_time")
        @Expose
        private String driveTime;
        @SerializedName("price_level")
        @Expose
        private Integer priceLevel;
        @SerializedName("photo")
        @Expose
        private ArrayList<String> nearByRestPhoto;
        @SerializedName("restaurant_id")
        @Expose
        private Integer nearByRestId;
        @SerializedName("restaurant_name")
        @Expose
        private String nearByRestName;
        @SerializedName("cuisine_text")
        @Expose
        private ArrayList<String> nearByRestCuisineText;

        @SerializedName("restaurant_address")
        @Expose
        private ArrayList<String> nearByRestAddress;

        public String getRestaurantLatLon() {
            return restaurantLatLon;
        }

        public void setRestaurantLatLon(String restaurantLatLon) {
            this.restaurantLatLon = restaurantLatLon;
        }

        public String getDriveTime() {
            return driveTime;
        }

        public void setDriveTime(String driveTime) {
            this.driveTime = driveTime;
        }

        public Integer getPriceLevel() {
            return priceLevel;
        }

        public void setPriceLevel(Integer priceLevel) {
            this.priceLevel = priceLevel;
        }

        public ArrayList<String> getNearByRestPhoto() {
            return nearByRestPhoto;
        }

        public void setNearByRestPhoto(ArrayList<String> nearByRestPhoto) {
            this.nearByRestPhoto = nearByRestPhoto;
        }

        public Integer getNearByRestId() {
            return nearByRestId;
        }

        public void setNearByRestId(Integer nearByRestId) {
            this.nearByRestId = nearByRestId;
        }

        public String getNearByRestName() {
            return nearByRestName;
        }

        public void setNearByRestName(String nearByRestName) {
            this.nearByRestName = nearByRestName;
        }

        public ArrayList<String> getNearByRestCuisineText() {
            return nearByRestCuisineText;
        }

        public void setNearByRestCuisineText(ArrayList<String> nearByRestCuisineText) {
            this.nearByRestCuisineText = nearByRestCuisineText;
        }

        public ArrayList<String> getNearByRestAddress() {
            return nearByRestAddress;
        }

        public void setNearByRestAddress(ArrayList<String> nearByRestAddress) {
            this.nearByRestAddress = nearByRestAddress;
        }
    }

}
