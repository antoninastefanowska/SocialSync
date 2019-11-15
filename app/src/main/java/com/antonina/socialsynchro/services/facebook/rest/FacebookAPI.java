package com.antonina.socialsynchro.services.facebook.rest;

import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookGetPagePictureResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookGetUserPagesResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookInspectTokenResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookPageResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FacebookAPI {
    @GET("/debug_token")
    Call<FacebookInspectTokenResponse> inspectToken(@Query("input_token") String inputToken, @Query("access_token") String appToken);

    @GET("/v5.0/{user_id}/accounts")
    Call<FacebookGetUserPagesResponse> getUserPages(@Path("user_id") String userID, @Query("access_token") String accessToken);

    @GET("/v5.0/{id}")
    Call<FacebookPageResponse> getPage(@Path("id") String id, @Query("access_token") String accessToken);

    @GET("/v5.0/{id}/picture")
    Call<FacebookGetPagePictureResponse> getPagePicture(@Path("id") String id, @Query("redirect") boolean redirect, @Query("access_token") String accessToken);
}
