package com.antonina.socialsynchro.services.backend;

import com.antonina.socialsynchro.services.backend.responses.BackendGetRateLimitsResponse;
import com.antonina.socialsynchro.services.backend.responses.BackendGetTwitterVerifierResponse;
import com.antonina.socialsynchro.services.backend.responses.BackendUpdateRequestCounterResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BackendAPI {
    @GET("get_twitter_verifier")
    Call<BackendGetTwitterVerifierResponse> getTwitterVerifier(@Query("oauth_token") String loginToken, @Header("Authorization") String authorization);

    @GET("get_rate_limits")
    Call<BackendGetRateLimitsResponse> getRateLimits(@Query("endpoint") String endpoint, @Query("service_name") String serviceName);

    @FormUrlEncoded
    @POST("update_request_counter")
    Call<BackendUpdateRequestCounterResponse> updateRequestCounter(@Field("endpoint") String endpoint, @Field("service_name") String serviceName, @Header("Authorization") String authorization);
}
