package com.antonina.socialsynchro.services.twitter.content;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.common.content.statistics.AccountStatistic;
import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.rest.RequestLimit;
import com.antonina.socialsynchro.common.utils.ConvertUtils;
import com.antonina.socialsynchro.common.utils.SecurityUtils;
import com.antonina.socialsynchro.services.twitter.database.repositories.TwitterAccountInfoRepository;
import com.antonina.socialsynchro.services.twitter.database.rows.TwitterAccountInfoRow;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.rest.IResponse;
import com.antonina.socialsynchro.common.content.services.ServiceID;
import com.antonina.socialsynchro.common.content.services.Services;
import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterUserAuthorizationStrategy;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterGetRateLimitsRequest;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterGetRateLimitsResponse;
import com.antonina.socialsynchro.services.twitter.rest.TwitterClient;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterGetUserRequest;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterUserResponse;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TwitterAccount extends Account {
    private String accessToken;
    private String secretToken;

    private int followerCount;

    public TwitterAccount(IDatabaseRow table) {
        createFromDatabaseRow(table);
        setService(Services.getService(ServiceID.Twitter));
        followerCount = 0;
    }

    public TwitterAccount() {
        setService(Services.getService(ServiceID.Twitter));
        followerCount = 0;
    }

    @Override
    public void createFromDatabaseRow(IDatabaseRow data) {
        super.createFromDatabaseRow(data);

        TwitterAccountInfoRepository repository = TwitterAccountInfoRepository.getInstance();
        final TwitterAccount instance = this;
        final LiveData<TwitterAccountInfoRow> dataTable = repository.getDataRowByID(data.getID());
        dataTable.observeForever(new Observer<TwitterAccountInfoRow>() {
            @Override
            public void onChanged(@Nullable TwitterAccountInfoRow data) {
                if (data != null) {
                    instance.setAccessToken(SecurityUtils.decrypt(data.accessToken));
                    instance.setSecretToken(SecurityUtils.decrypt(data.secretToken));
                    instance.setFollowerCount(data.followerCount);
                    dataTable.removeObserver(this);
                }
            }
        });
    }

    public String getAccessToken() { return accessToken; }

    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getSecretToken() { return secretToken; }

    public void setSecretToken(String secretToken) { this.secretToken = secretToken; }

    public int getFollowerCount() {
        return followerCount;
    }

    private void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    @Override
    public void saveInDatabase() {
        if (getInternalID() != null)
            updateInDatabase();
        else  {
            super.saveInDatabase();
            TwitterAccountInfoRepository repository = TwitterAccountInfoRepository.getInstance();
            if (!updated)
                repository.insert(this);
            else
                updateInDatabase();
        }
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
        setFollowerCount(twitterResponse.getFollowersCount());
    }

    @Override
    public void synchronize(final OnSynchronizedListener listener) {
        String endpoint = TwitterGetUserRequest.getRequestEndpoint();
        final RequestLimit requestLimit = getRequestLimit(endpoint);

        if (requestLimit == null || requestLimit.getRemaining() > 0) {
            setLoading(true);
            TwitterUserAuthorizationStrategy authorization = new TwitterUserAuthorizationStrategy(getAccessToken(), getSecretToken());
            TwitterGetUserRequest request = TwitterGetUserRequest.builder()
                    .userID(getExternalID())
                    .authorizationStrategy(authorization)
                    .build();
            final TwitterAccount instance = this;
            final LiveData<TwitterUserResponse> asyncResponse = TwitterClient.getUser(request);
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
                            synchronizeRequestLimits(listener);
                        } else {
                            setLoading(false);
                            listener.onError(instance, response.getErrorString());
                        }
                        if (requestLimit != null)
                            requestLimit.decrement();
                        notifyGUI();
                        asyncResponse.removeObserver(this);
                    }
                }
            });
        }
        else {
            listener.onError(this, ConvertUtils.requestLimitWaitMessage(requestLimit.getSecondsUntilReset()));
        }
    }

    public void synchronizeRequestLimits(final OnSynchronizedListener listener) {
        String endpoint = TwitterGetRateLimitsRequest.getRequestEndpoint();
        final RequestLimit requestLimit = requestLimits.get(endpoint);
        if (requestLimit == null || requestLimit.getRemaining() > 0) {
            TwitterUserAuthorizationStrategy authorization = new TwitterUserAuthorizationStrategy(getAccessToken(), getSecretToken());
            TwitterGetRateLimitsRequest request = TwitterGetRateLimitsRequest.builder()
                    .addResource("users")
                    .addResource("statuses")
                    .addResource("application")
                    .addResource("account")
                    .addResource("media")
                    .authorizationStrategy(authorization)
                    .build();
            final TwitterAccount instance = this;
            final LiveData<TwitterGetRateLimitsResponse> asyncResponse = TwitterClient.getRateLimits(request);
            asyncResponse.observeForever( new Observer<TwitterGetRateLimitsResponse>() {
                @Override
                public void onChanged(@Nullable TwitterGetRateLimitsResponse response) {
                    if (response != null) {
                        Date currentDate = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
                        long currentEpoch = currentDate.getTime() / 1000;
                        if (response.getErrorString() == null) {
                            boolean firstAttempt = requestLimits.isEmpty();
                            for (TwitterGetRateLimitsResponse.RequestLimitResponse requestLimitResponse : response.getRequestLimits()) {
                                RequestLimit requestLimit = firstAttempt ? new RequestLimit() : requestLimits.get(requestLimitResponse.getEndpoint());

                                requestLimit.setEndpoint(requestLimitResponse.getEndpoint());
                                requestLimit.setAccount(instance);
                                requestLimit.setLimit(requestLimitResponse.getLimit());
                                requestLimit.setRemaining(requestLimitResponse.getRemaining());
                                requestLimit.setSecondsUntilReset(currentEpoch - requestLimitResponse.getReset());
                                requestLimit.setResetWindowSeconds(requestLimitResponse.getResetTimeSeconds());
                                requestLimit.setSynchronizationDate(currentDate);

                                if (firstAttempt)
                                    addRequestLimit(requestLimit);

                                requestLimit.saveInDatabase();
                            }
                            listener.onSynchronized(instance);
                        } else {
                            listener.onError(instance, response.getErrorString());
                        }
                        if (requestLimit != null)
                            requestLimit.decrement();
                        asyncResponse.removeObserver(this);
                    }
                }
            });
        } else {
            listener.onError(this, ConvertUtils.requestLimitWaitMessage(requestLimit.getSecondsUntilReset()));
        }
    }

    @Override
    public AccountStatistic getStatistic() {
        return new AccountStatistic("Followers", followerCount, getProfilePictureURL(), getName(), getService().getPanelBackgroundID());
    }

    @Override
    public String getURL() {
        return "https://www.twitter.com/" + getExternalID();
    }
}
