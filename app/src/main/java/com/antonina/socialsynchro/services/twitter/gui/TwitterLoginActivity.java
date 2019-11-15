package com.antonina.socialsynchro.services.twitter.gui;

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
import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.gui.serialization.SerializableList;
import com.antonina.socialsynchro.common.rest.IServiceEntity;
import com.antonina.socialsynchro.common.rest.RequestLimit;
import com.antonina.socialsynchro.services.backend.BackendClient;
import com.antonina.socialsynchro.services.backend.requests.BackendGetTwitterVerifierRequest;
import com.antonina.socialsynchro.services.backend.responses.BackendGetTwitterVerifierResponse;
import com.antonina.socialsynchro.services.twitter.content.TwitterAccount;
import com.antonina.socialsynchro.services.twitter.rest.TwitterClient;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterGetAccessTokenRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterGetLoginTokenRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterVerifyCredentialsRequest;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterGetAccessTokenResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterGetLoginTokenResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterUserResponse;

import java.util.ArrayList;
import java.util.List;

public class TwitterLoginActivity extends AppCompatActivity {
    private String loginToken;
    private String secretLoginToken;
    private String verifier;
    private TwitterAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_login);

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
        TwitterGetLoginTokenRequest request = TwitterGetLoginTokenRequest.builder().build();
        final Context context = this;
        final LiveData<TwitterGetLoginTokenResponse> asyncResponse = TwitterClient.getLoginToken(request);
        asyncResponse.observe(this, new Observer<TwitterGetLoginTokenResponse>() {
            @Override
            public void onChanged(@Nullable TwitterGetLoginTokenResponse response) {
                if (response != null) {
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
            }
        });
    }

    private void getVerifier() {
        BackendGetTwitterVerifierRequest request = BackendGetTwitterVerifierRequest.builder()
                .loginToken(loginToken)
                .build();
        final Context context = this;
        final LiveData<BackendGetTwitterVerifierResponse> asyncResponse = BackendClient.getTwitterVerifier(request);
        asyncResponse.observe(this, new Observer<BackendGetTwitterVerifierResponse>() {
            @Override
            public void onChanged(@Nullable BackendGetTwitterVerifierResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        if (loginToken.equals(response.getLoginToken())) {
                            verifier = response.getVerifier();
                            getAccessToken();
                        }
                    } else {
                        Toast toast = Toast.makeText(context, getResources().getString(R.string.error_twitter_verifier, response.getErrorString()), Toast.LENGTH_LONG);
                        toast.show();
                    }
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    private void getAccessToken() {
        TwitterGetAccessTokenRequest request = TwitterGetAccessTokenRequest.builder()
                .loginToken(loginToken)
                .secretLoginToken(secretLoginToken)
                .verifier(verifier)
                .build();
        final Context context = this;
        final LiveData<TwitterGetAccessTokenResponse> asyncResponse = TwitterClient.getAccessToken(request);
        asyncResponse.observe(this, new Observer<TwitterGetAccessTokenResponse>() {
            @Override
            public void onChanged(@Nullable TwitterGetAccessTokenResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        account = new TwitterAccount();
                        account.setAccessToken(response.getAccessToken());
                        account.setSecretToken(response.getSecretToken());
                        account.synchronizeRequestLimits(new OnSynchronizedListener() {
                            @Override
                            public void onSynchronized(IServiceEntity entity) {
                                verifyCredentials();
                            }
                            @Override
                            public void onError(IServiceEntity entity, String error) {
                                //TODO
                            }
                        });
                    } else {
                        Toast toast = Toast.makeText(context, getResources().getString(R.string.error_access_token, response.getErrorString()), Toast.LENGTH_LONG);
                        toast.show();
                    }
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    private void verifyCredentials() {
        String endpoint = TwitterVerifyCredentialsRequest.getRequestEndpoint();
        RequestLimit requestLimit = account.getRequestLimit(endpoint);
        if (requestLimit.getRemaining() > 0) {
            TwitterVerifyCredentialsRequest request = TwitterVerifyCredentialsRequest.builder()
                    .accessToken(account.getAccessToken())
                    .secretToken(account.getSecretToken())
                    .build();
            final Context context = this;
            final LiveData<TwitterUserResponse> asyncResponse = TwitterClient.verifyCredentials(request);
            asyncResponse.observeForever(new Observer<TwitterUserResponse>() {
                @Override
                public void onChanged(@Nullable TwitterUserResponse response) {
                    if (response != null) {
                        if (response.getErrorString() == null) {
                            account.createFromResponse(response);
                            exitAndSave();
                        } else {
                            Toast toast = Toast.makeText(context, getResources().getString(R.string.error_account_info, response.getErrorString()), Toast.LENGTH_LONG);
                            toast.show();
                        }
                        asyncResponse.removeObserver(this);
                    }
                }
            });
        } else {
            //TODO
        }
    }

    private void exitAndSave() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        SerializableList<Account> serializableAccounts = new SerializableList<>(accounts);

        Intent accountsActivity = new Intent();
        accountsActivity.putExtra("accounts", serializableAccounts);
        setResult(RESULT_OK, accountsActivity);
        finish();
    }
}
