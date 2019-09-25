package com.antonina.socialsynchro.services.twitter.content;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.common.content.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.content.attachments.Attachment;
import com.antonina.socialsynchro.common.gui.listeners.OnAttachmentUploadedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnPublishedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnUnpublishedListener;
import com.antonina.socialsynchro.common.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.common.utils.ApplicationConfig;
import com.antonina.socialsynchro.common.rest.IResponse;
import com.antonina.socialsynchro.services.twitter.utils.TwitterConfig;
import com.antonina.socialsynchro.services.twitter.rest.TwitterClient;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterCreateContentRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterCreateContentWithMediaRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterGetContentRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterRemoveContentRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterUploadAppendRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterUploadFinalizeRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterUploadInitRequest;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterCheckUploadStatusRequest;
import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterApplicationAuthorizationStrategy;
import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterAuthorizationStrategy;
import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterUserAuthorizationStrategy;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterContentResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterUploadAppendResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterUploadFinalizeResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterUploadInitResponse;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterCheckUploadStatusResponse;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TwitterPostContainer extends ChildPostContainer {
    private static final int FILE_CHUNK_SIZE_BYTES = 2097152;
    private static final int MAX_CONTENT_LENGTH = 140;
    private static final int MAX_IMAGE_SIZE_MB = 5;
    private static final int MAX_GIF_SIZE_MB = 15;
    private static final int MAX_GIF_WIDTH = 1280;
    private static final int MAX_GIF_HEIGHT = 1080;
    private static final int MAX_GIF_FRAME_NUMBER = 350;
    private static final int MAX_GIF_PIXEL_NUMBER = 300000000;
    private static final int MAX_VIDEO_SIZE_MB = 512;
    private static final int MAX_VIDEO_DURATION_SEC = 140;
    private static final int MIN_VIDEO_DURATION_SEC = 1;
    private static final int MAX_IMAGE_NUMBER = 4;
    private static final int MAX_GIF_NUMBER = 1;
    private static final int MAX_VIDEO_NUMBER = 1;

    private int attachmentsPublished = 0;
    private int imageNumber = 0;
    private int gifNumber = 0;
    private int videoNumber = 0;

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
    public void setTitle(String title) { }

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
    public void publish(OnPublishedListener publishListener, OnAttachmentUploadedListener attachmentListener) {
        setLoading(true);
        if (!getAttachments().isEmpty())
            for (Attachment attachment : getAttachments())
                uploadAttachment(attachment, publishListener, attachmentListener);
        else
            publishJustContent(publishListener);
    }

    @Override
    public void createFromResponse(IResponse response) {
        TwitterContentResponse twitterResponse = (TwitterContentResponse)response;
        setSynchronizationDate(Calendar.getInstance().getTime());
        //TODO: pobrać statystyki
    }

    private void publishJustContent(final OnPublishedListener publishListener) {
        TwitterCreateContentRequest request = TwitterCreateContentRequest.builder()
                .accessToken(getAccount().getAccessToken())
                .secretToken(getAccount().getSecretToken())
                .status(getContent())
                .build();
        final TwitterPostContainer instance = this;
        final LiveData<TwitterContentResponse> asyncResponse = TwitterClient.createContent(request);
        asyncResponse.observeForever(new Observer<TwitterContentResponse>() {
            @Override
            public void onChanged(@Nullable TwitterContentResponse response) {
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

        TwitterCreateContentWithMediaRequest request = TwitterCreateContentWithMediaRequest.builder()
                .accessToken(getAccount().getAccessToken())
                .secretToken(getAccount().getSecretToken())
                .status(getContent())
                .mediaIDs(mediaIDs)
                .build();
        final TwitterPostContainer instance = this;
        final LiveData<TwitterContentResponse> asyncResponse = TwitterClient.createContentWithMedia(request);
        asyncResponse.observeForever(new Observer<TwitterContentResponse>() {
            @Override
            public void onChanged(@Nullable TwitterContentResponse response) {
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
        final long fileSize = attachment.getSizeBytes();
        TwitterUploadInitRequest request = TwitterUploadInitRequest.builder()
                .accessToken(getAccount().getAccessToken())
                .secretToken(getAccount().getSecretToken())
                .mediaType(attachment.getMIMEType())
                .totalBytes(fileSize)
                .build();
        final TwitterPostContainer instance = this;
        final LiveData<TwitterUploadInitResponse> asyncResponse = TwitterClient.uploadInit(request);
        asyncResponse.observeForever(new Observer<TwitterUploadInitResponse>() {
            @Override
            public void onChanged(@Nullable TwitterUploadInitResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        attachment.setExternalID(response.getMediaID());
                        attachmentListener.onInitialized(attachment);

                        long chunkStart = 0;
                        long chunkEnd = fileSize > FILE_CHUNK_SIZE_BYTES ?  FILE_CHUNK_SIZE_BYTES - 1: fileSize - 1;
                        int chunkStep = 0;

                        uploadAppend(attachment, chunkStep, chunkStart, chunkEnd, publishListener, attachmentListener);
                    } else {
                        setLoading(false);
                        attachmentListener.onError(attachment, response.getErrorString());
                        publishListener.onError(instance, "Initialization error: " + response.getErrorString());
                    }
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    private void uploadAppend(final Attachment attachment, final int chunkStep, final long chunkStart, final long chunkEnd, final OnPublishedListener publishListener, final OnAttachmentUploadedListener attachmentListener) {
        TwitterUploadAppendRequest request = TwitterUploadAppendRequest.builder()
                .accessToken(getAccount().getAccessToken())
                .secretToken(getAccount().getSecretToken())
                .mediaID(attachment.getExternalID())
                .segmentIndex(chunkStep)
                .media(attachment.getChunkRequestBody(chunkStart, chunkEnd))
                .build();
        final TwitterPostContainer instance = this;
        final LiveData<TwitterUploadAppendResponse> asyncResponse = TwitterClient.uploadAppend(request);
        asyncResponse.observeForever(new Observer<TwitterUploadAppendResponse>() {
            @Override
            public void onChanged(@Nullable TwitterUploadAppendResponse response) {
                if (response == null) {
                    long fileSize = attachment.getSizeBytes();
                    int progress = (int)((double)(chunkEnd + 1) / fileSize * 100);
                    attachment.setUploadProgress(progress);
                    attachment.notifyListener();
                    attachmentListener.onProgress(attachment);
                    long remaining = fileSize - chunkEnd - 1;
                    if (remaining > 0) {
                        long nextChunkStart = chunkStart + FILE_CHUNK_SIZE_BYTES;
                        long nextChunkEnd = remaining > FILE_CHUNK_SIZE_BYTES ? chunkEnd + FILE_CHUNK_SIZE_BYTES : fileSize - 1;
                        int nextChunkStep = chunkStep + 1;
                        uploadAppend(attachment, nextChunkStep, nextChunkStart, nextChunkEnd, publishListener, attachmentListener);
                    } else
                        uploadFinalize(attachment, publishListener, attachmentListener);
                } else {
                    setLoading(false);
                    attachment.setExternalID(null);
                    attachmentListener.onError(attachment, response.getErrorString());
                    publishListener.onError(instance, response.getErrorString());
                }
                asyncResponse.removeObserver(this);
            }
        });
    }

    private void uploadFinalize(final Attachment attachment, final OnPublishedListener publishListener, final OnAttachmentUploadedListener attachmentListener) {
        TwitterUploadFinalizeRequest request = TwitterUploadFinalizeRequest.builder()
                .accessToken(getAccount().getAccessToken())
                .secretToken(getAccount().getSecretToken())
                .mediaID(attachment.getExternalID())
                .build();
        final TwitterPostContainer instance = this;
        final LiveData<TwitterUploadFinalizeResponse> asyncResponse = TwitterClient.uploadFinalize(request);
        asyncResponse.observeForever(new Observer<TwitterUploadFinalizeResponse>() {
            @Override
            public void onChanged(@Nullable TwitterUploadFinalizeResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        attachment.setExternalID(response.getMediaID());
                        TwitterUploadFinalizeResponse.ProcessingInfo info = response.getProcessingInfo();
                        if (info == null)
                            finishAttachmentUpload(attachment, publishListener, attachmentListener);
                        else {
                            Timer timer = new Timer();
                            TimerTask timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    checkUploadStatus(attachment, publishListener, attachmentListener);
                                    cancel();
                                }
                            };
                            timer.schedule(timerTask, info.getCheckAfterSecs() * 1000);
                        }
                    } else {
                        setLoading(false);
                        attachment.setExternalID(null);
                        attachmentListener.onError(attachment, response.getErrorString());
                        publishListener.onError(instance, response.getErrorString());
                    }
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    private void checkUploadStatus(final Attachment attachment, final OnPublishedListener publishListener, final OnAttachmentUploadedListener attachmentListener) {
        TwitterCheckUploadStatusRequest request = TwitterCheckUploadStatusRequest.builder()
                .accessToken(getAccount().getAccessToken())
                .secretToken(getAccount().getSecretToken())
                .mediaID(attachment.getExternalID())
                .build();
        final TwitterPostContainer instance = this;
        final LiveData<TwitterCheckUploadStatusResponse> asyncResponse = TwitterClient.checkUploadStatus(request);
        asyncResponse.observeForever(new Observer<TwitterCheckUploadStatusResponse>() {
            @Override
            public void onChanged(@Nullable TwitterCheckUploadStatusResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        TwitterCheckUploadStatusResponse.ProcessingInfo info = response.getProcessingInfo();
                        switch (info.getState()) {
                            case "succeeded":
                                finishAttachmentUpload(attachment, publishListener, attachmentListener);
                                break;
                            case "failed":
                                attachment.setLoading(false);
                                attachment.setExternalID(null);
                                attachmentListener.onError(attachment, info.getErrorString());
                                publishListener.onError(instance, info.getErrorString());
                                break;
                            case "in_progress":
                                attachment.setUploadProgress(info.getProgressPercent());
                                attachment.notifyListener();
                                Timer timer = new Timer();
                                TimerTask timerTask = new TimerTask() {
                                    @Override
                                    public void run() {
                                        checkUploadStatus(attachment, publishListener, attachmentListener);
                                        cancel();
                                    }
                                };
                                timer.schedule(timerTask, info.getCheckAfterSecs() * 1000);
                                break;
                        }
                    } else {
                        setLoading(false);
                        attachment.setExternalID(null);
                        attachmentListener.onError(attachment, response.getErrorString());
                        publishListener.onError(instance, "Status check error: " + response.getErrorString());
                    }
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
        TwitterRemoveContentRequest request = TwitterRemoveContentRequest.builder()
                .id(getExternalID())
                .accessToken(getAccount().getAccessToken())
                .secretToken(getAccount().getSecretToken())
                .build();
        final TwitterPostContainer instance = this;
        final LiveData<TwitterContentResponse> asyncResponse = TwitterClient.removeContent(request);
        asyncResponse.observeForever(new Observer<TwitterContentResponse>() {
            @Override
            public void onChanged(@Nullable TwitterContentResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        if (response.getID().equals(getExternalID())) {
                            instance.setExternalID(null);
                            listener.onUnpublished(instance);
                        }
                    } else {
                        listener.onError(instance, response.getErrorString());
                    }
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    @Override
    public void synchronize(final OnSynchronizedListener listener) {
        setLoading(true);
        TwitterConfig twitterConfig = ApplicationConfig.getInstance().getTwitterConfig();
        TwitterAuthorizationStrategy authorization;
        if (twitterConfig.isApplicationAuthorizationEnabled())
            authorization = new TwitterApplicationAuthorizationStrategy();
        else
            authorization = new TwitterUserAuthorizationStrategy(getAccount().getAccessToken(), getAccount().getSecretToken());

        TwitterGetContentRequest request = TwitterGetContentRequest.builder()
                .id(getExternalID())
                .authorizationStrategy(authorization)
                .build();
        final TwitterPostContainer instance = this;
        final LiveData<TwitterContentResponse> asyncResponse = TwitterClient.getContent(request);
        asyncResponse.observeForever(new Observer<TwitterContentResponse>() {
            @Override
            public void onChanged(@Nullable TwitterContentResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        createFromResponse(response);
                        setLoading(false);
                        listener.onSynchronized(instance);
                    } else {
                        setLoading(false);
                        listener.onError(instance, response.getErrorString());
                    }
                }
            }
        });
    }
}
