package com.antonina.socialsynchro.services.facebook.gui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.gui.serialization.SerializableList;
import com.antonina.socialsynchro.common.rest.IServiceEntity;
import com.antonina.socialsynchro.common.utils.GenerateUtils;
import com.antonina.socialsynchro.services.backend.BackendClient;
import com.antonina.socialsynchro.services.backend.requests.BackendGetFacebookTokenRequest;
import com.antonina.socialsynchro.services.backend.responses.BackendGetFacebookTokenResponse;
import com.antonina.socialsynchro.services.facebook.content.FacebookAccount;
import com.antonina.socialsynchro.services.facebook.rest.FacebookClient;
import com.antonina.socialsynchro.services.facebook.rest.authorization.FacebookUserAuthorizationStrategy;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookGetUserPagesRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookInspectTokenRequest;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookGetUserPagesResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookInspectTokenResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookPageResponse;

import java.util.ArrayList;
import java.util.List;

public class FacebookLoginActivity extends AppCompatActivity {
    private String state;
    private String userToken;
    private String userID;

    private List<Account> accounts;
    private int loadedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("state"))
                state = savedInstanceState.getString("state");
            if (savedInstanceState.containsKey("user_token"))
                userToken = savedInstanceState.getString("user_token");
            if (savedInstanceState.containsKey("user_id"))
                userID = savedInstanceState.getString("user_id");
        }

        accounts = new ArrayList<>();
        loadedCount = 0;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("state", state);
        outState.putString("user_token", userToken);
        outState.putString("user_id", userID);
        super.onSaveInstanceState(outState);
    }

    public void buttonConnect_onClick(View view) {
        connect();
    }

    public void buttonConfirm_onClick(View view) {
        getAccessToken();
    }

    private void connect() {
        state = GenerateUtils.generateRandomString(30);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(FacebookClient.getLoginURL(state)));
        startActivity(browserIntent);
    }

    private void getAccessToken() {
        BackendGetFacebookTokenRequest request = BackendGetFacebookTokenRequest.builder()
                .state(state)
                .build();
        final Context context = this;
        final LiveData<BackendGetFacebookTokenResponse> asyncResponse = BackendClient.getFacebookToken(request);
        asyncResponse.observe(this, new Observer<BackendGetFacebookTokenResponse>() {
            @Override
            public void onChanged(@Nullable BackendGetFacebookTokenResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        if (state.equals(response.getState())) {
                            userToken = response.getToken();
                            inspectToken();
                        }
                    } else {
                        Toast toast = Toast.makeText(context, getResources().getString(R.string.error_facebook_token, response.getErrorString()), Toast.LENGTH_LONG);
                        toast.show();
                    }
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    private void inspectToken() {
        FacebookInspectTokenRequest request = FacebookInspectTokenRequest.builder()
                .inputToken(userToken)
                .build();
        final LiveData<FacebookInspectTokenResponse> asyncResponse = FacebookClient.inspectToken(request);
        asyncResponse.observe(this, new Observer<FacebookInspectTokenResponse>() {
            @Override
            public void onChanged(@Nullable FacebookInspectTokenResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        userID = response.getUserID();
                        getUserPages();
                    }
                }
            }
        });
    }

    private void getUserPages() {
        FacebookUserAuthorizationStrategy authorization = new FacebookUserAuthorizationStrategy(userToken);
        FacebookGetUserPagesRequest request = FacebookGetUserPagesRequest.builder()
                .userID(userID)
                .authorizationStrategy(authorization)
                .build();
        final Context context = this;
        final LiveData<FacebookGetUserPagesResponse> asyncResponse = FacebookClient.getUserPages(request);
        asyncResponse.observe(this, new Observer<FacebookGetUserPagesResponse>() {
            @Override
            public void onChanged(@Nullable FacebookGetUserPagesResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        final int pageCount = response.getPages().size();
                        List<FacebookPageResponse> pageResponses = response.getPages();
                        OnSynchronizedListener listener = new OnSynchronizedListener() {
                            @Override
                            public void onSynchronized(IServiceEntity entity) {
                                loadedCount++;
                                if (loadedCount >= pageCount)
                                    exitAndSave();
                            }

                            @Override
                            public void onError(IServiceEntity entity, String error) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.error_account_info, error), Toast.LENGTH_LONG);
                                toast.show();
                            }
                        };

                        for (FacebookPageResponse pageResponse : pageResponses) {
                            FacebookAccount account = new FacebookAccount();
                            account.createFromResponse(pageResponse);
                            account.loadPicture(listener);
                            accounts.add(account);
                        }
                    }
                }
            }
        });
    }

    private void exitAndSave() {
        SerializableList<Account> serializableAccounts = new SerializableList<>(accounts);
        Intent accountsActivity = new Intent();
        accountsActivity.putExtra("accounts", serializableAccounts);
        setResult(RESULT_OK, accountsActivity);
        finish();
    }
}
