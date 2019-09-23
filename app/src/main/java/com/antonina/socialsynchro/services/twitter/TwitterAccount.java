package com.antonina.socialsynchro.services.twitter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.database.repositories.TwitterAccountInfoRepository;
import com.antonina.socialsynchro.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.database.tables.TwitterAccountInfoTable;
import com.antonina.socialsynchro.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.services.IResponse;
import com.antonina.socialsynchro.services.ServiceID;
import com.antonina.socialsynchro.services.Services;
import com.antonina.socialsynchro.services.twitter.requests.TwitterVerifyCredentialsRequest;
import com.antonina.socialsynchro.services.twitter.responses.TwitterVerifyCredentialsResponse;

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
        TwitterVerifyCredentialsResponse twitterResponse = (TwitterVerifyCredentialsResponse)response;
        setExternalID(twitterResponse.getID());
        setName(twitterResponse.getName());
        setProfilePictureURL(twitterResponse.getProfilePictureURL());
    }

    @Override
    public void synchronize(final OnSynchronizedListener listener) {
        setLoading(true);
        notifyListener();
        super.synchronize(listener);
        TwitterClient client = TwitterClient.getInstance();
        TwitterVerifyCredentialsRequest request = TwitterVerifyCredentialsRequest.builder()
                .accessToken(getAccessToken())
                .secretToken(getSecretToken())
                .build();
        final TwitterAccount instance = this;
        final LiveData<TwitterVerifyCredentialsResponse> asyncResponse = client.verifyCredentials(request);
        asyncResponse.observeForever(new Observer<TwitterVerifyCredentialsResponse>() {
            @Override
            public void onChanged(@Nullable TwitterVerifyCredentialsResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        createFromResponse(response);
                        setLoading(false);
                        notifyListener();
                        if (getInternalID() != null)
                            updateInDatabase();
                        listener.onSynchronized(instance);
                    } else {
                        setLoading(false);
                        notifyListener();
                        listener.onError(instance, response.getErrorString());
                    }
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }
}
