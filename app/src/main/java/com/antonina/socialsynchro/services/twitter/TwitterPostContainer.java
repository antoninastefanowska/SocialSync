package com.antonina.socialsynchro.services.twitter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.content.ChildPostContainer;
import com.antonina.socialsynchro.content.OnPublishedListener;
import com.antonina.socialsynchro.content.OnUnpublishedListener;
import com.antonina.socialsynchro.content.ParentPostContainer;
import com.antonina.socialsynchro.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.services.twitter.requests.TwitterCreateContentRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterRemoveContentRequest;
import com.antonina.socialsynchro.services.twitter.responses.TwitterContentResponse;

public class TwitterPostContainer extends ChildPostContainer {
    private static final int MAX_CONTENT_LENGTH = 140;

    public TwitterPostContainer(IDatabaseTable data) {
        super(data);
    }

    public TwitterPostContainer(TwitterAccount account) {
        super(account);
    }

    @Override
    public TwitterAccount getAccount() {
        return (TwitterAccount)super.getAccount();
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
    public void publish(OnPublishedListener listener) {
        setLoading(true);
        TwitterClient client = TwitterClient.getInstance();
        TwitterCreateContentRequest request = TwitterCreateContentRequest.builder()
                .status(getContent())
                .accessToken(getAccount().getAccessToken())
                .secretToken(getAccount().getSecretToken())
                .build();
        final OnPublishedListener onPublishedListener = listener;
        final TwitterPostContainer instance = this;
        final LiveData<TwitterContentResponse> asyncResponse = client.createContent(request);
        asyncResponse.observeForever(new Observer<TwitterContentResponse>() {
            @Override
            public void onChanged(@Nullable TwitterContentResponse response) {
                instance.setExternalID(response.getID());
                setLoading(false);
                onPublishedListener.onPublished(instance, response.getErrorString());
                asyncResponse.removeObserver(this);
            }
        });
    }

    @Override
    public void unpublish(OnUnpublishedListener listener) {
        TwitterClient client = TwitterClient.getInstance();
        TwitterRemoveContentRequest request = TwitterRemoveContentRequest.builder()
                .id(getExternalID())
                .accessToken(getAccount().getAccessToken())
                .secretToken(getAccount().getSecretToken())
                .build();
        final OnUnpublishedListener onUnpublishedListener = listener;
        final TwitterPostContainer instance = this;
        final LiveData<TwitterContentResponse> asyncResponse = client.removeContent(request);
        asyncResponse.observeForever(new Observer<TwitterContentResponse>() {
            @Override
            public void onChanged(@Nullable TwitterContentResponse response) {
                if (response.getID().equals(getExternalID()))
                    instance.setExternalID(null);
                onUnpublishedListener.onUnpublished(instance, response.getErrorString());
                asyncResponse.removeObserver(this);
            }
        });
    }
}
