package com.antonina.socialsynchro.services.twitter.content;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.common.content.attachments.AttachmentTypeID;
import com.antonina.socialsynchro.common.content.attachments.ImageAttachment;
import com.antonina.socialsynchro.common.content.attachments.VideoAttachment;
import com.antonina.socialsynchro.common.content.statistics.ChildGroupStatistic;
import com.antonina.socialsynchro.common.content.statistics.ChildStatistic;
import com.antonina.socialsynchro.common.content.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.content.attachments.Attachment;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.gui.listeners.OnAttachmentUploadedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnPublishedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnUnpublishedListener;
import com.antonina.socialsynchro.common.rest.RequestLimit;
import com.antonina.socialsynchro.common.utils.ApplicationConfig;
import com.antonina.socialsynchro.common.rest.IResponse;
import com.antonina.socialsynchro.common.utils.ConvertUtils;
import com.antonina.socialsynchro.services.backend.BackendClient;
import com.antonina.socialsynchro.services.backend.requests.BackendGetRateLimitsRequest;
import com.antonina.socialsynchro.services.backend.requests.BackendUpdateRequestCounterRequest;
import com.antonina.socialsynchro.services.backend.responses.BackendGetRateLimitsResponse;
import com.antonina.socialsynchro.services.twitter.database.repositories.TwitterPostInfoRepository;
import com.antonina.socialsynchro.services.twitter.database.repositories.TwitterPostOptionsRepository;
import com.antonina.socialsynchro.services.twitter.database.rows.TwitterPostInfoRow;
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
    private static final long MAX_IMAGE_SIZE_BYTES = 5242880;
    private static final long MAX_GIF_SIZE_BYTES = 15728640;
    private static final int MAX_GIF_WIDTH = 1280;
    private static final int MAX_GIF_HEIGHT = 1080;
    private static final int MAX_GIF_FRAME_NUMBER = 350;
    private static final int MAX_GIF_PIXEL_NUMBER = 300000000;
    private static final long MAX_VIDEO_SIZE_BYTES = 536870912;
    private static final int MAX_VIDEO_DURATION_MILLISECONDS = 140000;
    private static final int MIN_VIDEO_DURATION_MILLISECONDS = 500;
    private static final int MAX_IMAGE_NUMBER = 4;
    private static final int MAX_GIF_NUMBER = 1;
    private static final int MAX_VIDEO_NUMBER = 1;

    private int attachmentsPublished = 0;

    private int retweetCount;
    private int favoriteCount;

    public TwitterPostContainer(IDatabaseRow data) {
        super(data);
    }

    public TwitterPostContainer(TwitterAccount account) {
        super(account);
        retweetCount = 0;
        favoriteCount = 0;
    }

    @Override
    public void createFromDatabaseRow(IDatabaseRow data) {
        super.createFromDatabaseRow(data);

        TwitterPostInfoRepository repository = TwitterPostInfoRepository.getInstance();
        final TwitterPostContainer instance = this;
        final LiveData<TwitterPostInfoRow> dataTable = repository.getDataRowByID(data.getID());
        dataTable.observeForever(new Observer<TwitterPostInfoRow>() {
            @Override
            public void onChanged(@Nullable TwitterPostInfoRow data) {
                if (data != null) {
                    instance.setRetweetCount(data.retweetCount);
                    instance.setFavoriteCount(data.favoriteCount);
                    notifyGUI();
                    dataTable.removeObserver(this);
                }
            }
        });
        TwitterPostOptionsRepository optionsRepository = TwitterPostOptionsRepository.getInstance();
        final LiveData<TwitterPostOptions> liveDataOptions = optionsRepository.getDataByID(data.getID());
        liveDataOptions.observeForever(new Observer<TwitterPostOptions>() {
            @Override
            public void onChanged(@Nullable TwitterPostOptions options) {
                setOptions(options);
                notifyGUI();
                liveDataOptions.removeObserver(this);
            }
        });
    }

    @Override
    public TwitterAccount getAccount() {
        return (TwitterAccount)super.getAccount();
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    private void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    private void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    @Override
    public void createFromResponse(IResponse response) {
        TwitterContentResponse twitterResponse = (TwitterContentResponse)response;
        setSynchronizationDate(Calendar.getInstance().getTime());
        setRetweetCount(twitterResponse.getRetweetCount());
        setFavoriteCount(twitterResponse.getFavoriteCount());
   }

    @Override
    public void saveInDatabase() {
        if (getInternalID() != null)
            updateInDatabase();
        else {
            super.saveInDatabase();
            TwitterPostInfoRepository repository = TwitterPostInfoRepository.getInstance();
            repository.insert(this);
        }
    }

    @Override
    public void updateInDatabase() {
        TwitterPostInfoRepository repository = TwitterPostInfoRepository.getInstance();
        repository.update(this);
        super.updateInDatabase();
    }

    @Override
    public void deleteFromDatabase() {
        TwitterPostInfoRepository repository = TwitterPostInfoRepository.getInstance();
        repository.delete(this);
        super.deleteFromDatabase();
    }

    @Override
    public void publish(final OnPublishedListener publishListener, final OnAttachmentUploadedListener attachmentListener) {
        if (!isOnline()) {
            setLoading(true);
            notifyGUI();
            String endpoint = TwitterCreateContentRequest.getRequestEndpoint();
            BackendGetRateLimitsRequest request = BackendGetRateLimitsRequest.builder()
                    .endpoint(endpoint)
                    .serviceName(getService().getName())
                    .build();
            final TwitterPostContainer instance = this;
            final LiveData<BackendGetRateLimitsResponse> asyncResponse = BackendClient.getRateLimits(request);
            asyncResponse.observeForever(new Observer<BackendGetRateLimitsResponse>() {
                @Override
                public void onChanged(@Nullable BackendGetRateLimitsResponse response) {
                    if (response != null) {
                        if (response.getRemaining() > 0) {
                            if (!getAttachments().isEmpty())
                                for (Attachment attachment : getAttachments())
                                    uploadAttachment(attachment, publishListener, attachmentListener);
                            else
                                publishJustContent(publishListener);
                        } else {
                            publishListener.onError(instance, ConvertUtils.requestLimitWaitMessage(response.getRemaining()));
                        }
                        notifyGUI();
                        asyncResponse.removeObserver(this);
                    }
                }
            });
        }
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
                        instance.saveInDatabase();
                        publishListener.onPublished(instance);
                    } else {
                        publishListener.onError(instance, response.getErrorString());
                    }
                    String endpoint = TwitterCreateContentRequest.getRequestEndpoint();
                    BackendUpdateRequestCounterRequest request = BackendUpdateRequestCounterRequest.builder()
                            .endpoint(endpoint)
                            .serviceName(getService().getName())
                            .build();
                    BackendClient.updateRequestCounter(request);
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
                        instance.saveInDatabase();
                        publishListener.onPublished(instance);
                    } else {
                        publishListener.onError(instance, response.getErrorString());
                    }
                    String endpoint = TwitterCreateContentRequest.getRequestEndpoint();
                    BackendUpdateRequestCounterRequest request = BackendUpdateRequestCounterRequest.builder()
                            .endpoint(endpoint)
                            .serviceName(getService().getName())
                            .build();
                    BackendClient.updateRequestCounter(request);
                    notifyGUI();
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    private void uploadAttachment(final Attachment attachment, final OnPublishedListener publishListener, final OnAttachmentUploadedListener attachmentListener) {
        String endpoint = TwitterUploadInitRequest.getRequestEndpoint();
        final RequestLimit requestLimit = getAccount().getRequestLimit(endpoint);
        if (requestLimit == null || requestLimit.getRemaining() > 0) {
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
                            attachment.saveInDatabase();
                            attachmentListener.onInitialized(attachment);

                            long chunkStart = 0;
                            long chunkEnd = fileSize > FILE_CHUNK_SIZE_BYTES ? FILE_CHUNK_SIZE_BYTES - 1 : fileSize - 1;
                            int chunkStep = 0;

                            uploadAppend(attachment, chunkStep, chunkStart, chunkEnd, publishListener, attachmentListener);
                        } else {
                            setLoading(false);
                            attachment.setLoading(false);
                            attachmentListener.onError(attachment, response.getErrorString());
                            publishListener.onError(instance, "Initialization error: " + response.getErrorString());
                        }
                        if (requestLimit != null)
                            requestLimit.decrement();
                        attachment.notifyGUI();
                        asyncResponse.removeObserver(this);
                    }
                }
            });
        } else {
            attachmentListener.onError(attachment, ConvertUtils.requestLimitWaitMessage(requestLimit.getSecondsUntilReset()));
            publishListener.onError(this, ConvertUtils.requestLimitWaitMessage(requestLimit.getSecondsUntilReset()));
        }
    }

    private void uploadAppend(final Attachment attachment, final int chunkStep, final long chunkStart, final long chunkEnd, final OnPublishedListener publishListener, final OnAttachmentUploadedListener attachmentListener) {
        String endpoint = TwitterUploadAppendRequest.getRequestEndpoint();
        final RequestLimit requestLimit = getAccount().getRequestLimit(endpoint);
        if (requestLimit == null || requestLimit.getRemaining() > 0) {
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
                        int progress = (int) ((double) (chunkEnd + 1) / fileSize * 100);
                        attachment.setUploadProgress(progress);
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
                        attachment.saveInDatabase();
                        attachmentListener.onError(attachment, response.getErrorString());
                        publishListener.onError(instance, response.getErrorString());
                    }
                    if (requestLimit != null)
                        requestLimit.decrement();
                    attachment.notifyGUI();
                    asyncResponse.removeObserver(this);
                }
            });
        } else {
            attachmentListener.onError(attachment, ConvertUtils.requestLimitWaitMessage(requestLimit.getSecondsUntilReset()));
            publishListener.onError(this, ConvertUtils.requestLimitWaitMessage(requestLimit.getSecondsUntilReset()));
        }
    }

    private void uploadFinalize(final Attachment attachment, final OnPublishedListener publishListener, final OnAttachmentUploadedListener attachmentListener) {
        String endpoint = TwitterUploadAppendRequest.getRequestEndpoint();
        final RequestLimit requestLimit = getAccount().getRequestLimit(endpoint);
        if (requestLimit == null || requestLimit.getRemaining() > 0) {
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
                            attachment.saveInDatabase();
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
                            attachment.saveInDatabase();
                            attachmentListener.onError(attachment, response.getErrorString());
                            publishListener.onError(instance, response.getErrorString());
                        }
                        if (requestLimit != null)
                            requestLimit.decrement();
                        attachment.notifyGUI();
                        asyncResponse.removeObserver(this);
                    }
                }
            });
        } else {
            attachmentListener.onError(attachment, ConvertUtils.requestLimitWaitMessage(requestLimit.getSecondsUntilReset()));
            publishListener.onError(this, ConvertUtils.requestLimitWaitMessage(requestLimit.getSecondsUntilReset()));
        }
    }

    private void checkUploadStatus(final Attachment attachment, final OnPublishedListener publishListener, final OnAttachmentUploadedListener attachmentListener) {
        String endpoint = TwitterCreateContentRequest.getRequestEndpoint();
        final RequestLimit requestLimit = getAccount().getRequestLimit(endpoint);
        if (requestLimit == null || requestLimit.getRemaining() > 0) {
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
                                    attachment.saveInDatabase();
                                    attachmentListener.onError(attachment, info.getErrorString());
                                    publishListener.onError(instance, info.getErrorString());
                                    break;
                                case "in_progress":
                                    attachment.setUploadProgress(info.getProgressPercent());
                                    attachment.notifyGUI();
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
                            attachment.saveInDatabase();
                            attachmentListener.onError(attachment, response.getErrorString());
                            publishListener.onError(instance, "Status check error: " + response.getErrorString());
                        }
                        if (requestLimit != null)
                            requestLimit.decrement();
                        attachment.notifyGUI();
                        asyncResponse.removeObserver(this);
                    }
                }
            });
        }
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
            setLoading(true);
            final String endpoint = TwitterRemoveContentRequest.getRequestEndpoint();
            BackendGetRateLimitsRequest request = BackendGetRateLimitsRequest.builder()
                    .endpoint(endpoint)
                    .serviceName(getService().getName())
                    .build();
            final TwitterPostContainer instance = this;
            final LiveData<BackendGetRateLimitsResponse> asyncResponse = BackendClient.getRateLimits(request);
            asyncResponse.observeForever(new Observer<BackendGetRateLimitsResponse>() {
                @Override
                public void onChanged(@Nullable BackendGetRateLimitsResponse response) {
                    if (response != null) {
                        if (response.getRemaining() > 0) {
                            TwitterRemoveContentRequest request = TwitterRemoveContentRequest.builder()
                                    .id(getExternalID())
                                    .accessToken(getAccount().getAccessToken())
                                    .secretToken(getAccount().getSecretToken())
                                    .build();
                            final LiveData<TwitterContentResponse> asyncResponse = TwitterClient.removeContent(request);
                            asyncResponse.observeForever(new Observer<TwitterContentResponse>() {
                                @Override
                                public void onChanged(@Nullable TwitterContentResponse response) {
                                    if (response != null) {
                                        if (response.getErrorString() == null) {
                                            if (response.getID().equals(getExternalID())) {
                                                instance.setExternalID(null);
                                                instance.saveInDatabase();
                                                listener.onUnpublished(instance);
                                            }
                                        } else
                                            listener.onError(instance, response.getErrorString());
                                        BackendUpdateRequestCounterRequest request = BackendUpdateRequestCounterRequest.builder()
                                                .endpoint(endpoint)
                                                .serviceName(getService().getName())
                                                .build();
                                        BackendClient.updateRequestCounter(request);
                                        setLoading(false);
                                        asyncResponse.removeObserver(this);
                                    }
                                }
                            });
                            notifyGUI();
                        } else
                            listener.onError(instance, ConvertUtils.requestLimitWaitMessage(response.getRemaining()));
                    }
                }
            });
        }
    }

    @Override
    public void synchronize(final OnSynchronizedListener listener) {
        if (isOnline()) {
            String endpoint = TwitterGetContentRequest.getRequestEndpoint();
            final RequestLimit requestLimit = getAccount().getRequestLimit(endpoint);
            if (requestLimit == null || requestLimit.getRemaining() > 0) {
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
                                saveInDatabase();
                                listener.onSynchronized(instance);
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
            } else
                listener.onError(this, ConvertUtils.requestLimitWaitMessage(requestLimit.getSecondsUntilReset()));
        }
    }

    @Override
    public ChildGroupStatistic getStatistic() {
        ChildGroupStatistic groupStatistic = new ChildGroupStatistic(getAccount().getProfilePictureURL(), getAccount().getName());
        groupStatistic.addChildStatistic(new ChildStatistic("Retweets", retweetCount, getService().getPanelBackgroundID()));
        groupStatistic.addChildStatistic(new ChildStatistic("Favorites", favoriteCount, getService().getPanelBackgroundID()));
        return groupStatistic;
    }

    @Override
    public String getURL() {
        if (isOnline())
            return "https://www.twitter.com/i/web/status/" + getExternalID();
        else
            return "";
    }

    @Override
    protected boolean validateTitle(String title) {
        return true;
    }

    @Override
    protected boolean validateContent(String content) {
        return content.length() <= getMaxContentLength();
    }

    @Override
    protected boolean validateAttachment(Attachment attachment) {
        switch (attachment.getAttachmentType().getID()) {
            case Image:
                ImageAttachment image = (ImageAttachment)attachment;
                if (image.isGIF()) {
                    if (getAttachments().size() <= MAX_GIF_NUMBER) {
                        if (image.getSizeBytes() > MAX_GIF_SIZE_BYTES ||
                            image.getHeight() > MAX_GIF_HEIGHT ||
                            image.getWidth() > MAX_GIF_WIDTH ||
                            image.getFrameCount() > MAX_GIF_FRAME_NUMBER ||
                            image.getPixelCount() > MAX_GIF_PIXEL_NUMBER)
                            return false;
                        else
                            return true;
                    } else
                        return false;
                }
                else {
                    if (getAttachments().size() <= MAX_GIF_NUMBER ||
                        (!getAttachments().isEmpty() &&
                         getAttachments().get(0).getAttachmentType().getID() == AttachmentTypeID.Image &&
                         !getAttachments().get(0).getFileExtension().equals(".gif") &&
                         getAttachments().size() <= MAX_IMAGE_NUMBER)) {
                        if (image.getSizeBytes() > MAX_IMAGE_SIZE_BYTES)
                            return false;
                        else
                            return true;
                    } else
                        return false;
                }
            case Video:
                if (getAttachments().size() <= MAX_VIDEO_NUMBER) {
                    VideoAttachment video = (VideoAttachment)attachment;
                    if (video.getSizeBytes() > MAX_VIDEO_SIZE_BYTES ||
                        video.getDurationMilliseconds() > MAX_VIDEO_DURATION_MILLISECONDS ||
                        video.getDurationMilliseconds() < MIN_VIDEO_DURATION_MILLISECONDS)
                        return false;
                    else
                        return true;
                } else
                    return false;
        }
        return false;
    }

    @Override
    protected boolean preValidateAttachment(Attachment attachment) {
        switch (attachment.getAttachmentType().getID()) {
            case Image:
                ImageAttachment image = (ImageAttachment)attachment;
                if (image.isGIF()) {
                    if (getAttachments().isEmpty()) {
                        if (image.getSizeBytes() > MAX_GIF_SIZE_BYTES ||
                                image.getHeight() > MAX_GIF_HEIGHT ||
                                image.getWidth() > MAX_GIF_WIDTH ||
                                image.getFrameCount() > MAX_GIF_FRAME_NUMBER ||
                                image.getPixelCount() > MAX_GIF_PIXEL_NUMBER)
                            return false;
                        else
                            return true;
                    } else
                        return false;
                }
                else {
                    if (getAttachments().isEmpty() ||
                            (!getAttachments().isEmpty() &&
                             getAttachments().get(0).getAttachmentType().getID() == AttachmentTypeID.Image &&
                             !getAttachments().get(0).getFileExtension().equals(".gif") &&
                             getAttachments().size() < MAX_IMAGE_NUMBER)) {
                        if (image.getSizeBytes() > MAX_IMAGE_SIZE_BYTES)
                            return false;
                        else
                            return true;
                    } else
                        return false;
                }
            case Video:
                if (getAttachments().isEmpty()) {
                    VideoAttachment video = (VideoAttachment)attachment;
                    if (video.getSizeBytes() > MAX_VIDEO_SIZE_BYTES ||
                            video.getDurationMilliseconds() > MAX_VIDEO_DURATION_MILLISECONDS ||
                            video.getDurationMilliseconds() < MIN_VIDEO_DURATION_MILLISECONDS)
                        return false;
                    else
                        return true;
                } else
                    return false;
        }
        return false;
    }

    @Override
    protected int getMaxTitleLength() {
        return 0;
    }

    @Override
    protected int getMaxContentLength() {
        return MAX_CONTENT_LENGTH;
    }
}
