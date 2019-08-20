package com.antonina.socialsynchro.services.twitter;

import com.antonina.socialsynchro.services.twitter.responses.TwitterContentResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterVerifyCredentialsResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TwitterAPI {
    @POST("/oauth/request_token")
    Call<ResponseBody> getLoginToken(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("/oauth/access_token")
    Call<ResponseBody> getAccessToken(@Field(value = "oauth_verifier", encoded = true) String verifier, @Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("/1.1/statuses/update.json")
    Call<TwitterContentResponse> createContent(@Field(value = "status", encoded = true) String status, @Header("Authorization") String authorization);

    @POST("1.1/statuses/destroy/{id}.json")
    Call<TwitterContentResponse> removeContent(@Path("id") String id, @Header("Authorization") String authorization);

    @GET("1.1/account/verify_credentials.json")
    Call<TwitterVerifyCredentialsResponse> verifyCredentials(@Header("Authorization") String authorization);
}
