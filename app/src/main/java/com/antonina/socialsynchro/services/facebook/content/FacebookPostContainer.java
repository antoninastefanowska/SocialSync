package com.antonina.socialsynchro.services.facebook.content;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.antonina.socialsynchro.common.content.attachments.Attachment;
import com.antonina.socialsynchro.common.content.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.content.statistics.ChildGroupStatistic;
import com.antonina.socialsynchro.common.content.statistics.ChildStatistic;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.gui.listeners.OnAttachmentUploadedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnPublishedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnUnpublishedListener;
import com.antonina.socialsynchro.common.rest.IResponse;
import com.antonina.socialsynchro.common.rest.IServiceEntity;
import com.antonina.socialsynchro.services.facebook.database.repositories.FacebookPostInfoRepository;
import com.antonina.socialsynchro.services.facebook.database.rows.FacebookPostInfoRow;
import com.antonina.socialsynchro.services.facebook.rest.FacebookClient;
import com.antonina.socialsynchro.services.facebook.rest.authorization.FacebookUserAuthorizationStrategy;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookCreateContentRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookCreateContentWithMediaRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookPostRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookRemoveContentRequest;
import com.antonina.socialsynchro.services.facebook.rest.requests.FacebookUploadPhotoRequest;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookContentResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookCountResponse;
import com.antonina.socialsynchro.services.facebook.rest.responses.FacebookIdentifierResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FacebookPostContainer extends ChildPostContainer {
    private static final int MAX_CONTENT_LENGTH = 63206;

    private int attachmentsPublished = 0;

    private int reactionCount;
    private int commentCount;

    private boolean reactionCountLoaded = false;
    private boolean commentCountLoaded = false;

    public FacebookPostContainer(FacebookAccount account) {
        super(account);
        reactionCount = 0;
        commentCount = 0;
    }

    public FacebookPostContainer(IDatabaseRow data) {
        super(data);
        reactionCount = 0;
        commentCount = 0;
    }

    @Override
    public void createFromDatabaseRow(IDatabaseRow data) {
        super.createFromDatabaseRow(data);

        FacebookPostInfoRepository repository = FacebookPostInfoRepository.getInstance();
        final FacebookPostContainer instance = this;
        final LiveData<FacebookPostInfoRow> dataTable = repository.getDataTableByID(data.getID());
        dataTable.observeForever(new Observer<FacebookPostInfoRow>() {
            @Override
            public void onChanged(@Nullable FacebookPostInfoRow data) {
                if (data != null) {
                    instance.setReactionCount(data.reactionCount);
                    instance.setCommentCount(data.commentCount);
                    notifyGUI();
                    dataTable.removeObserver(this);
                }
            }
        });
    }

    @Override
    public void saveInDatabase() {
        super.saveInDatabase();
        if (getInternalID() != null)
            updateInDatabase();
        else {
            FacebookPostInfoRepository repository = FacebookPostInfoRepository.getInstance();
            repository.insert(this);
        }
    }

    @Override
    public void updateInDatabase() {
        FacebookPostInfoRepository repository = FacebookPostInfoRepository.getInstance();
        repository.update(this);
        super.updateInDatabase();
    }

    @Override
    public void deleteFromDatabase() {
        FacebookPostInfoRepository repository = FacebookPostInfoRepository.getInstance();
        repository.delete(this);
        super.deleteFromDatabase();
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

    public int getReactionCount() {
        return reactionCount;
    }

    private void setReactionCount(int reactionCount) {
        this.reactionCount = reactionCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    private void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public void publish(final OnPublishedListener publishListener, final OnAttachmentUploadedListener attachmentListener) {
        if (!isOnline()) {
            setLoading(true);
            notifyGUI();
            if (!getAttachments().isEmpty())
                for (Attachment attachment : getAttachments())
                    uploadAttachment(attachment, publishListener, attachmentListener);
            else
                publishJustContent(publishListener);
        }
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
                    } else
                        publishListener.onError(instance, response.getErrorString());
                    notifyGUI();
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
                    notifyGUI();
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
                    attachment.notifyGUI();
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    private void finishAttachmentUpload(Attachment attachment, OnPublishedListener publishListener, OnAttachmentUploadedListener attachmentListener) {
        attachment.setUploadProgress(100);
        attachment.setLoading(false);
        attachment.notifyGUI();
        attachmentListener.onFinished(attachment);
        attachmentsPublished++;
        if (attachmentsPublished >= getAttachments().size()) {
            publishWithAttachments(publishListener);
            attachmentsPublished = 0;
        }
    }

    @Override
    public void unpublish(final OnUnpublishedListener listener) {
        if (isOnline()) {
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
                        notifyGUI();
                        asyncResponse.removeObserver(this);
                    }
                }
            });
        }
    }

    @Override
    public void createFromResponse(IResponse response) {
        setSynchronizationDate(Calendar.getInstance().getTime());
        //TODO
    }

    @Override
    public void synchronize(final OnSynchronizedListener listener) {
        if (isOnline()) {
            setLoading(true);
            FacebookUserAuthorizationStrategy authorization = new FacebookUserAuthorizationStrategy(getAccount().getAccessToken());
            final FacebookPostRequest request = FacebookPostRequest.builder()
                    .postID(getExternalID())
                    .authorizationStrategy(authorization)
                    .build();
            final FacebookPostContainer instance = this;
            final LiveData<FacebookContentResponse> asyncResponse = FacebookClient.getContent(request);
            asyncResponse.observeForever(new Observer<FacebookContentResponse>() {
                @Override
                public void onChanged(@Nullable FacebookContentResponse response) {
                    if (response != null) {
                        if (response.getErrorString() == null) {
                            createFromResponse(response);
                            OnSynchronizedListener statisticsListener = new OnSynchronizedListener() {
                                @Override
                                public void onSynchronized(IServiceEntity entity) {
                                    if (reactionCountLoaded && commentCountLoaded) {
                                        listener.onSynchronized(entity);
                                        setLoading(false);
                                        reactionCountLoaded = false;
                                        commentCountLoaded = false;
                                        saveInDatabase();
                                    }
                                }

                                @Override
                                public void onError(IServiceEntity entity, String error) {
                                    listener.onError(entity, error);
                                    setLoading(false);
                                }
                            };
                            loadReactions(statisticsListener, request);
                            loadComments(statisticsListener, request);
                        } else {
                            setLoading(false);
                            listener.onError(instance, response.getErrorString());
                        }
                        notifyGUI();
                        asyncResponse.removeObserver(this);
                    }
                }
            });
        }
    }

    private void loadReactions(final OnSynchronizedListener listener, FacebookPostRequest request) {
        final FacebookPostContainer instance = this;
        final LiveData<FacebookCountResponse> asyncResponse = FacebookClient.getPostReactions(request);
        asyncResponse.observeForever(new Observer<FacebookCountResponse>() {
            @Override
            public void onChanged(@Nullable FacebookCountResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        setReactionCount(response.getTotalCount());
                        reactionCountLoaded = true;
                        listener.onSynchronized(instance);
                    } else {
                        listener.onError(instance, response.getErrorString());
                    }
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    private void loadComments(final OnSynchronizedListener listener, FacebookPostRequest request) {
        final FacebookPostContainer instance = this;
        final LiveData<FacebookCountResponse> asyncResponse = FacebookClient.getPostComments(request);
        asyncResponse.observeForever(new Observer<FacebookCountResponse>() {
            @Override
            public void onChanged(@Nullable FacebookCountResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        setCommentCount(response.getTotalCount());
                        commentCountLoaded = true;
                        listener.onSynchronized(instance);
                    } else {
                        listener.onError(instance, response.getErrorString());
                    }
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    @Override
    public ChildGroupStatistic getStatistic() {
        ChildGroupStatistic groupStatistic = new ChildGroupStatistic(getAccount().getProfilePictureURL(), getAccount().getName());
        groupStatistic.addChildStatistic(new ChildStatistic("Reactions", reactionCount, getService().getPanelBackgroundID()));
        groupStatistic.addChildStatistic(new ChildStatistic("Comments", commentCount, getService().getPanelBackgroundID()));
        return groupStatistic;
    }

    @Override
    public String getURL() {
        if (isOnline())
            return "https://www.facebook.com/" + getExternalID();
        else
            return "";
    }
}
