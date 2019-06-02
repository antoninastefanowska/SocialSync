package com.antonina.socialsynchro.services.twitter;

import com.antonina.socialsynchro.services.twitter.responses.TwitterTweetResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
    Call<TwitterTweetResponse> postContent(@Field(value = "status", encoded = true) String status, @Header("Authorization") String authorization);

    @POST("1.1/statuses/destroy/{id}.json")
    Call<TwitterTweetResponse> removeContent(@Path("id") String id, @Header("Authorization") String authorization);
}
