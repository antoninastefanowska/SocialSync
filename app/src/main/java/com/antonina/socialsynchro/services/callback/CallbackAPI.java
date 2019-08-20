package com.antonina.socialsynchro.services.callback;

import com.antonina.socialsynchro.services.callback.responses.CallbackGetVerifierResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CallbackAPI {
    @GET("get_verifier")
    Call<CallbackGetVerifierResponse> getVerifier(@Query("oauth_token") String loginToken);
}
