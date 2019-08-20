package com.antonina.socialsynchro.services.twitter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.content.ChildPostContainer;
import com.antonina.socialsynchro.content.ParentPostContainer;
import com.antonina.socialsynchro.database.tables.ITable;
import com.antonina.socialsynchro.services.twitter.requests.TwitterCreateContentRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterRemoveContentRequest;
import com.antonina.socialsynchro.services.twitter.responses.TwitterContentResponse;

public class TwitterPostContainer extends ChildPostContainer {
    private static final int MAX_CONTENT_LENGTH = 140;

    public TwitterPostContainer(ITable data) {
        super(data);
    }

    public TwitterPostContainer(Account account) {
        super(account);
    }

    public TwitterPostContainer(ParentPostContainer parent) {
        super(parent);
        lock();
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public void setTitle(String title) {
        return;
    }

    @Override
    public void setContent(String content) {
        if (content.length() > MAX_CONTENT_LENGTH) {
            unlock();
            content = content.substring(0, content.length() - 3) + "...";
        }
        super.setContent(content);
    }

    @Override
    public void lock() {
        String content = parent.getContent();
        super.lock();
        if (content.length() > MAX_CONTENT_LENGTH) {
            unlock();
            content = content.substring(0, content.length() - 3) + "...";
            super.setContent(content);
        }
    }

    @Override
    public void publish() {
        TwitterClient client = TwitterClient.getInstance();
        TwitterCreateContentRequest request = TwitterCreateContentRequest.builder()
                .status(getContent())
                .accessToken(getAccount().getAccessToken())
                .secretToken(getAccount().getSecretToken())
                .build();
        final TwitterPostContainer instance = this;
        final LiveData<TwitterContentResponse> asyncResponse = client.createContent(request);
        asyncResponse.observeForever(new Observer<TwitterContentResponse>() {
            @Override
            public void onChanged(@Nullable TwitterContentResponse response) {
                instance.setServiceExternalIdentifier(response.getID());
                asyncResponse.removeObserver(this);
            }
        });
    }

    @Override
    public void remove() {
        TwitterClient client = TwitterClient.getInstance();
        TwitterRemoveContentRequest request = TwitterRemoveContentRequest.builder()
                .id(getServiceExternalIdentifier())
                .accessToken(getAccount().getAccessToken())
                .secretToken(getAccount().getSecretToken())
                .build();
        final TwitterPostContainer instance = this;
        final LiveData<TwitterContentResponse> asyncResponse = client.removeContent(request);
        asyncResponse.observeForever(new Observer<TwitterContentResponse>() {
            @Override
            public void onChanged(@Nullable TwitterContentResponse response) {
                if (response.getID().equals(getServiceExternalIdentifier()))
                    instance.setServiceExternalIdentifier(null);
                asyncResponse.removeObserver(this);
            }
        });
    }
}
