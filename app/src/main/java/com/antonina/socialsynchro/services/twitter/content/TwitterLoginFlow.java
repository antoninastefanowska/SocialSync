package com.antonina.socialsynchro.services.twitter.content;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.content.accounts.LoginFlow;
import com.antonina.socialsynchro.common.gui.activities.LoginActivity;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.rest.IServiceEntity;
import com.antonina.socialsynchro.common.rest.RequestLimit;
import com.antonina.socialsynchro.services.backend.BackendClient;
import com.antonina.socialsynchro.services.backend.requests.BackendGetTwitterVerifierRequest;
import com.antonina.socialsynchro.services.backend.responses.BackendGetTwitterVerifierResponse;
import com.antonina.socialsynchro.services.twitter.rest.TwitterClient;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterGetAccessTokenRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterGetLoginTokenRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterVerifyCredentialsRequest;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterGetAccessTokenResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterGetLoginTokenResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterUserResponse;

import java.util.ArrayList;
import java.util.List;

public class TwitterLoginFlow extends LoginFlow {
    private String loginToken;
    private String secretLoginToken;
    private String verifier;
    private TwitterAccount account;

    public TwitterLoginFlow(LoginActivity context) {
        super(context);
    }

    @Override
    public void signIn() {
        TwitterGetLoginTokenRequest request = TwitterGetLoginTokenRequest.builder().build();
        final LiveData<TwitterGetLoginTokenResponse> asyncResponse = TwitterClient.getLoginToken(request);
        asyncResponse.observe(context, new Observer<TwitterGetLoginTokenResponse>() {
            @Override
            public void onChanged(@Nullable TwitterGetLoginTokenResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        loginToken = response.getLoginToken();
                        secretLoginToken = response.getLoginSecretToken();

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(TwitterClient.getLoginURL(loginToken)));
                        context.startActivity(browserIntent);
                    } else {
                        Toast toast = Toast.makeText(context, context.getResources().getString(R.string.error_login_token, response.getErrorString()), Toast.LENGTH_LONG);
                        toast.show();
                    }
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    @Override
    public void confirm() {
        BackendGetTwitterVerifierRequest request = BackendGetTwitterVerifierRequest.builder()
                .loginToken(loginToken)
                .build();
        final LiveData<BackendGetTwitterVerifierResponse> asyncResponse = BackendClient.getTwitterVerifier(request);
        asyncResponse.observe(context, new Observer<BackendGetTwitterVerifierResponse>() {
            @Override
            public void onChanged(@Nullable BackendGetTwitterVerifierResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        if (loginToken.equals(response.getLoginToken())) {
                            verifier = response.getVerifier();
                            getAccessToken();
                        }
                    } else {
                        Toast toast = Toast.makeText(context, context.getResources().getString(R.string.error_twitter_verifier, response.getErrorString()), Toast.LENGTH_LONG);
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
        final LiveData<TwitterGetAccessTokenResponse> asyncResponse = TwitterClient.getAccessToken(request);
        asyncResponse.observe(context, new Observer<TwitterGetAccessTokenResponse>() {
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
                        Toast toast = Toast.makeText(context, context.getResources().getString(R.string.error_access_token, response.getErrorString()), Toast.LENGTH_LONG);
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
            final LiveData<TwitterUserResponse> asyncResponse = TwitterClient.verifyCredentials(request);
            asyncResponse.observeForever(new Observer<TwitterUserResponse>() {
                @Override
                public void onChanged(@Nullable TwitterUserResponse response) {
                    if (response != null) {
                        if (response.getErrorString() == null) {
                            account.createFromResponse(response);
                            complete();
                        } else {
                            Toast toast = Toast.makeText(context, context.getResources().getString(R.string.error_account_info, response.getErrorString()), Toast.LENGTH_LONG);
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

    @Override
    protected void complete() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        context.exitAndSave(accounts);
    }
}
