package com.antonina.socialsynchro.services.twitter.content;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.services.twitter.database.repositories.TwitterAccountInfoRepository;
import com.antonina.socialsynchro.common.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.services.twitter.database.tables.TwitterAccountInfoTable;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.utils.ApplicationConfig;
import com.antonina.socialsynchro.common.rest.IResponse;
import com.antonina.socialsynchro.common.content.services.ServiceID;
import com.antonina.socialsynchro.common.content.services.Services;
import com.antonina.socialsynchro.services.twitter.utils.TwitterConfig;
import com.antonina.socialsynchro.services.twitter.rest.TwitterClient;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterGetUserRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterVerifyCredentialsRequest;
import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterApplicationAuthorizationStrategy;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterUserResponse;

import java.util.Calendar;

public class TwitterAccount extends Account {
    private String accessToken;
    private String secretToken;

    public TwitterAccount(IDatabaseTable table) {
        createFromData(table);
        setService(Services.getService(ServiceID.Twitter));
    }

    public TwitterAccount() {
        setService(Services.getService(ServiceID.Twitter));
    }

    @Override
    public void createFromData(IDatabaseTable data) {
        super.createFromData(data);

        TwitterAccountInfoRepository repository = TwitterAccountInfoRepository.getInstance();
        final LiveData<TwitterAccountInfoTable> dataTable = repository.getDataTableByID(data.getID());
        final TwitterAccount instance = this;
        dataTable.observeForever(new Observer<TwitterAccountInfoTable>() {
            @Override
            public void onChanged(@Nullable TwitterAccountInfoTable data) {
                if (data != null) {
                    instance.setAccessToken(data.accessToken);
                    instance.setSecretToken(data.secretToken);
                    dataTable.removeObserver(this);
                }
            }
        });
    }

    // TODO: uwzględnić limity żądań

    public String getAccessToken() { return accessToken; }

    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getSecretToken() { return secretToken; }

    public void setSecretToken(String secretToken) { this.secretToken = secretToken; }

    @Override
    public void saveInDatabase() {
        super.saveInDatabase();
        TwitterAccountInfoRepository repository = TwitterAccountInfoRepository.getInstance();
        if (!updated)
            repository.insert(this);
        else
            updateInDatabase();
    }

    @Override
    public void updateInDatabase() {
        TwitterAccountInfoRepository repository = TwitterAccountInfoRepository.getInstance();
        repository.update(this);
        super.updateInDatabase();
    }

    @Override
    public void deleteFromDatabase() {
        TwitterAccountInfoRepository repository = TwitterAccountInfoRepository.getInstance();
        repository.delete(this);
        super.deleteFromDatabase();
    }

    @Override
    public void createFromResponse(IResponse response) {
        TwitterUserResponse twitterResponse = (TwitterUserResponse)response;
        setSynchronizationDate(Calendar.getInstance().getTime());
        setExternalID(twitterResponse.getID());
        setName(twitterResponse.getName());
        setProfilePictureURL(twitterResponse.getProfilePictureURL());
    }

    @Override
    public void synchronize(final OnSynchronizedListener listener) {
        setLoading(true);
        TwitterConfig twitterConfig = ApplicationConfig.getInstance().getTwitterConfig();

        final LiveData<TwitterUserResponse> asyncResponse;
        if (twitterConfig.isApplicationAuthorizationEnabled() && getExternalID() != null) {
            TwitterApplicationAuthorizationStrategy authorization = new TwitterApplicationAuthorizationStrategy();
            TwitterGetUserRequest request = TwitterGetUserRequest.builder()
                    .userID(getExternalID())
                    .authorizationStrategy(authorization)
                    .build();
            asyncResponse = TwitterClient.getUser(request);
        } else {
            TwitterVerifyCredentialsRequest request = TwitterVerifyCredentialsRequest.builder()
                    .accessToken(getAccessToken())
                    .secretToken(getSecretToken())
                    .build();
            asyncResponse = TwitterClient.verifyCredentials(request);
        }

        final TwitterAccount instance = this;
        asyncResponse.observeForever(new Observer<TwitterUserResponse>() {
            @Override
            public void onChanged(@Nullable TwitterUserResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        createFromResponse(response);
                        setLoading(false);
                        if (getInternalID() != null)
                            updateInDatabase();
                        listener.onSynchronized(instance);
                    } else {
                        setLoading(false);
                        listener.onError(instance, response.getErrorString());
                    }
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }
}
