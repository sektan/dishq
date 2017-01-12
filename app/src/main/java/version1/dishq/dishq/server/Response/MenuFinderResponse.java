package version1.dishq.dishq.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kavin.prabhu on 09/01/17.
 */

public class MenuFinderResponse {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("response")
    @Expose
    private String response;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public class Datum {

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
        private List<String> photo = null;
        @SerializedName("restaurant_id")
        @Expose
        private Integer restaurantId;
        @SerializedName("restaurant_name")
        @Expose
        private String restaurantName;
        @SerializedName("cuisine_text")
        @Expose
        private List<String> cuisineText = null;
        @SerializedName("restaurant_address")
        @Expose
        private List<String> restaurantAddress = null;

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

        public List<String> getPhoto() {
            return photo;
        }

        public void setPhoto(List<String> photo) {
            this.photo = photo;
        }

        public Integer getRestaurantId() {
            return restaurantId;
        }

        public void setRestaurantId(Integer restaurantId) {
            this.restaurantId = restaurantId;
        }

        public String getRestaurantName() {
            return restaurantName;
        }

        public void setRestaurantName(String restaurantName) {
            this.restaurantName = restaurantName;
        }

        public List<String> getCuisineText() {
            return cuisineText;
        }

        public void setCuisineText(List<String> cuisineText) {
            this.cuisineText = cuisineText;
        }

        public List<String> getRestaurantAddress() {
            return restaurantAddress;
        }

        public void setRestaurantAddress(List<String> restaurantAddress) {
            this.restaurantAddress = restaurantAddress;
        }

    }

}
