package com.antonina.socialsynchro.services.twitter.rest;

import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterContentResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterGetBearerTokenResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterGetRateLimitsResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterUploadAppendResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterUploadFinalizeResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterUploadInitResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterCheckUploadStatusResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterUserResponse;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TwitterAPI {
    @POST("/oauth/request_token")
    Call<ResponseBody> getLoginToken(
            @Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("/oauth/access_token")
    Call<ResponseBody> getAccessToken(
            @Field(value = "oauth_verifier", encoded = true) String verifier,
            @Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("/oauth2/token")
    Call<TwitterGetBearerTokenResponse> getBearerToken(
            @Field(value = "grant_type", encoded = true) String grantType,
            @Header("Authorization") String authorization);

    @GET("/1.1/account/verify_credentials.json")
    Call<TwitterUserResponse> verifyCredentials(
            @Header("Authorization") String authorization);

    @GET("/1.1/users/show.json")
    Call<TwitterUserResponse> getUser(
            @Query(value = "user_id", encoded = true) String userID,
            @Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("/1.1/statuses/update.json")
    Call<TwitterContentResponse> createContent(
            @Field(value = "status", encoded = true) String status,
            @Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("/1.1/statuses/update.json")
    Call<TwitterContentResponse> createContentWithMedia(
            @Field(value = "status", encoded = true) String status,
            @Field(value = "media_ids", encoded = true) String mediaIDs,
            @Header("Authorization") String authorization);

    @POST("/1.1/statuses/destroy/{id}.json")
    Call<TwitterContentResponse> removeContent(
            @Path("id") String id,
            @Header("Authorization") String authorization);

    @GET("/1.1/statuses/show.json")
    Call<TwitterContentResponse> getContent(
            @Query(value = "id", encoded = true) String id,
            @Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("/1.1/media/upload.json")
    Call<TwitterUploadInitResponse> uploadInit(
            @Field(value = "command", encoded = true) String command,
            @Field(value = "total_bytes", encoded = true) String totalBytes,
            @Field(value = "media_type", encoded = true) String mediaType,
            @Header("Authorization") String authorization);

    @Multipart
    @POST("/1.1/media/upload.json")
    Call<TwitterUploadAppendResponse> uploadAppend(
            @Query(value = "command", encoded = true) String command,
            @Query(value = "media_id", encoded = true) String mediaID,
            @Query(value = "segment_index", encoded = true) String segmentIndex,
            @Part("media") RequestBody media,
            @Header("Authorization") String authorization);

    @POST("/1.1/media/upload.json")
    Call<TwitterUploadFinalizeResponse> uploadFinalize(
            @Query(value = "command", encoded = true) String command,
            @Query(value = "media_id", encoded = true) String mediaID,
            @Header("Authorization") String authorization);

    @GET("/1.1/media/upload.json")
    Call<TwitterCheckUploadStatusResponse> checkUploadStatus(
            @Query(value = "command", encoded = true) String command,
            @Query(value = "media_id", encoded = true) String mediaID,
            @Header("Authorization") String authorization);

    @GET("/1.1/application/rate_limit_status.json")
    Call<TwitterGetRateLimitsResponse> getRateLimits(
            @Query(value = "resources", encoded = true) String resources,
            @Header("Authorization") String authorization);
}
