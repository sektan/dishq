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
        @SerializedName("delivery_time")
        @Expose
        String nbDeliveryTime;

        @SerializedName("is_open_now")
        @Expose
        Boolean nbIsOpenNow;

        @SerializedName("has_home_delivery")
        @Expose
        Boolean nbHasHomeDelivery;

        @SerializedName("can_be_delivered")
        @Expose
        Boolean nbCanBeDelivered;

        @SerializedName("price_level")
        @Expose
        private int priceLevel;
        @SerializedName("photo")
        @Expose
        private ArrayList<String> nearByRestPhoto;
        @SerializedName("restaurant_id")
        @Expose
        private int nearByRestId;
        @SerializedName("restaurant_name")
        @Expose
        private String nearByRestName;
        @SerializedName("cuisine_text")
        @Expose
        private ArrayList<String> nearByRestCuisineText;

        @SerializedName("restaurant_address")
        @Expose
        private ArrayList<String> nearByRestAddress;

        @SerializedName("restaurant_type_text")
        @Expose
        private ArrayList<String> nearByRestTypeText;


        public String getNbDeliveryTime() {
            return nbDeliveryTime;
        }

        public void setNbDeliveryTime(String nbDeliveryTime) {
            this.nbDeliveryTime = nbDeliveryTime;
        }

        public Boolean getNbIsOpenNow() {
            return nbIsOpenNow;
        }

        public void setNbIsOpenNow(Boolean nbIsOpenNow) {
            this.nbIsOpenNow = nbIsOpenNow;
        }

        public Boolean getNbHasHomeDelivery() {
            return nbHasHomeDelivery;
        }

        public void setNbHasHomeDelivery(Boolean nbHasHomeDelivery) {
            this.nbHasHomeDelivery = nbHasHomeDelivery;
        }

        public Boolean getNbCanBeDelivered() {
            return nbCanBeDelivered;
        }

        public void setNbCanBeDelivered(Boolean nbCanBeDelivered) {
            this.nbCanBeDelivered = nbCanBeDelivered;
        }



        public ArrayList<String> getNearByRestTypeText() {
            return nearByRestTypeText;
        }

        public void setNearByRestTypeText(ArrayList<String> nearByRestTypeText) {
            this.nearByRestTypeText = nearByRestTypeText;
        }

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

        public int getPriceLevel() {
            return priceLevel;
        }

        public void setPriceLevel(int priceLevel) {
            this.priceLevel = priceLevel;
        }

        public ArrayList<String> getNearByRestPhoto() {
            return nearByRestPhoto;
        }

        public void setNearByRestPhoto(ArrayList<String> nearByRestPhoto) {
            this.nearByRestPhoto = nearByRestPhoto;
        }

        public int getNearByRestId() {
            return nearByRestId;
        }

        public void setNearByRestId(int nearByRestId) {
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
