package com.antonina.socialsynchro.services.facebook.content;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.common.content.attachments.Attachment;
import com.antonina.socialsynchro.common.content.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.gui.listeners.OnAttachmentUploadedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnPublishedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnUnpublishedListener;
import com.antonina.socialsynchro.common.rest.IResponse;
import com.antonina.socialsynchro.services.facebook.rest.FacebookClient;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookCreateContentRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookCreateContentWithMediaRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookRemoveContentRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookUploadPhotoRequest;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookIdentifierResponse;

import java.util.ArrayList;
import java.util.List;

public class FacebookPostContainer extends ChildPostContainer {
    private static final int MAX_CONTENT_LENGTH = 63206;

    private int attachmentsPublished = 0;

    public FacebookPostContainer(FacebookAccount account) {
        super(account);
    }

    public FacebookPostContainer(IDatabaseRow data) {
        super(data);
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
    public FacebookAccount getAccount() {
        return (FacebookAccount)super.getAccount();
    }

    @Override
    public void publish(final OnPublishedListener publishListener, final OnAttachmentUploadedListener attachmentListener) {
        setLoading(true);
        if (!getAttachments().isEmpty())
            for (Attachment attachment : getAttachments())
                uploadAttachment(attachment, publishListener, attachmentListener);
        else
            publishJustContent(publishListener);
    }

    private void publishJustContent(final OnPublishedListener publishListener) {
        FacebookCreateContentRequest request = FacebookCreateContentRequest.builder()
                .pageID(getAccount().getExternalID())
                .message(getContent())
                .accessToken(getAccount().getAccessToken())
                .build();
        final FacebookPostContainer instance = this;
        final LiveData<FacebookIdentifierResponse> asyncResponse = FacebookClient.createContent(request);
        asyncResponse.observeForever(new Observer<FacebookIdentifierResponse>() {
            @Override
            public void onChanged(@Nullable FacebookIdentifierResponse response) {
                if (response != null) {
                    setLoading(false);
                    if (response.getErrorString() == null) {
                        instance.setExternalID(response.getID());
                        publishListener.onPublished(instance);
                    } else {
                        publishListener.onError(instance, response.getErrorString());
                    }
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    private void publishWithAttachments(final OnPublishedListener publishListener) {
        List<String> mediaIDs = new ArrayList<>();
        for (Attachment attachment : getAttachments())
            mediaIDs.add(attachment.getExternalID());

        FacebookCreateContentWithMediaRequest request = FacebookCreateContentWithMediaRequest.builder()
                .pageID(getAccount().getExternalID())
                .message(getContent())
                .mediaIDs(mediaIDs)
                .accessToken(getAccount().getAccessToken())
                .build();
        final FacebookPostContainer instance = this;
        final LiveData<FacebookIdentifierResponse> asyncResponse = FacebookClient.createContentWithMedia(request);
        asyncResponse.observeForever(new Observer<FacebookIdentifierResponse>() {
            @Override
            public void onChanged(@Nullable FacebookIdentifierResponse response) {
                if (response != null) {
                    setLoading(false);
                    if (response.getErrorString() == null) {
                        instance.setExternalID(response.getID());
                        publishListener.onPublished(instance);
                    } else {
                        publishListener.onError(instance, response.getErrorString());
                    }
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    private void uploadAttachment(final Attachment attachment, final OnPublishedListener publishListener, final OnAttachmentUploadedListener attachmentListener) {
        attachment.setUploadProgress(0);
        attachment.setLoading(true);
        FacebookUploadPhotoRequest request = FacebookUploadPhotoRequest.builder()
                .pageID(getAccount().getExternalID())
                .photo(attachment.getPart())
                .accessToken(getAccount().getAccessToken())
                .build();
        final FacebookPostContainer instance = this;
        final LiveData<FacebookIdentifierResponse> asyncResponse = FacebookClient.uploadPhoto(request);
        asyncResponse.observeForever(new Observer<FacebookIdentifierResponse>() {
            @Override
            public void onChanged(@Nullable FacebookIdentifierResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        attachment.setExternalID(response.getID());
                        finishAttachmentUpload(attachment, publishListener, attachmentListener);
                    } else {
                        attachment.setLoading(false);
                        setLoading(false);
                        publishListener.onError(instance, response.getErrorString());
                        attachmentListener.onError(attachment, response.getErrorString());
                    }
                    attachment.setLoading(false);
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    private void finishAttachmentUpload(Attachment attachment, OnPublishedListener publishListener, OnAttachmentUploadedListener attachmentListener) {
        attachment.setUploadProgress(100);
        attachment.setLoading(false);
        attachmentListener.onFinished(attachment);
        attachmentsPublished++;
        if (attachmentsPublished >= getAttachments().size()) {
            publishWithAttachments(publishListener);
            attachmentsPublished = 0;
        }
    }

    @Override
    public void unpublish(final OnUnpublishedListener listener) {
        FacebookRemoveContentRequest request = FacebookRemoveContentRequest.builder()
                .postID(getExternalID())
                .accessToken(getAccount().getAccessToken())
                .build();
        final FacebookPostContainer instance = this;
        final LiveData<FacebookIdentifierResponse> asyncResponse = FacebookClient.removeContent(request);
        asyncResponse.observeForever(new Observer<FacebookIdentifierResponse>() {
            @Override
            public void onChanged(@Nullable FacebookIdentifierResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        instance.setExternalID(null);
                        listener.onUnpublished(instance);
                    } else
                        listener.onError(instance, response.getErrorString());
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    @Override
    public void createFromResponse(IResponse response) {
        //TODO
    }

    @Override
    public void synchronize(OnSynchronizedListener listener) {
        //TODO
    }
}
