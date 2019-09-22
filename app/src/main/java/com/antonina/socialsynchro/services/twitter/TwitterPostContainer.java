package com.antonina.socialsynchro.services.twitter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.content.ChildPostContainer;
import com.antonina.socialsynchro.content.attachments.Attachment;
import com.antonina.socialsynchro.gui.listeners.OnAttachmentUploadedListener;
import com.antonina.socialsynchro.gui.listeners.OnPublishedListener;
import com.antonina.socialsynchro.gui.listeners.OnUnpublishedListener;
import com.antonina.socialsynchro.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.services.IResponse;
import com.antonina.socialsynchro.services.twitter.requests.TwitterCreateContentRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterRemoveContentRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterUploadAppendRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterUploadFinalizeRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterUploadInitRequest;
import com.antonina.socialsynchro.services.twitter.requests.TwitterCheckUploadStatusRequest;
import com.antonina.socialsynchro.services.twitter.responses.TwitterContentResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterUploadAppendResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterUploadFinalizeResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterUploadInitResponse;
import com.antonina.socialsynchro.services.twitter.responses.TwitterCheckUploadStatusResponse;

import java.util.ArrayList;
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

    private int attachmentsPublished = 0;
    private transient OnPublishedListener publishListener;

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
    public void publish(OnPublishedListener listener, OnAttachmentUploadedListener attachmentListener) {
        this.publishListener = listener;
        setLoading(true);
        if (!getAttachments().isEmpty())
            for (Attachment attachment : getAttachments())
                uploadAttachment(attachment, attachmentListener);
        else
            publishJustContent();
    }

    @Override
    public void createFromResponse(IResponse response) {
        //TODO
    }

    private void publishJustContent() {
        TwitterClient client = TwitterClient.getInstance();
        TwitterCreateContentRequest request = TwitterCreateContentRequest.builder()
                .accessToken(getAccount().getAccessToken())
                .secretToken(getAccount().getSecretToken())
                .status(getContent())
                .build();
        final TwitterPostContainer instance = this;
        final LiveData<TwitterContentResponse> asyncResponse = client.createContent(request);
        asyncResponse.observeForever(new Observer<TwitterContentResponse>() {
            @Override
            public void onChanged(@Nullable TwitterContentResponse response) {
                setLoading(false);
                if (response.getErrorString() == null) {
                    instance.setExternalID(response.getID());
                    publishListener.onPublished(instance);
                } else {
                    publishListener.onError(instance, response.getErrorString());
                }
                asyncResponse.removeObserver(this);
            }
        });
    }

    private void publishWithAttachments() {
        List<String> mediaIDs = new ArrayList<String>();
        for (Attachment attachment : getAttachments())
            mediaIDs.add(attachment.getExternalID());

        TwitterClient client = TwitterClient.getInstance();
        TwitterCreateContentRequest request = TwitterCreateContentRequest.builder()
                .accessToken(getAccount().getAccessToken())
                .secretToken(getAccount().getSecretToken())
                .status(getContent())
                .mediaIDs(mediaIDs)
                .build();
        final TwitterPostContainer instance = this;
        final LiveData<TwitterContentResponse> asyncResponse = client.createContent(request);
        asyncResponse.observeForever(new Observer<TwitterContentResponse>() {
            @Override
            public void onChanged(@Nullable TwitterContentResponse response) {
                setLoading(false);
                if (response.getErrorString() == null) {
                    instance.setExternalID(response.getID());
                    publishListener.onPublished(instance);
                } else {
                    publishListener.onError(instance, response.getErrorString());
                }
                asyncResponse.removeObserver(this);
            }
        });
    }

    private void uploadAttachment(final Attachment attachment, final OnAttachmentUploadedListener listener) {
        attachment.setLoading(true);
        final long fileSize = attachment.getSizeBytes();
        final TwitterClient client = TwitterClient.getInstance();
        TwitterUploadInitRequest request = TwitterUploadInitRequest.builder()
                .accessToken(getAccount().getAccessToken())
                .secretToken(getAccount().getSecretToken())
                .mediaType(attachment.getMIMEType())
                .totalBytes(fileSize)
                .build();
        final TwitterPostContainer instance = this;
        final LiveData<TwitterUploadInitResponse> asyncResponse = client.uploadInit(request);
        asyncResponse.observeForever(new Observer<TwitterUploadInitResponse>() {
            @Override
            public void onChanged(@Nullable TwitterUploadInitResponse response) {
                if (response.getErrorString() == null) {
                    attachment.setExternalID(response.getMediaID());
                    listener.onInitialized(attachment);

                    long chunkStart = 0;
                    long chunkEnd = fileSize < FILE_CHUNK_SIZE_BYTES ? fileSize - 1 : FILE_CHUNK_SIZE_BYTES - 1;
                    int chunkStep = 0;

                    uploadAppend(attachment, chunkStep, chunkStart, chunkEnd, listener);
                } else {
                    setLoading(false);
                    listener.onError(attachment, response.getErrorString());
                    publishListener.onError(instance, "Initialization error: " + response.getErrorString());
                }
                asyncResponse.removeObserver(this);
            }
        });
    }

    private void uploadAppend(final Attachment attachment, final int chunkStep, final long chunkStart, final long chunkEnd, final OnAttachmentUploadedListener listener) {
        TwitterClient client = TwitterClient.getInstance();
        TwitterUploadAppendRequest request = TwitterUploadAppendRequest.builder()
                .accessToken(getAccount().getAccessToken())
                .secretToken(getAccount().getSecretToken())
                .mediaID(attachment.getExternalID())
                .segmentIndex(chunkStep)
                .media(attachment.getChunkRequestBody(chunkStart, chunkEnd))
                .build();
        final TwitterPostContainer instance = this;
        final LiveData<TwitterUploadAppendResponse> asyncResponse = client.uploadAppend(request);
        asyncResponse.observeForever(new Observer<TwitterUploadAppendResponse>() {
            @Override
            public void onChanged(@Nullable TwitterUploadAppendResponse response) {
                if (response == null) {
                    long fileSize = attachment.getSizeBytes();
                    listener.onProgress(attachment, (int)((double)(chunkEnd + 1) / fileSize * 100));
                    long remaining = fileSize - chunkEnd - 1;
                    if (remaining > 0) {
                        long nextChunkStart = chunkStart + FILE_CHUNK_SIZE_BYTES;
                        long nextChunkEnd = remaining > FILE_CHUNK_SIZE_BYTES ? chunkEnd + FILE_CHUNK_SIZE_BYTES : fileSize - 1;
                        int nextChunkStep = chunkStep + 1;
                        uploadAppend(attachment, nextChunkStep, nextChunkStart, nextChunkEnd, listener);
                    } else
                        uploadFinalize(attachment, listener);
                } else {
                    setLoading(false);
                    attachment.setExternalID(null);
                    listener.onError(attachment, response.getErrorString());
                    publishListener.onError(instance, response.getErrorString());
                }
                asyncResponse.removeObserver(this);
            }
        });
    }

    private void uploadFinalize(final Attachment attachment, final OnAttachmentUploadedListener listener) {
        TwitterClient client = TwitterClient.getInstance();
        TwitterUploadFinalizeRequest request = TwitterUploadFinalizeRequest.builder()
                .accessToken(getAccount().getAccessToken())
                .secretToken(getAccount().getSecretToken())
                .mediaID(attachment.getExternalID())
                .build();
        final TwitterPostContainer instance = this;
        final LiveData<TwitterUploadFinalizeResponse> asyncResponse = client.uploadFinalize(request);
        asyncResponse.observeForever(new Observer<TwitterUploadFinalizeResponse>() {
            @Override
            public void onChanged(@Nullable TwitterUploadFinalizeResponse response) {
                if (response.getErrorString() == null) {
                    attachment.setExternalID(response.getMediaID());
                    TwitterUploadFinalizeResponse.ProcessingInfo info = response.getProcessingInfo();
                    if (info == null)
                        finishAttachmentUpload(attachment, listener);
                    else {
                        Timer timer = new Timer();
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                checkUploadStatus(attachment, listener);
                                cancel();
                            }
                        };
                        timer.schedule(timerTask, info.getCheckAfterSecs() * 1000);
                    }
                } else {
                    setLoading(false);
                    attachment.setExternalID(null);
                    listener.onError(attachment, response.getErrorString());
                    publishListener.onError(instance, response.getErrorString());
                }
                asyncResponse.removeObserver(this);
            }
        });
    }

    private void checkUploadStatus(final Attachment attachment, final OnAttachmentUploadedListener listener) {
        TwitterClient client = TwitterClient.getInstance();
        TwitterCheckUploadStatusRequest request = TwitterCheckUploadStatusRequest.builder()
                .accessToken(getAccount().getAccessToken())
                .secretToken(getAccount().getSecretToken())
                .mediaID(attachment.getExternalID())
                .build();
        final TwitterPostContainer instance = this;
        final LiveData<TwitterCheckUploadStatusResponse> asyncResponse = client.checkUploadStatus(request);
        asyncResponse.observeForever(new Observer<TwitterCheckUploadStatusResponse>() {
            @Override
            public void onChanged(@Nullable TwitterCheckUploadStatusResponse response) {
                if (response.getErrorString() == null) {
                    TwitterCheckUploadStatusResponse.ProcessingInfo info = response.getProcessingInfo();
                    switch (info.getState()) {
                        case "succeeded":
                            finishAttachmentUpload(attachment, listener);
                            break;
                        case "failed":
                            attachment.setLoading(false);
                            attachment.setExternalID(null);
                            listener.onError(attachment, info.getErrorString());
                            publishListener.onError(instance, info.getErrorString());
                            break;
                        case "in_progress":
                            Timer timer = new Timer();
                            TimerTask timerTask = new TimerTask() {
                                @Override
                                public void run() {
                                    checkUploadStatus(attachment, listener);
                                    cancel();
                                }
                            };
                            timer.schedule(timerTask, info.getCheckAfterSecs() * 1000);
                            break;
                    }
                } else {
                    setLoading(false);
                    attachment.setExternalID(null);
                    listener.onError(attachment, response.getErrorString());
                    publishListener.onError(instance, "Status check error: " + response.getErrorString());
                }
                asyncResponse.removeObserver(this);
            }
        });
    }

    private void finishAttachmentUpload(Attachment attachment, OnAttachmentUploadedListener listener) {
        attachment.setLoading(false);
        listener.onFinished(attachment);
        attachmentsPublished++;
        if (attachmentsPublished >= getAttachments().size()) {
            publishWithAttachments();
            attachmentsPublished = 0;
        }
    }

    @Override
    public void unpublish(final OnUnpublishedListener listener) {
        TwitterClient client = TwitterClient.getInstance();
        TwitterRemoveContentRequest request = TwitterRemoveContentRequest.builder()
                .id(getExternalID())
                .accessToken(getAccount().getAccessToken())
                .secretToken(getAccount().getSecretToken())
                .build();
        final TwitterPostContainer instance = this;
        final LiveData<TwitterContentResponse> asyncResponse = client.removeContent(request);
        asyncResponse.observeForever(new Observer<TwitterContentResponse>() {
            @Override
            public void onChanged(@Nullable TwitterContentResponse response) {
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
        });
    }
}
