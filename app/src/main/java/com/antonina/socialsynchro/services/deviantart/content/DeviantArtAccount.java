package com.antonina.socialsynchro.services.deviantart.content;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.content.services.ServiceID;
import com.antonina.socialsynchro.common.content.services.Services;
import com.antonina.socialsynchro.common.content.statistics.AccountStatistic;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.rest.IResponse;
import com.antonina.socialsynchro.common.rest.IServiceEntity;
import com.antonina.socialsynchro.common.utils.ApplicationConfig;
import com.antonina.socialsynchro.common.utils.SecurityUtils;
import com.antonina.socialsynchro.services.deviantart.database.repositories.DeviantArtAccountInfoRepository;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtAccountInfoRow;
import com.antonina.socialsynchro.services.deviantart.rest.DeviantArtClient;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtRefreshTokenRequest;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtWhoAmIRequest;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtAccessTokenResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtUserResponse;

import java.util.Calendar;
import java.util.Date;

public class DeviantArtAccount extends Account {
    private String accessToken;
    private String refreshToken;
    private Date tokenDate;
    private long tokenExpiresIn;

    private int watcherCount;

    public DeviantArtAccount(IDatabaseRow table) {
        createFromDatabaseRow(table);
        setService(Services.getService(ServiceID.DeviantArt));
        watcherCount = 0;
    }

    public DeviantArtAccount() {
        setService(Services.getService(ServiceID.DeviantArt));
        watcherCount = 0;
    }

    @Override
    public void createFromDatabaseRow(IDatabaseRow data) {
        super.createFromDatabaseRow(data);

        DeviantArtAccountInfoRepository repository = DeviantArtAccountInfoRepository.getInstance();
        final LiveData<DeviantArtAccountInfoRow> dataTable = repository.getDataTableByID(data.getID());
        dataTable.observeForever(new Observer<DeviantArtAccountInfoRow>() {
            @Override
            public void onChanged(@Nullable DeviantArtAccountInfoRow data) {
                if (data != null) {
                    setAccessToken(SecurityUtils.decrypt(data.accessToken));
                    setRefreshToken(SecurityUtils.decrypt(data.refreshToken));
                    setTokenDate(data.tokenDate);
                    setTokenExpiresIn(data.tokenExiresIn);
                    setWatcherCount(data.watcherCount);
                    dataTable.removeObserver(this);
                }
            }
        });
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Date getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(Date tokenDate) {
        this.tokenDate = tokenDate;
    }

    public long getTokenExpiresIn() {
        return tokenExpiresIn;
    }

    public void setTokenExpiresIn(long tokenExpiresIn) {
        this.tokenExpiresIn = tokenExpiresIn;
    }

    public int getWatcherCount() {
        return watcherCount;
    }

    public void setWatcherCount(int watcherCount) {
        this.watcherCount = watcherCount;
    }

    @Override
    public void saveInDatabase() {
        if (getInternalID() != null)
            updateInDatabase();
        else {
            super.saveInDatabase();
            DeviantArtAccountInfoRepository repository = DeviantArtAccountInfoRepository.getInstance();
            if (!updated)
                repository.insert(this);
            else
                updateInDatabase();
        }
    }

    @Override
    public void updateInDatabase() {
        DeviantArtAccountInfoRepository repository = DeviantArtAccountInfoRepository.getInstance();
        repository.update(this);
        super.updateInDatabase();
    }

    @Override
    public void deleteFromDatabase() {
        DeviantArtAccountInfoRepository repository = DeviantArtAccountInfoRepository.getInstance();
        repository.delete(this);
        super.deleteFromDatabase();
    }

    @Override
    public AccountStatistic getStatistic() {
        return new AccountStatistic("Watchers", watcherCount, getProfilePictureURL(), getName(), getService().getPanelBackgroundID());
    }

    @Override
    public String getURL() {
        return "https://www.deviantart.com/" + getName();
    }

    @Override
    public void createFromResponse(IResponse response) {
        DeviantArtUserResponse deviantArtResponse = (DeviantArtUserResponse)response;
        setExternalID(deviantArtResponse.getUserID());
        setName(deviantArtResponse.getUsername());
        setProfilePictureURL(deviantArtResponse.getUserIcon());
        setWatcherCount(deviantArtResponse.getWatcherCount());
    }

    public void refreshToken(final OnSynchronizedListener listener) {
        ApplicationConfig config = ApplicationConfig.getInstance();
        Date currentDate = Calendar.getInstance().getTime();
        long elapsed = (currentDate.getTime() - tokenDate.getTime()) / 1000;
        if (elapsed <= tokenExpiresIn) {
            listener.onSynchronized(this);
        } else {
            DeviantArtRefreshTokenRequest request = DeviantArtRefreshTokenRequest.builder()
                    .clientID(config.getKey("deviantart_client_id"))
                    .clientSecret(config.getKey("deviantart_client_secret"))
                    .refreshToken(getRefreshToken())
                    .build();
            final DeviantArtAccount instance = this;
            final LiveData<DeviantArtAccessTokenResponse> asyncResponse = DeviantArtClient.refreshToken(request);
            asyncResponse.observeForever(new Observer<DeviantArtAccessTokenResponse>() {
                @Override
                public void onChanged(@Nullable DeviantArtAccessTokenResponse response) {
                    if (response != null) {
                        if (response.getErrorString() == null) {
                            setAccessToken(response.getAccessToken());
                            setRefreshToken(response.getRefreshToken());
                            setTokenDate(Calendar.getInstance().getTime());
                            setTokenExpiresIn(response.getExpiresIn());
                            listener.onSynchronized(instance);
                        } else {
                            listener.onError(instance, response.getErrorString());
                        }
                    }
                }
            });
        }
    }

    @Override
    public void synchronize(final OnSynchronizedListener listener) {
        setLoading(true);
        final DeviantArtAccount instance = this;
        refreshToken(new OnSynchronizedListener() {
            @Override
            public void onSynchronized(IServiceEntity entity) {
                DeviantArtWhoAmIRequest request = DeviantArtWhoAmIRequest.builder()
                        .accessToken(getAccessToken())
                        .build();
                final LiveData<DeviantArtUserResponse> asyncResponse = DeviantArtClient.whoAmI(request);
                asyncResponse.observeForever(new Observer<DeviantArtUserResponse>() {
                    @Override
                    public void onChanged(@Nullable DeviantArtUserResponse response) {
                        if (response != null) {
                            if (response.getErrorString() == null) {
                                createFromResponse(response);
                                if (getInternalID() != null)
                                    updateInDatabase();
                                listener.onSynchronized(instance);
                            } else {
                                listener.onError(instance, response.getErrorString());
                            }
                            setLoading(false);
                            asyncResponse.removeObserver(this);
                        }
                    }
                });
            }

            @Override
            public void onError(IServiceEntity entity, String error) {
                listener.onError(entity, error);
            }
        });
    }
}
