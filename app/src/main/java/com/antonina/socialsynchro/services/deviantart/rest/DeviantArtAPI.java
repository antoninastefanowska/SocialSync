package com.antonina.socialsynchro.services.deviantart.rest;

import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtAccessTokenResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtDeviationResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtResultResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtStashPublishResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtStashSubmitResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtUserResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DeviantArtAPI {
    @FormUrlEncoded
    @POST("/oauth2/token")
    Call<DeviantArtAccessTokenResponse> getAccessToken(@Field("client_id") String clientID, @Field("client_secret") String clientSecret, @Field("grant_type") String grantType, @Field("code") String code, @Field("redirect_uri") String redirectURI);

    @FormUrlEncoded
    @POST("/oauth2/token")
    Call<DeviantArtAccessTokenResponse> refreshToken(@Field("client_id") String clientID, @Field("client_secret") String clientSecret, @Field("grant_type") String grantType, @Field("refresh_token") String refreshToken);

    @GET("/api/v1/oauth2/user/whoami")
    Call<DeviantArtUserResponse> whoAmI(@Query("expand") String expandOptions, @Query("access_token") String authorization);

    @Multipart
    @POST("/api/v1/oauth2/stash/submit")
    Call<DeviantArtStashSubmitResponse> stashSubmit(@Part MultipartBody.Part file, @Query("title") String title, @Query("artist_comments") String description, @Query("access_token") String authorization);

    @FormUrlEncoded
    @POST("/api/v1/oauth2/stash/publish")
    Call<DeviantArtStashPublishResponse> stashPublish(@Field("itemid") String stashID, @Field("is_mature") boolean isMature, @Field("agree_submission") boolean agreeSubmission, @Field("agree_tos") boolean agreeTOS, @Field("access_token") String authorization);

    @FormUrlEncoded
    @POST("/api/v1/oauth2/stash/delete")
    Call<DeviantArtResultResponse> stashDelete(@Field("itemid") String stashID, @Field("access_token") String authorization);

    @GET("/api/v1/oauth2/deviation/{deviation_id}")
    Call<DeviantArtDeviationResponse> getDeviation(@Path("deviation_id") String deviationID, @Query("access_token") String authorization);
}
