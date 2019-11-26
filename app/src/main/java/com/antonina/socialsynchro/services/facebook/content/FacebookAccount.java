package com.antonina.socialsynchro.services.facebook.content;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.content.services.ServiceID;
import com.antonina.socialsynchro.common.content.services.Services;
import com.antonina.socialsynchro.common.content.statistics.AccountStatistic;
import com.antonina.socialsynchro.common.content.statistics.StatisticsContainer;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.rest.IResponse;
import com.antonina.socialsynchro.common.utils.SecurityUtils;
import com.antonina.socialsynchro.services.facebook.database.repositories.FacebookAccountInfoRepository;
import com.antonina.socialsynchro.services.facebook.database.rows.FacebookAccountInfoRow;
import com.antonina.socialsynchro.services.facebook.rest.FacebookClient;
import com.antonina.socialsynchro.services.facebook.rest.authorization.FacebookUserAuthorizationStrategy;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookGetPagePictureRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookGetPageRequest;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookGetPagePictureResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookPageResponse;

import java.util.Calendar;

public class FacebookAccount extends Account {
    private String accessToken;

    private int likeCount;

    public FacebookAccount(IDatabaseRow table) {
        createFromDatabaseRow(table);
        setService(Services.getService(ServiceID.Facebook));
        likeCount = 0;
    }

    public FacebookAccount() {
        setService(Services.getService(ServiceID.Facebook));
        likeCount = 0;
    }

    @Override
    public void createFromDatabaseRow(IDatabaseRow data) {
        super.createFromDatabaseRow(data);

        FacebookAccountInfoRepository repository = FacebookAccountInfoRepository.getInstance();
        final FacebookAccount instance = this;
        final LiveData<FacebookAccountInfoRow> dataTable = repository.getDataTableByID(data.getID());
        dataTable.observeForever(new Observer<FacebookAccountInfoRow>() {
            @Override
            public void onChanged(@Nullable FacebookAccountInfoRow data) {
                if (data != null) {
                    instance.setAccessToken(SecurityUtils.decrypt(data.accessToken));
                    instance.setLikeCount(data.likeCount);
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

    public int getLikeCount() {
        return likeCount;
    }

    private void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    @Override
    public void saveInDatabase() {
        super.saveInDatabase();
        FacebookAccountInfoRepository repository = FacebookAccountInfoRepository.getInstance();
        if (!updated)
            repository.insert(this);
        else
            updateInDatabase();
    }

    @Override
    public void updateInDatabase() {
        FacebookAccountInfoRepository repository = FacebookAccountInfoRepository.getInstance();
        repository.update(this);
        super.updateInDatabase();
    }

    @Override
    public void deleteFromDatabase() {
        FacebookAccountInfoRepository repository = FacebookAccountInfoRepository.getInstance();
        repository.delete(this);
        super.deleteFromDatabase();
    }

    @Override
    public void createFromResponse(IResponse response) {
        FacebookPageResponse facebookResponse = (FacebookPageResponse)response;
        setSynchronizationDate(Calendar.getInstance().getTime());
        setAccessToken(facebookResponse.getAccessToken());
        setExternalID(facebookResponse.getID());
        setName(facebookResponse.getName());
        setLikeCount(facebookResponse.getFanCount());
    }

    @Override
    public void synchronize(final OnSynchronizedListener listener) {
        setLoading(true);
        FacebookUserAuthorizationStrategy authorization = new FacebookUserAuthorizationStrategy(accessToken);
        FacebookGetPageRequest request = FacebookGetPageRequest.builder()
                .pageID(getExternalID())
                .addField("name")
                .addField("fan_count")
                .addField("access_token")
                .authorizationStrategy(authorization)
                .build();
        final FacebookAccount instance = this;
        final LiveData<FacebookPageResponse> asyncResponse = FacebookClient.getPage(request);
        asyncResponse.observeForever(new Observer<FacebookPageResponse>() {
            @Override
            public void onChanged(@Nullable FacebookPageResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        createFromResponse(response);
                        loadPicture(listener);
                    } else {
                        setLoading(false);
                        listener.onError(instance, response.getErrorString());
                    }
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    private void loadPicture(final OnSynchronizedListener listener) {
        FacebookUserAuthorizationStrategy authorization = new FacebookUserAuthorizationStrategy(accessToken);
        FacebookGetPagePictureRequest request = FacebookGetPagePictureRequest.builder()
                .pageID(getExternalID())
                .authorizationStrategy(authorization)
                .build();
        final FacebookAccount instance = this;
        final LiveData<FacebookGetPagePictureResponse> asyncResponse = FacebookClient.getPagePicture(request);
        asyncResponse.observeForever(new Observer<FacebookGetPagePictureResponse>() {
            @Override
            public void onChanged(@Nullable FacebookGetPagePictureResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        setProfilePictureURL(response.getURL());
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

    @Override
    public void synchronizeRequestLimits(OnSynchronizedListener listener) {
        //TODO
    }

    @Override
    public AccountStatistic getStatistic() {
        return new AccountStatistic("Likes", likeCount, getProfilePictureURL(), getName(), getService().getPanelBackgroundID());
    }
}
