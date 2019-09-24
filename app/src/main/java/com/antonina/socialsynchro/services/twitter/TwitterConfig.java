package com.antonina.socialsynchro.services.twitter;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.services.twitter.requests.TwitterGetBearerTokenRequest;
import com.antonina.socialsynchro.services.twitter.responses.TwitterGetBearerTokenResponse;

public class TwitterConfig {
    private Application application;

    private String bearerToken;
    private int loadBearerTokenAttempts = 0;

    public TwitterConfig(Application application) {
        this.application = application;
        loadBearerToken();
    }

    public boolean isApplicationAuthorizationEnabled() {
        return bearerToken != null;
    }

    private void loadBearerToken() {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        if (sharedPreferences.contains("twitter_bearer_token"))
            bearerToken = sharedPreferences.getString("twitter_bearer_token", null);
        else {
            TwitterGetBearerTokenRequest request = TwitterGetBearerTokenRequest.builder().build();
            final LiveData<TwitterGetBearerTokenResponse> asyncResponse = TwitterClient.getBearerToken(request);
            asyncResponse.observeForever(new Observer<TwitterGetBearerTokenResponse>() {
                @Override
                public void onChanged(@Nullable TwitterGetBearerTokenResponse response) {
                    if (response != null) {
                        if (response.getErrorString() == null) {
                            if (response.getTokenType().equals("bearer")) {
                                bearerToken = response.getBearerToken();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("twitter_bearer_token", bearerToken);
                                editor.apply();
                            }
                        } else {
                            loadBearerTokenAttempts++;
                            if (loadBearerTokenAttempts < 3)
                                loadBearerToken();
                        }
                        asyncResponse.removeObserver(this);
                    }
                }
            });
        }
    }

    public String getBearerToken() {
        if (bearerToken == null)
            loadBearerToken();
        return bearerToken;
    }
}
