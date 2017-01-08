package version1.dishq.dishq.server.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dishq on 06-01-2017.
 * Package name version1.dishq.dishq.
 */

public class FavouriteDishesResponse {

    @SerializedName("data")
    @Expose
    public ArrayList<FavouriteDishesInfo> favouriteDishesInfos;

    @SerializedName("response")
    @Expose
    private String favDishesResponse;

    public class FavouriteDishesInfo {
        @SerializedName("tags")
        @Expose
        public ArrayList<String> favDishTags;

        @SerializedName("photo")
        @Expose
        public ArrayList<String> favDishPhoto;

        @SerializedName("dish_type")
        @Expose
        public FavDishType favDishType;

        @SerializedName("course")
        @Expose
        public FavDishCourse favDishCourse;

        @SerializedName("recipe_url")
        @Expose
        private String favDishRecipeUrl;

        @SerializedName("dish_nature")
        @Expose
        private int favDishNature;

        @SerializedName("generic_dish_id")
        @Expose
        private int favDishGenericDishId;

        @SerializedName("cuisine")
        @Expose
        public ArrayList<FavDishCuisine> favDishCuisines;

        @SerializedName("name")
        @Expose
        private String favDishName;

        public ArrayList<String> getFavDishTags() {
            return favDishTags;
        }

        public void setFavDishTags(ArrayList<String> favDishTags) {
            this.favDishTags = favDishTags;
        }

        public ArrayList<String> getFavDishPhoto() {
            return favDishPhoto;
        }

        public void setFavDishPhoto(ArrayList<String> favDishPhoto) {
            this.favDishPhoto = favDishPhoto;
        }

        public FavDishType getFavDishType() {
            return favDishType;
        }

        public void setFavDishType(FavDishType favDishType) {
            this.favDishType = favDishType;
        }

        public FavDishCourse getFavDishCourse() {
            return favDishCourse;
        }

        public void setFavDishCourse(FavDishCourse favDishCourse) {
            this.favDishCourse = favDishCourse;
        }

        public String getFavDishRecipeUrl() {
            return favDishRecipeUrl;
        }

        public void setFavDishRecipeUrl(String favDishRecipeUrl) {
            this.favDishRecipeUrl = favDishRecipeUrl;
        }

        public int getFavDishNature() {
            return favDishNature;
        }

        public void setFavDishNature(int favDishNature) {
            this.favDishNature = favDishNature;
        }

        public int getFavDishGenericDishId() {
            return favDishGenericDishId;
        }

        public void setFavDishGenericDishId(int favDishGenericDishId) {
            this.favDishGenericDishId = favDishGenericDishId;
        }

        public ArrayList<FavDishCuisine> getFavDishCuisines() {
            return favDishCuisines;
        }

        public void setFavDishCuisines(ArrayList<FavDishCuisine> favDishCuisines) {
            this.favDishCuisines = favDishCuisines;
        }

        public String getFavDishName() {
            return favDishName;
        }

        public void setFavDishName(String favDishName) {
            this.favDishName = favDishName;
        }
    }

    private class FavDishType {
        @SerializedName("id")
        @Expose
        private int favDishTypeId;

        @SerializedName("name")
        @Expose
        private String favDishTypeName;

        public int getFavDishTypeId() {
            return favDishTypeId;
        }

        public void setFavDishTypeId(int favDishTypeId) {
            this.favDishTypeId = favDishTypeId;
        }

        public String getFavDishTypeName() {
            return favDishTypeName;
        }

        public void setFavDishTypeName(String favDishTypeName) {
            this.favDishTypeName = favDishTypeName;
        }
    }

    private class FavDishCourse {
        @SerializedName("id")
        @Expose
        private int favDishCourseId;

        @SerializedName("name")
        @Expose
        private String favDishCourseName;

        public int getFavDishCourseId() {
            return favDishCourseId;
        }

        public void setFavDishCourseId(int favDishCourseId) {
            this.favDishCourseId = favDishCourseId;
        }

        public String getFavDishCourseName() {
            return favDishCourseName;
        }

        public void setFavDishCourseName(String favDishCourseName) {
            this.favDishCourseName = favDishCourseName;
        }
    }

    private class FavDishCuisine {
        @SerializedName("id")
        @Expose
        private int favDishCuisineId;

        @SerializedName("name")
        @Expose
        private String favDishCuisineName;

        public int getFavDishCuisineId() {
            return favDishCuisineId;
        }

        public void setFavDishCuisineId(int favDishCuisineId) {
            this.favDishCuisineId = favDishCuisineId;
        }

        public String getFavDishCuisineName() {
            return favDishCuisineName;
        }

        public void setFavDishCuisineName(String favDishCuisineName) {
            this.favDishCuisineName = favDishCuisineName;
        }
    }
}
