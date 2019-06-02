package com.antonina.socialsynchro.services.callback;

import android.util.Log;

import com.antonina.socialsynchro.services.twitter.TwitterController;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CallbackController {
    private final static String BASE_URL = "https://socialsynchro.pythonanywhere.com/callback/";

    public CallbackController() { }

    public void requestGetVerifier(String loginToken, String secretLoginToken) {
        VerifierController verifierController = new VerifierController();
        verifierController.start(loginToken, secretLoginToken);
    }

    private class VerifierController implements Callback<CallbackTokenResponse> {
        private String secretLoginToken;

        public void start(String loginToken, String secretLoginToken) {
            this.secretLoginToken = secretLoginToken;
            Gson gson = new Gson();
            Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
            CallbackAPI callbackAPI = retrofit.create(CallbackAPI.class);
            Call<CallbackTokenResponse> call = callbackAPI.getCallbackToken(loginToken);
            call.enqueue(this);
        }

        @Override
        public void onResponse(Call<CallbackTokenResponse> call, Response<CallbackTokenResponse> response) {
            if (response.isSuccessful()) {
                CallbackTokenResponse callbackTokenResponse = response.body();
                TwitterController twitterController = TwitterController.getInstance();
                twitterController.requestGetAccessToken(callbackTokenResponse.getLoginToken(), secretLoginToken, callbackTokenResponse.getVerifier());
            }
            else {
                Log.d("wysylanie", "callback: " + response.code() + " " + response.message());
            }
        }

        @Override
        public void onFailure(Call<CallbackTokenResponse> call, Throwable t) {
            Log.d("wysylanie", "callback: niepowodzenie");
            t.printStackTrace();
        }
    }
}
