package version1.dishq.dishq.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dishq on 09-01-2017.
 * Package name version1.dishq.dishq.
 */

public class DineoutMenuResponse {

    @SerializedName("data")
    @Expose
    public DineoutMenuInfo dineoutMenuInfo;

    @SerializedName("response")
    @Expose
    private String dineoutMenuResponse;

    public class DineoutMenuInfo {

        @SerializedName("menu_data")
        @Expose
        public ArrayList<DineoutMenuData> dineoutMenuDatas;

        @SerializedName("restaurant_data")
        @Expose
        public DineoutRestData dineoutRestData;
    }

    public class DineoutMenuData {

        @SerializedName("photo_popup")
        @Expose
        private ArrayList<String> dineMenuPhotoPopup;

        @SerializedName("has_alcohol")
        @Expose
        private Boolean dineMenuHasAlcohol;

        @SerializedName("is_veg")
        @Expose
        private Boolean dineMenuIsVeg;

        @SerializedName("tags")
        @Expose
        private ArrayList<String> dineMenuTags;

        @SerializedName("photo")
        @Expose
        public ArrayList<String> dineMenuPhoto;

        @SerializedName("price")
        @Expose
        private String dineMenuDishPrice;

        @SerializedName("generic_dish_id")
        @Expose
        private int dineMenuGenericDishId;

        @SerializedName("dish_nature")
        @Expose
        private int dineMenuDishNature;

        @SerializedName("has_egg")
        @Expose
        private Boolean dineMenuHasEgg;

        @SerializedName("dish_name")
        @Expose
        private String dineMenuDishName;

        @SerializedName("is_spicy")
        @Expose
        private Boolean dineMenuIsSpicy;

        @SerializedName("is_added_to_favourite")
        @Expose
        private Boolean isAddedToFavdine;

        @SerializedName("cuisine_text")
        @Expose
        private ArrayList<String> dineMenuCuisineText;

        @SerializedName("dish_type_text")
        @Expose
        private String dineMenuDishType;


        public Boolean getAddedToFavdine() {
            return isAddedToFavdine;
        }

        public void setAddedToFavdine(Boolean addedToFavdine) {
            isAddedToFavdine = addedToFavdine;
        }

        public ArrayList<String> getDineMenuPhotoPopup() {
            return dineMenuPhotoPopup;
        }

        public void setDineMenuPhotoPopup(ArrayList<String> dineMenuPhotoPopup) {
            this.dineMenuPhotoPopup = dineMenuPhotoPopup;
        }

        public Boolean getDineMenuHasAlcohol() {
            return dineMenuHasAlcohol;
        }

        public void setDineMenuHasAlcohol(Boolean dineMenuHasAlcohol) {
            this.dineMenuHasAlcohol = dineMenuHasAlcohol;
        }

        public Boolean getDineMenuIsVeg() {
            return dineMenuIsVeg;
        }

        public void setDineMenuIsVeg(Boolean dineMenuIsVeg) {
            this.dineMenuIsVeg = dineMenuIsVeg;
        }

        public ArrayList<String> getDineMenuTags() {
            return dineMenuTags;
        }

        public void setDineMenuTags(ArrayList<String> dineMenuTags) {
            this.dineMenuTags = dineMenuTags;
        }

        public ArrayList<String> getDineMenuPhoto() {
            return dineMenuPhoto;
        }

        public void setDineMenuPhoto(ArrayList<String> dineMenuPhoto) {
            this.dineMenuPhoto = dineMenuPhoto;
        }

        public String getDineMenuDishPrice() {
            return dineMenuDishPrice;
        }

        public void setDineMenuDishPrice(String dineMenuDishPrice) {
            this.dineMenuDishPrice = dineMenuDishPrice;
        }

        public int getDineMenuGenericDishId() {
            return dineMenuGenericDishId;
        }

        public void setDineMenuGenericDishId(int dineMenuGenericDishId) {
            this.dineMenuGenericDishId = dineMenuGenericDishId;
        }

        public int getDineMenuDishNature() {
            return dineMenuDishNature;
        }

        public void setDineMenuDishNature(int dineMenuDishNature) {
            this.dineMenuDishNature = dineMenuDishNature;
        }

        public Boolean getDineMenuHasEgg() {
            return dineMenuHasEgg;
        }

        public void setDineMenuHasEgg(Boolean dineMenuHasEgg) {
            this.dineMenuHasEgg = dineMenuHasEgg;
        }

        public String getDineMenuDishName() {
            return dineMenuDishName;
        }

        public void setDineMenuDishName(String dineMenuDishName) {
            this.dineMenuDishName = dineMenuDishName;
        }

        public Boolean getDineMenuIsSpicy() {
            return dineMenuIsSpicy;
        }

        public void setDineMenuIsSpicy(Boolean dineMenuIsSpicy) {
            this.dineMenuIsSpicy = dineMenuIsSpicy;
        }

        public ArrayList<String> getDineMenuCuisineText() {
            return dineMenuCuisineText;
        }

        public void setDineMenuCuisineText(ArrayList<String> dineMenuCuisineText) {
            this.dineMenuCuisineText = dineMenuCuisineText;
        }

        public String getDineMenuDishType() {
            return dineMenuDishType;
        }

        public void setDineMenuDishType(String dineMenuDishType) {
            this.dineMenuDishType = dineMenuDishType;
        }
    }

    public class DineoutRestData {

        @SerializedName("restaurant_lat_lon")
        @Expose
        private String dineMenuRestLatLong;

        @SerializedName("drive_time")
        @Expose
        private String dineMenuDriveTime;

        @SerializedName("has_home_delivery")
        @Expose
        private Boolean hasHomeDelivery;

        @SerializedName("restaurant_type_text")
        @Expose
        private ArrayList<String> dineMenuRestTypeText;

        @SerializedName("price_level")
        @Expose
        private int dineMenuPriceLvl;

        @SerializedName("photo")
        @Expose
        private ArrayList<String> dineRestPhoto;

        @SerializedName("restaurant_id")
        @Expose
        private int dineMenuRestId;

        @SerializedName("restaurant_tags")
        @Expose
        private ArrayList<String> dineMenuRestTags;

        @SerializedName("restaurant_name")
        @Expose
        private String dineMenuRestName;

        @SerializedName("contact_numbers")
        @Expose
        private ArrayList<String> dineMenuRestContNo;

        @SerializedName("cuisine_text")
        @Expose
        private ArrayList<String> dineMenuCusineText;

        @SerializedName("food_tags")
        @Expose
        private ArrayList<String> restFoodTags;

        @SerializedName("restaurant_address")
        @Expose
        private ArrayList<String> dineMenuRestAddr;

        @SerializedName("swiggy_delivery_url")
        @Expose
        private String delMenuSwiggyUrl;

        @SerializedName("zomato_delivery_url")
        @Expose
        private String delMenuZomatoUrl;

        @SerializedName("runnr_delivery_url")
        @Expose
        private String delMenuRunnrUrl;

        @SerializedName("foodpanda_delivery_url")
        @Expose
        private String delMenuFoodPandaUrl;

        @SerializedName("dineout_tablebooking_url")
        @Expose
        private String tbMenuDineoutUrl;

        @SerializedName("zomato_tablebooking_url")
        @Expose
        private String tbMenuZomatoUrl;

        @SerializedName("easydiner_tablebooking_url")
        @Expose
        private String tbMenuEazydinerUrl;

        @SerializedName("ambience_text")
        @Expose
        private ArrayList<String> menuAmbienceText;

        @SerializedName("has_table_booking")
        @Expose
        private Boolean hasTableBooking;

        @SerializedName("can_be_delivered")
        @Expose
        private Boolean canBeDelivered;

        @SerializedName("delivery_time")
        @Expose
        private String menuDeliveryTime;

        @SerializedName("is_open_now")
        @Expose
        private Boolean isOpenNow;

        public Boolean getOpenNow() {
            return isOpenNow;
        }

        public void setOpenNow(Boolean openNow) {
            isOpenNow = openNow;
        }

        public ArrayList<String> getRestFoodTags() {
            return restFoodTags;
        }

        public void setRestFoodTags(ArrayList<String> restFoodTags) {
            this.restFoodTags = restFoodTags;
        }

        public String getMenuDeliveryTime() {
            return menuDeliveryTime;
        }

        public void setMenuDeliveryTime(String menuDeliveryTime) {
            this.menuDeliveryTime = menuDeliveryTime;
        }

        public String getDelMenuSwiggyUrl() {
            return delMenuSwiggyUrl;
        }

        public void setDelMenuSwiggyUrl(String delMenuSwiggyUrl) {
            this.delMenuSwiggyUrl = delMenuSwiggyUrl;
        }

        public String getDelMenuZomatoUrl() {
            return delMenuZomatoUrl;
        }

        public void setDelMenuZomatoUrl(String delMenuZomatoUrl) {
            this.delMenuZomatoUrl = delMenuZomatoUrl;
        }

        public String getDelMenuRunnrUrl() {
            return delMenuRunnrUrl;
        }

        public void setDelMenuRunnrUrl(String delMenuRunnrUrl) {
            this.delMenuRunnrUrl = delMenuRunnrUrl;
        }

        public String getDelMenuFoodPandaUrl() {
            return delMenuFoodPandaUrl;
        }

        public void setDelMenuFoodPandaUrl(String delMenuFoodPandaUrl) {
            this.delMenuFoodPandaUrl = delMenuFoodPandaUrl;
        }

        public String getTbMenuDineoutUrl() {
            return tbMenuDineoutUrl;
        }

        public void setTbMenuDineoutUrl(String tbMenuDineoutUrl) {
            this.tbMenuDineoutUrl = tbMenuDineoutUrl;
        }

        public String getTbMenuZomatoUrl() {
            return tbMenuZomatoUrl;
        }

        public void setTbMenuZomatoUrl(String tbMenuZomatoUrl) {
            this.tbMenuZomatoUrl = tbMenuZomatoUrl;
        }

        public String getTbMenuEazydinerUrl() {
            return tbMenuEazydinerUrl;
        }

        public void setTbMenuEazydinerUrl(String tbMenuEazydinerUrl) {
            this.tbMenuEazydinerUrl = tbMenuEazydinerUrl;
        }

        public ArrayList<String> getMenuAmbienceText() {
            return menuAmbienceText;
        }

        public void setMenuAmbienceText(ArrayList<String> menuAmbienceText) {
            this.menuAmbienceText = menuAmbienceText;
        }

        public Boolean getHasTableBooking() {
            return hasTableBooking;
        }

        public void setHasTableBooking(Boolean hasTableBooking) {
            this.hasTableBooking = hasTableBooking;
        }

        public Boolean getCanBeDelivered() {
            return canBeDelivered;
        }

        public void setCanBeDelivered(Boolean canBeDelivered) {
            this.canBeDelivered = canBeDelivered;
        }

        public ArrayList<String> getDineMenuRestTags() {
            return dineMenuRestTags;
        }

        public void setDineMenuRestTags(ArrayList<String> dineMenuRestTags) {
            this.dineMenuRestTags = dineMenuRestTags;
        }

        public ArrayList<String> getDineMenuRestContNo() {
            return dineMenuRestContNo;
        }

        public void setDineMenuRestContNo(ArrayList<String> dineMenuRestContNo) {
            this.dineMenuRestContNo = dineMenuRestContNo;
        }

        public String getDineMenuRestLatLong() {
            return dineMenuRestLatLong;
        }

        public void setDineMenuRestLatLong(String dineMenuRestLatLong) {
            this.dineMenuRestLatLong = dineMenuRestLatLong;
        }

        public Boolean getHasHomeDelivery() {
            return hasHomeDelivery;
        }

        public void setHasHomeDelivery(Boolean hasHomeDelivery) {
            this.hasHomeDelivery = hasHomeDelivery;
        }

        public String getDineMenuDriveTime() {
            return dineMenuDriveTime;
        }

        public void setDineMenuDriveTime(String dineMenuDriveTime) {
            this.dineMenuDriveTime = dineMenuDriveTime;
        }

        public int getDineMenuPriceLvl() {
            return dineMenuPriceLvl;
        }

        public void setDineMenuPriceLvl(int dineMenuPriceLvl) {
            this.dineMenuPriceLvl = dineMenuPriceLvl;
        }

        public ArrayList<String> getDineMenuRestTypeText() {
            return dineMenuRestTypeText;
        }

        public void setDineMenuRestTypeText(ArrayList<String> dineMenuRestTypeText) {
            this.dineMenuRestTypeText = dineMenuRestTypeText;
        }

        public ArrayList<String> getDineRestPhoto() {
            return dineRestPhoto;
        }

        public void setDineRestPhoto(ArrayList<String> dineRestPhoto) {
            this.dineRestPhoto = dineRestPhoto;
        }

        public int getDineMenuRestId() {
            return dineMenuRestId;
        }

        public void setDineMenuRestId(int dineMenuRestId) {
            this.dineMenuRestId = dineMenuRestId;
        }

        public String getDineMenuRestName() {
            return dineMenuRestName;
        }

        public void setDineMenuRestName(String dineMenuRestName) {
            this.dineMenuRestName = dineMenuRestName;
        }

        public ArrayList<String> getDineMenuCusineText() {
            return dineMenuCusineText;
        }

        public void setDineMenuCusineText(ArrayList<String> dineMenuCusineText) {
            this.dineMenuCusineText = dineMenuCusineText;
        }

        public ArrayList<String> getDineMenuRestAddr() {
            return dineMenuRestAddr;
        }

        public void setDineMenuRestAddr(ArrayList<String> dineMenuRestAddr) {
            this.dineMenuRestAddr = dineMenuRestAddr;
        }

    }
}

