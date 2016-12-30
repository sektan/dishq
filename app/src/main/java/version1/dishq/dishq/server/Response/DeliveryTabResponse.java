package version1.dishq.dishq.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dishq on 30-12-2016.
 * Package name version1.dishq.dishq.
 */

public class DeliveryTabResponse {

    @SerializedName("data")
    @Expose
    public DeliveryRestaurants deliveryRestaurants;

    @SerializedName("response")
    @Expose
    public String deliveryResponse;

    public class DeliveryRestaurants {

        @SerializedName("delivery_restaurants")
        @Expose
        public ArrayList<DeliveryRestInfo> deliveryRestInfo;

        @SerializedName("has_more_results")
        @Expose
        public boolean hasMoreResults;

        public ArrayList<DeliveryRestInfo> getDeliveryRestInfo() {
            return deliveryRestInfo;
        }

        public void setDeliveryRestInfo(ArrayList<DeliveryRestInfo> deliveryRestInfo) {
            this.deliveryRestInfo = deliveryRestInfo;
        }

        public boolean isHasMoreResults() {
            return hasMoreResults;
        }

        public void setHasMoreResults(boolean hasMoreResults) {
            this.hasMoreResults = hasMoreResults;
        }
    }

    public class DeliveryRestInfo {

        @SerializedName("delivery_time")
        @Expose
        public String deliveryTime;

        @SerializedName("price_level")
        @Expose
        public int delivPriceLvl;

        @SerializedName("restaurant_id")
        @Expose
        public int delivRestId;

        @SerializedName("dish_data")
        @Expose
        public ArrayList<DeliveryDishData> deliveryDishDatas;

        @SerializedName("restaurant_name")
        @Expose
        public String delivRestName;

        @SerializedName("cuisine_text")
        @Expose
        public ArrayList<String> delivCuisineText;

        public String getDeliveryTime() {
            return deliveryTime;
        }

        public void setDeliveryTime(String deliveryTime) {
            this.deliveryTime = deliveryTime;
        }

        public int getDelivPriceLvl() {
            return delivPriceLvl;
        }

        public void setDelivPriceLvl(int delivPriceLvl) {
            this.delivPriceLvl = delivPriceLvl;
        }

        public int getDelivRestId() {
            return delivRestId;
        }

        public void setDelivRestId(int delivRestId) {
            this.delivRestId = delivRestId;
        }

        public ArrayList<DeliveryDishData> getDeliveryDishDatas() {
            return deliveryDishDatas;
        }

        public void setDeliveryDishDatas(ArrayList<DeliveryDishData> deliveryDishDatas) {
            this.deliveryDishDatas = deliveryDishDatas;
        }

        public String getDelivRestName() {
            return delivRestName;
        }

        public void setDelivRestName(String delivRestName) {
            this.delivRestName = delivRestName;
        }

        public ArrayList<String> getDelivCuisineText() {
            return delivCuisineText;
        }

        public void setDelivCuisineText(ArrayList<String> delivCuisineText) {
            this.delivCuisineText = delivCuisineText;
        }
    }

    public class DeliveryDishData {

        @SerializedName("photo")
        @Expose
        public ArrayList<String> delivPhoto;

        @SerializedName("dish_id")
        @Expose
        public int delivDishId;

        public ArrayList<String> getDelivPhoto() {
            return delivPhoto;
        }

        public void setDelivPhoto(ArrayList<String> delivPhoto) {
            this.delivPhoto = delivPhoto;
        }

        public int getDelivDishId() {
            return delivDishId;
        }

        public void setDelivDishId(int delivDishId) {
            this.delivDishId = delivDishId;
        }
    }
}
