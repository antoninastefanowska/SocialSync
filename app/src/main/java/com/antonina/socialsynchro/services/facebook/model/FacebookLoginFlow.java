package com.antonina.socialsynchro.services.facebook.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.model.accounts.Account;
import com.antonina.socialsynchro.common.model.accounts.LoginFlow;
import com.antonina.socialsynchro.common.gui.activities.LoginActivity;
import com.antonina.socialsynchro.common.gui.dialogs.WarningDialog;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.rest.IServiceEntity;
import com.antonina.socialsynchro.common.utils.GenerateUtils;
import com.antonina.socialsynchro.services.backend.BackendClient;
import com.antonina.socialsynchro.services.backend.requests.BackendGetFacebookTokenRequest;
import com.antonina.socialsynchro.services.backend.responses.BackendGetFacebookTokenResponse;
import com.antonina.socialsynchro.services.facebook.rest.FacebookClient;
import com.antonina.socialsynchro.services.facebook.rest.authorization.FacebookUserAuthorizationStrategy;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookGetUserPagesRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookInspectTokenRequest;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookGetUserPagesResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookInspectTokenResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookPageResponse;

import java.util.ArrayList;
import java.util.List;

public class FacebookLoginFlow extends LoginFlow {
    private String state;
    private String userToken;
    private String userID;

    private List<Account> accounts;
    private int loadedCount;

    public FacebookLoginFlow(LoginActivity context) {
        super(context);
        accounts = new ArrayList<>();
        loadedCount = 0;
        WarningDialog warningDialog = new WarningDialog(context, context.getResources().getString(R.string.warning_facebook_app_review));
        warningDialog.show();
    }

    @Override
    public void signIn() {
        state = GenerateUtils.generateRandomString(30);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(FacebookClient.getLoginURL(state)));
        context.startActivity(browserIntent);
    }

    @Override
    public void confirm() {
        BackendGetFacebookTokenRequest request = BackendGetFacebookTokenRequest.builder()
                .state(state)
                .build();
        final LiveData<BackendGetFacebookTokenResponse> asyncResponse = BackendClient.getFacebookToken(request);
        asyncResponse.observe(context, new Observer<BackendGetFacebookTokenResponse>() {
            @Override
            public void onChanged(@Nullable BackendGetFacebookTokenResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        if (state.equals(response.getState())) {
                            userToken = response.getToken();
                            inspectToken();
                        }
                    } else {
                        Toast toast = Toast.makeText(context, context.getResources().getString(R.string.error_facebook_token, response.getErrorString()), Toast.LENGTH_LONG);
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
        asyncResponse.observe(context, new Observer<FacebookInspectTokenResponse>() {
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
        final LiveData<FacebookGetUserPagesResponse> asyncResponse = FacebookClient.getUserPages(request);
        asyncResponse.observe(context, new Observer<FacebookGetUserPagesResponse>() {
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
                                    complete();
                            }

                            @Override
                            public void onError(IServiceEntity entity, String error) {
                                Toast toast = Toast.makeText(context, context.getResources().getString(R.string.error_account_info, error), Toast.LENGTH_LONG);
                                toast.show();
                            }
                        };

                        for (FacebookPageResponse pageResponse : pageResponses) {
                            FacebookAccount account = new FacebookAccount();
                            account.createFromResponse(pageResponse);
                            account.synchronize(listener);
                            accounts.add(account);
                        }
                    }
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    @Override
    protected void complete() {
        context.exitAndSave(accounts);
    }
}
