package com.antonina.socialsynchro.services.facebook.rest;

import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookContentResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookCountResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookIdentifierResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookGetPagePictureResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookGetUserPagesResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookInspectTokenResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookPageResponse;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FacebookAPI {
    @GET("/debug_token")
    Call<FacebookInspectTokenResponse> inspectToken(@Query("input_token") String inputToken, @Query("access_token") String appToken);

    @GET("/v5.0/{user_id}/accounts")
    Call<FacebookGetUserPagesResponse> getUserPages(@Path("user_id") String userID, @Query("access_token") String accessToken);

    @GET("/v5.0/{page_id}")
    Call<FacebookPageResponse> getPage(@Path("page_id") String pageID, @Query("fields") String fields, @Query("access_token") String accessToken);

    @GET("/v5.0/{page_id}/picture")
    Call<FacebookGetPagePictureResponse> getPagePicture(@Path("page_id") String pageID, @Query("redirect") boolean redirect, @Query("access_token") String accessToken);

    @FormUrlEncoded
    @POST("/v5.0/{page_id}/feed")
    Call<FacebookIdentifierResponse> createContent(@Path("page_id") String pageID, @Field("message") String message, @Query("access_token") String accessToken);

    @FormUrlEncoded
    @POST("/v5.0/{page_id}/feed")
    Call<FacebookIdentifierResponse> createContentWithMedia(@Path("page_id") String pageID, @Field("message") String message, @FieldMap HashMap<String, String> mediaIDs, @Query("access_token") String accessToken);

    @GET("/v5.0/{post_id}")
    Call<FacebookContentResponse> getContent(@Path("post_id") String postID, @Query("access_token") String accessToken);

    @FormUrlEncoded
    @POST("/v5.0/{post_id}")
    Call<FacebookIdentifierResponse> updateContent(@Path("post_id") String postID, @Field("message") String message, @Query("access_token") String accessToken);

    @DELETE("/v5.0/{post_id}")
    Call<FacebookIdentifierResponse> removeContent(@Path("post_id") String postID, @Query("access_token") String accessToken);

    @Multipart
    @POST("/v5.0/{page_id}/photos")
    Call<FacebookIdentifierResponse> uploadPhoto(@Path("page_id") String pageID, @Part MultipartBody.Part photo, @Query("published") boolean published, @Query("access_token") String accessToken);

    @GET("/v5.0/{post_id}/reactions")
    Call<FacebookCountResponse> getPostReactions(@Path("post_id") String postID, @Query("access_token") String accessToken);

    @GET("/v5.0/{post_id}/comments")
    Call<FacebookCountResponse> getPostComments(@Path("post_id") String postID, @Query("access_token") String accessToken);
}
