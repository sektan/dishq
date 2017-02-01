package version1.dishq.dishq.server;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import version1.dishq.dishq.fragments.dialogfragment.filters.models.quickfilters.FoodFilters;
import version1.dishq.dishq.fragments.dialogfragment.filters.models.searchfilters.SearchFilter;
import version1.dishq.dishq.server.Request.FavDishAddRemHelper;
import version1.dishq.dishq.server.Request.SignUpHelper;
import version1.dishq.dishq.server.Request.UserPrefRequest;
import version1.dishq.dishq.server.Response.DeliveryTabResponse;
import version1.dishq.dishq.server.Response.DineoutMenuResponse;
import version1.dishq.dishq.server.Response.DineoutTabResponse;
import version1.dishq.dishq.server.Response.FavouriteDishesResponse;
import version1.dishq.dishq.server.Response.HomeDishesResponse;
import version1.dishq.dishq.server.Response.MenuFinderNearbyRestResponse;
import version1.dishq.dishq.server.Response.MenuFinderRestSuggestResponse;
import version1.dishq.dishq.server.Response.SignUpResponse;
import version1.dishq.dishq.server.Response.TastePrefData;
import version1.dishq.dishq.server.Response.VersionCheckResponse;
import version1.dishq.dishq.server.Response.DeliveryMenuResponse;

/**
 * Created by dishq on 13-12-2016.
 * Package name version1.dishq.dishq.
 */

public interface RestApi {

    //Api call for checking the version of the app
    @GET("api/getandroidappdetails/")
    Call<VersionCheckResponse> checkVersion(@Query("version_number") int versionCode,
                                            @Query("version_name") String versionName,
                                            @Query("uid") String UniqueIdentifier,
                                            @Query("user_id") int UserId);

    @GET("api/appclose/")
    Call<ResponseBody> appClose(@Query("uid") String UniqueIdentifier,
                                @Query("user_id") int UserId);

    @GET("api/apptobackground/")
    Call<ResponseBody> appToBackground(@Query("uid") String UniqueIdentifier,
                                       @Query("user_id") int UserId);

    @POST("api/auth/signin/")
    Call<SignUpResponse> createNewUser(@Header("Authorization") String authorization,
                                       @Body SignUpHelper signUpHelper);

    @GET("api/tastepref/fetchformdata/")
    Call<TastePrefData> fetchTastePref(@Query("uid") String UniqueIdentifier,
                                       @Query("user_id") int UserId);

    @POST("api/tastepref/saveorupdate/")
    Call<ResponseBody> sendUserPref(@Header("Authorization") String authorization,
                                    @Body UserPrefRequest userPrefRequest);

    @GET("api/search/dish/")
    Call<HomeDishesResponse> fetchPersonalDishes(@Header("Authorization") String authorization,
                                                 @Query("uid") String uid,
                                                 @Query("latitude") String latitude,
                                                 @Query("longitude") String longitude,
                                                 @Query("food_mood_id") int foodMoodId,
                                                 @Query("class_name") String quickFilterName,
                                                 @Query("entity_id") int quickFilterEntityId);

    @POST("api/ugc/dishfavourite/")
    Call<ResponseBody> addRemoveFavDish(@Header("Authorization") String authorization,
                                        @Body FavDishAddRemHelper favDishAddRemHelper);

    @GET("api/search/filters/")
    Call<FoodFilters> getFoodFilters(@Header("Authorization") String authorization);

    @GET("api/search/suggest/foodmood/")
    Call<SearchFilter> getSearchFilters(@Query("query") String searchKey,
                                        @Query("uid") String UniqueIdentifier,
                                        @Query("user_id") int UserId);


    @GET("api/restaurant/dish/{generic_dish_id}/dineout/")
    Call<DineoutTabResponse> addDineRestOptions(@Header("Authorization") String authorization,
                                                @Path("generic_dish_id") int genericDishId,
                                                @Query("uid") String uid,
                                                @Query("source") String Source,
                                                @Query("latitude") String latitude,
                                                @Query("longitude") String longitude,
                                                @Query("showmore") int showMore);

    @GET("api/restaurant/dish/{generic_dish_id}/delivery/")
    Call<DeliveryTabResponse> addDelivRestOptions(@Header("Authorization") String authorization,
                                                  @Path("generic_dish_id") int genericDishId,
                                                  @Query("uid") String uid,
                                                  @Query("source") String Source,
                                                  @Query("latitude") String latitude,
                                                  @Query("longitude") String longitude,
                                                  @Query("showmore") int showMore);

    @GET("api/ugc/allfavourites/")
    Call<FavouriteDishesResponse> getFavouriteDishes(@Header("Authorization") String authorization,
                                                     @Query("uid") String uid);

    @GET("api/restaurant/{restaurant_id}/menu/dineout/")
    Call<DineoutMenuResponse> getDineoutMenuOptions(@Header("Authorization") String authorization,
                                                    @Path("restaurant_id") int restId,
                                                    @Query("uid") String uid,
                                                    @Query("source") String Source,
                                                    @Query("latitude") String latitude,
                                                    @Query("longitude") String longitude,
                                                    @Query("source_generic_dish_id") int genericDishId);

    @GET("api/restaurant/{restaurant_id}/menu/delivery/")
    Call<DeliveryMenuResponse> getDeliveryMenuOptions(@Header("Authorization") String authorization,
                                                      @Path("restaurant_id") int restId,
                                                      @Query("uid") String uid,
                                                      @Query("source") String Source,
                                                      @Query("latitude") String latitude,
                                                      @Query("longitude") String longitude,
                                                      @Query("source_generic_dish_id") int genericDishId);

    @GET("api/restaurant/nearby/")
    Call<MenuFinderNearbyRestResponse> getNearbyRestaurants(@Query("uid") String uid,
                                                            @Query("user_id") int UserId,
                                                            @Query("latitude") String latitude,
                                                            @Query("longitude") String longitude);

    @GET("api/search/restaurantsuggest/")
    Call<MenuFinderRestSuggestResponse> getRestSuggestion(@Query("query") String query,
                                                          @Query("uid") String uid,
                                                          @Query("user_id") int UserId,
                                                          @Query("latitude") String latitude,
                                                          @Query("longitude") String longitude);

    //Api call for sending the feedback response to the server
    @GET("api/ugc/feedback/")
    Call<ResponseBody> sendFeedback(@Header("Authorization") String authorization,
                                    @Query("uid") String uid,
                                    @Query("feedback") int feedback);
}
