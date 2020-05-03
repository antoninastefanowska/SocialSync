package com.antonina.socialsynchro.services.deviantart.model;

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
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.rest.IServiceEntity;
import com.antonina.socialsynchro.common.utils.ApplicationConfig;
import com.antonina.socialsynchro.common.utils.GenerateUtils;
import com.antonina.socialsynchro.services.backend.BackendClient;
import com.antonina.socialsynchro.services.backend.requests.BackendGetDeviantArtCodeRequest;
import com.antonina.socialsynchro.services.backend.responses.BackendGetDeviantArtCodeResponse;
import com.antonina.socialsynchro.services.deviantart.rest.DeviantArtClient;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtGetAccessTokenRequest;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtAccessTokenResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DeviantArtLoginFlow extends LoginFlow {
    private String state;
    private String code;
    private DeviantArtAccount account;

    public DeviantArtLoginFlow(LoginActivity context) {
        super(context);
    }

    @Override
    public void signIn() {
        state = GenerateUtils.generateRandomString(30);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(DeviantArtClient.getLoginURL(state)));
        context.startActivity(browserIntent);
    }

    @Override
    public void confirm() {
        BackendGetDeviantArtCodeRequest request = BackendGetDeviantArtCodeRequest.builder()
                .state(state)
                .build();
        final LiveData<BackendGetDeviantArtCodeResponse> asyncResponse = BackendClient.getDeviantArtCode(request);
        asyncResponse.observe(context, new Observer<BackendGetDeviantArtCodeResponse>() {
            @Override
            public void onChanged(@Nullable BackendGetDeviantArtCodeResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        if (state.equals(response.getState())) {
                            code = response.getCode();
                            getAccessToken();
                        }
                    } else {
                        Toast toast = Toast.makeText(context, context.getResources().getString(R.string.error_deviantart_code, response.getErrorString()), Toast.LENGTH_LONG);
                        toast.show();
                    }
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    private void getAccessToken() {
        ApplicationConfig config = ApplicationConfig.getInstance();
        DeviantArtGetAccessTokenRequest request = DeviantArtGetAccessTokenRequest.builder()
                .clientID(config.getKey("deviantart_client_id"))
                .clientSecret(config.getKey("deviantart_client_secret"))
                .code(code)
                .build();
        final LiveData<DeviantArtAccessTokenResponse> asyncResponse = DeviantArtClient.getAccessToken(request);
        asyncResponse.observe(context, new Observer<DeviantArtAccessTokenResponse>() {
            @Override
            public void onChanged(@Nullable DeviantArtAccessTokenResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        account = new DeviantArtAccount();
                        account.setAccessToken(response.getAccessToken());
                        account.setRefreshToken(response.getRefreshToken());
                        account.setTokenDate(Calendar.getInstance().getTime());
                        account.setTokenExpiresIn(response.getExpiresIn());

                        account.synchronize(new OnSynchronizedListener() {
                            @Override
                            public void onSynchronized(IServiceEntity entity) {
                                complete();
                            }

                            @Override
                            public void onError(IServiceEntity entity, String error) {
                                Toast toast = Toast.makeText(context, context.getResources().getString(R.string.error_account_info, error), Toast.LENGTH_LONG);
                                toast.show();
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

    @Override
    protected void complete() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        context.exitAndSave(accounts);
    }
}
