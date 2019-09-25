package com.antonina.socialsynchro.services.backend;

import com.antonina.socialsynchro.services.backend.responses.BackendGetTwitterVerifierResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BackendAPI {
    @GET("get_verifier")
    Call<BackendGetTwitterVerifierResponse> getTwitterVerifier(@Query("oauth_token") String loginToken);
}
