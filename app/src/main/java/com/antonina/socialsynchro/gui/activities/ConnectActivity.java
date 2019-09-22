package com.antonina.socialsynchro.gui.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.services.callback.CallbackClient;
import com.antonina.socialsynchro.services.callback.requests.CallbackGetVerifierRequest;
import com.antonina.socialsynchro.services.callback.responses.CallbackGetVerifierResponse;
import com.antonina.socialsynchro.services.twitter.TwitterAccount;
import com.antonina.socialsynchro.services.twitter.TwitterClient;
import com.antonina.socialsynchro.services.twitter.requests.TwitterGetAccessTokenRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterGetLoginTokenRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterVerifyCredentialsRequest;
import com.antonina.socialsynchro.services.twitter.responses.TwitterGetAccessTokenResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterGetLoginTokenResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterVerifyCredentialsResponse;

public class ConnectActivity extends AppCompatActivity {
    private String loginToken;
    private String secretLoginToken;
    private String verifier;
    private TwitterAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("login_token"))
                loginToken = savedInstanceState.getString("login_token");
            if (savedInstanceState.containsKey("secret_login_token"))
                secretLoginToken = savedInstanceState.getString("secret_login_token");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("login_token", loginToken);
        outState.putString("secret_login_token", secretLoginToken);
        super.onSaveInstanceState(outState);
    }

    public void buttonConnect_onClick(View view) {
        getLoginToken();
    }

    public void buttonConfirm_onClick(View view) {
        getVerifier();
    }

    private void getLoginToken() {
        TwitterClient client = TwitterClient.getInstance();
        TwitterGetLoginTokenRequest request = TwitterGetLoginTokenRequest.builder().build();
        final Context context = this;
        final LiveData<TwitterGetLoginTokenResponse> asyncResponse = client.getLoginToken(request);
        asyncResponse.observe(this, new Observer<TwitterGetLoginTokenResponse>() {
            @Override
            public void onChanged(@Nullable TwitterGetLoginTokenResponse response) {
                if (response.getErrorString() == null) {
                    loginToken = response.getLoginToken();
                    secretLoginToken = response.getLoginSecretToken();

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(TwitterClient.getLoginURL(loginToken)));
                    startActivity(browserIntent);
                } else {
                    Toast toast = Toast.makeText(context, getResources().getString(R.string.error_login_token, response.getErrorString()), Toast.LENGTH_LONG);
                    toast.show();
                }
                asyncResponse.removeObserver(this);
            }
        });
    }

    private void getVerifier() {
        CallbackClient client = CallbackClient.getInstance();
        CallbackGetVerifierRequest request = CallbackGetVerifierRequest.builder()
                .loginToken(loginToken)
                .build();
        final Context context = this;
        final LiveData<CallbackGetVerifierResponse> asyncResponse = client.getVerifier(request);
        asyncResponse.observe(this, new Observer<CallbackGetVerifierResponse>() {
            @Override
            public void onChanged(@Nullable CallbackGetVerifierResponse response) {
                if (response.getErrorString() == null) {
                    if (loginToken.equals(response.getLoginToken())) {
                        verifier = response.getVerifier();
                        getAccessToken();
                    }
                } else {
                    Toast toast = Toast.makeText(context, getResources().getString(R.string.error_verifier, response.getErrorString()), Toast.LENGTH_LONG);
                    toast.show();
                }
                asyncResponse.removeObserver(this);
            }
        });
    }

    private void getAccessToken() {
        TwitterClient client = TwitterClient.getInstance();
        TwitterGetAccessTokenRequest request = TwitterGetAccessTokenRequest.builder()
                .loginToken(loginToken)
                .secretLoginToken(secretLoginToken)
                .verifier(verifier)
                .build();
        final Context context = this;
        final LiveData<TwitterGetAccessTokenResponse> asyncResponse = client.getAccessToken(request);
        asyncResponse.observe(this, new Observer<TwitterGetAccessTokenResponse>() {
            @Override
            public void onChanged(@Nullable TwitterGetAccessTokenResponse response) {
                if (response.getErrorString() == null) {
                    account = new TwitterAccount();
                    account.setAccessToken(response.getAccessToken());
                    account.setSecretToken(response.getSecretToken());
                    verifyCredentials();
                } else {
                    Toast toast = Toast.makeText(context, getResources().getString(R.string.error_access_token, response.getErrorString()), Toast.LENGTH_LONG);
                    toast.show();
                }
                asyncResponse.removeObserver(this);
            }
        });
    }

    private void verifyCredentials() {
        TwitterClient client = TwitterClient.getInstance();
        TwitterVerifyCredentialsRequest request = TwitterVerifyCredentialsRequest.builder()
                .accessToken(account.getAccessToken())
                .secretToken(account.getSecretToken())
                .build();
        final Context context = this;
        final LiveData<TwitterVerifyCredentialsResponse> asyncResponse = client.verifyCredentials(request);
        asyncResponse.observeForever(new Observer<TwitterVerifyCredentialsResponse>() {
            @Override
            public void onChanged(@Nullable TwitterVerifyCredentialsResponse response) {
                if (response.getErrorString() == null) {
                    account.createFromResponse(response);
                    exitAndSave();
                } else {
                    Toast toast = Toast.makeText(context, getResources().getString(R.string.error_account_info, response.getErrorString()), Toast.LENGTH_LONG);
                    toast.show();
                }
                asyncResponse.removeObserver(this);
            }
        });
    }

    private void exitAndSave() {
        Intent accountsActivity = new Intent();
        accountsActivity.putExtra("account", account);
        setResult(RESULT_OK, accountsActivity);
        finish();
    }
}
