package com.antonina.socialsynchro.services.callback;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CallbackAPI {
    @GET("get_token")
    Call<CallbackTokenResponse> getCallbackToken(@Query("oauth_token") String requestToken);
}
