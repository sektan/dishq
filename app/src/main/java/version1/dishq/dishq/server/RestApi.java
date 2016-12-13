package version1.dishq.dishq.server;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import version1.dishq.dishq.server.Response.VersionCheckResponse;

/**
 * Created by dishq on 13-12-2016.
 * Package name version1.dishq.dishq.
 */

public interface RestApi {

    @POST("api/getandroidappdetails/")
    Call<VersionCheckResponse> checkVersion();

}
