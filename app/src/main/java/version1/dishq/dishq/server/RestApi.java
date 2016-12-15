package version1.dishq.dishq.server;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import version1.dishq.dishq.server.Request.SignUpHelper;
import version1.dishq.dishq.server.Response.SignUpResponse;
import version1.dishq.dishq.server.Response.VersionCheckResponse;
import version1.dishq.dishq.server.Response.tastePrefData;

/**
 * Created by dishq on 13-12-2016.
 * Package name version1.dishq.dishq.
 */

public interface RestApi {

    @GET("api/getandroidappdetails/")
    Call<VersionCheckResponse> checkVersion(@Query("version_number")int versionCode, @Query("version_name")String versionName, @Query("uid")String UniqueIdentifier, @Query("user_id")int UserId);

    @GET("api/appclose/")
    Call<ResponseBody> appClose(@Query("uid")int UniqueIdentifier, @Query("user_id")int UserId);

    @GET("api/apptobackground/")
    Call<ResponseBody> appToBackground(@Query("uid")int UniqueIdentifier, @Query("user_id")int UserId);

    @POST("api/auth/signup/")
    Call<SignUpResponse> createNewUser(@Body SignUpHelper signUpHelper);

    @GET("api/tastepref/fetchformdata/")
    Call<tastePrefData> fetchTastePref(@Header("Authorization")String accessToken);

}
