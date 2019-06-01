package com.antonina.socialsynchro.apis;

import com.antonina.socialsynchro.accounts.TwitterAccount;
import com.antonina.socialsynchro.requests.OAuthToken;
import com.antonina.socialsynchro.posts.Post;
import com.antonina.socialsynchro.responses.TwitterPostResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TwitterAPI {
    @FormUrlEncoded
    @POST("oath2/token")
    Call<OAuthToken> getToken(@Field("grant_type") String grantType, @Header("Authorization") String apiKey);

    @FormUrlEncoded
    @POST("/1.1/statuses/update.json")
    Call<TwitterPostResponse> postContent(@Field(value = "status", encoded = true) String status, @Header("Authorization") String authorization);

    @POST("1.1/statuses/destroy/{id}.json")
    Call<TwitterPostResponse> removeContent(@Path("id") String id, @Header("Authorization") String authorization);

    @GET("/1.1/users/show.json")
    Call<TwitterAccount> getAccountDetails(@Query("screen_name") String name);
}
