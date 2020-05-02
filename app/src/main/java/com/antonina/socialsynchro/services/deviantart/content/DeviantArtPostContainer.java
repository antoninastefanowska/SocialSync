package com.antonina.socialsynchro.services.deviantart.content;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

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
import com.antonina.socialsynchro.services.deviantart.database.repositories.DeviantArtPostInfoRepository;
import com.antonina.socialsynchro.services.deviantart.database.repositories.DeviantArtPostOptionsRepository;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtPostInfoRow;
import com.antonina.socialsynchro.services.deviantart.rest.DeviantArtClient;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtGetDeviationRequest;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtStashDeleteRequest;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtStashPublishRequest;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtStashSubmitRequest;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtDeviationResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtResultResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtStashPublishResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtStashSubmitResponse;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DeviantArtPostContainer extends ChildPostContainer {
    private static final int MAX_CONTENT_LENGTH = 65535;
    private static final int MAX_TITLE_LENGTH = 50;
    private static final long MAX_IMAGE_SIZE_BYTES = 31457280;
    private static final int MAX_IMAGE_NUMBER = 1;

    private String stashID;
    private String url;

    private int faveCount;
    private int commentCount;

    public DeviantArtPostContainer(DeviantArtAccount account) {
        super(account);
        setOptions(new DeviantArtPostOptions());
        faveCount = 0;
        commentCount = 0;
    }

    public DeviantArtPostContainer(IDatabaseRow data) {
        super(data);
    }

    @Override
    public DeviantArtAccount getAccount() {
        return (DeviantArtAccount)super.getAccount();
    }

    public String getStashID() {
        return stashID;
    }

    private void setStashID(String stashID) {
        this.stashID = stashID;
    }

    public int getFaveCount() {
        return faveCount;
    }

    private void setFaveCount(int faveCount) {
        this.faveCount = faveCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    private void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public void saveInDatabase() {
        if (getInternalID() != null)
            updateInDatabase();
        else {
            super.saveInDatabase();
            DeviantArtPostInfoRepository repository = DeviantArtPostInfoRepository.getInstance();
            repository.insert(this);
        }
    }

    @Override
    public void updateInDatabase() {
        DeviantArtPostInfoRepository repository = DeviantArtPostInfoRepository.getInstance();
        repository.update(this);
        super.updateInDatabase();
    }

    @Override
    public void deleteFromDatabase() {
        DeviantArtPostInfoRepository repository = DeviantArtPostInfoRepository.getInstance();
        repository.delete(this);
        super.deleteFromDatabase();
    }

    @Override
    public void publish(final OnPublishedListener publishListener, final OnAttachmentUploadedListener attachmentListener) {
        if (!isOnline()) {
            setLoading(true);
            final DeviantArtPostContainer instance = this;
            getAccount().refreshToken(new OnSynchronizedListener() {
                @Override
                public void onSynchronized(IServiceEntity entity) {
                    if (!getAttachments().isEmpty())
                        uploadAttachment(getAttachments().get(0), publishListener, attachmentListener);
                     else
                        publishJustContent(publishListener);
                }

                @Override
                public void onError(IServiceEntity entity, String error) {
                    publishListener.onError(instance, error);
                    setLoading(false);
                }
            });
        }
    }

    private void publishJustContent(final OnPublishedListener publishListener) {
        DeviantArtStashSubmitRequest request = DeviantArtStashSubmitRequest.builder()
                .accessToken(getAccount().getAccessToken())
                .title(getTitle())
                .file(contentToPart())
                .build();
        final DeviantArtPostContainer instance = this;
        final LiveData<DeviantArtStashSubmitResponse> asyncResponse = DeviantArtClient.stashSubmit(request);
        asyncResponse.observeForever(new Observer<DeviantArtStashSubmitResponse>() {
            @Override
            public void onChanged(@Nullable DeviantArtStashSubmitResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        setStashID(response.getStashID());
                        publishFromStash(publishListener);
                    } else {
                        publishListener.onError(instance, response.getErrorString());
                    }
                    setLoading(false);
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    private MultipartBody.Part contentToPart() {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), getContent());
        return MultipartBody.Part.createFormData("source", "text", requestBody);
    }

    private void uploadAttachment(final Attachment attachment, final OnPublishedListener publishListener, final OnAttachmentUploadedListener attachmentListener) {
        attachment.setUploadProgress(0);
        attachment.setLoading(true);
        DeviantArtStashSubmitRequest request = DeviantArtStashSubmitRequest.builder()
                .accessToken(getAccount().getAccessToken())
                .title(getTitle())
                .description(getContent())
                .file(attachment.getPart())
                .build();
        final DeviantArtPostContainer instance = this;
        final LiveData<DeviantArtStashSubmitResponse> asyncResponse = DeviantArtClient.stashSubmit(request);
        asyncResponse.observeForever(new Observer<DeviantArtStashSubmitResponse>() {
            @Override
            public void onChanged(@Nullable DeviantArtStashSubmitResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        setStashID(response.getStashID());
                        attachment.setUploadProgress(100);
                        attachment.notifyGUI();
                        attachmentListener.onFinished(attachment);
                        publishFromStash(publishListener);
                    } else {
                        setLoading(false);
                        publishListener.onError(instance, response.getErrorString());
                    }
                    attachment.setLoading(false);
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    private void publishFromStash(final OnPublishedListener publishListener) {
        DeviantArtStashPublishRequest request = DeviantArtStashPublishRequest.builder()
                .accessToken(getAccount().getAccessToken())
                .stashID(getStashID())
                .build();
        final DeviantArtPostContainer instance = this;
        final LiveData<DeviantArtStashPublishResponse> asyncResponse = DeviantArtClient.stashPublish(request);
        asyncResponse.observeForever(new Observer<DeviantArtStashPublishResponse>() {
            @Override
            public void onChanged(@Nullable DeviantArtStashPublishResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        instance.setExternalID(response.getDeviationID());
                        instance.setURL(response.getURL());
                        instance.saveInDatabase();
                        publishListener.onPublished(instance);
                    } else {
                        publishListener.onError(instance, response.getErrorString());
                    }
                    setLoading(false);
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    @Override
    public void createFromDatabaseRow(IDatabaseRow data) {
        super.createFromDatabaseRow(data);

        DeviantArtPostInfoRepository repository = DeviantArtPostInfoRepository.getInstance();
        final DeviantArtPostContainer instance = this;
        final LiveData<DeviantArtPostInfoRow> dataTable = repository.getDataRowByID(data.getID());
        dataTable.observeForever(new Observer<DeviantArtPostInfoRow>() {
            @Override
            public void onChanged(@Nullable DeviantArtPostInfoRow data) {
                if (data != null) {
                    instance.setStashID(data.stashID);
                    instance.setURL(data.url);
                    instance.setFaveCount(data.faveCount);
                    instance.setCommentCount(data.commentCount);
                    notifyGUI();
                    dataTable.removeObserver(this);
                }
            }
        });
        DeviantArtPostOptionsRepository optionsRepository = DeviantArtPostOptionsRepository.getInstance();
        final LiveData<DeviantArtPostOptions> liveDataOptions = optionsRepository.getDataByID(data.getID());
        liveDataOptions.observeForever(new Observer<DeviantArtPostOptions>() {
            @Override
            public void onChanged(@Nullable DeviantArtPostOptions options) {
                setOptions(options);
                notifyGUI();
                liveDataOptions.removeObserver(this);
            }
        });
    }

    @Override
    public void unpublish(final OnUnpublishedListener listener) {
        if (isOnline()) {
            setLoading(true);
            final DeviantArtPostContainer instance = this;
            getAccount().refreshToken(new OnSynchronizedListener() {
                @Override
                public void onSynchronized(IServiceEntity entity) {
                    DeviantArtStashDeleteRequest request = DeviantArtStashDeleteRequest.builder()
                            .stashID(stashID)
                            .accessToken(getAccount().getAccessToken())
                            .build();
                    final LiveData<DeviantArtResultResponse> asyncResponse = DeviantArtClient.stashDelete(request);
                    asyncResponse.observeForever(new Observer<DeviantArtResultResponse>() {
                        @Override
                        public void onChanged(@Nullable DeviantArtResultResponse response) {
                            if (response != null) {
                                if (response.getErrorString() == null) {
                                    instance.setExternalID(null);
                                    instance.saveInDatabase();
                                    listener.onUnpublished(instance);
                                } else
                                    listener.onError(instance, response.getErrorString());
                                setLoading(false);
                                asyncResponse.removeObserver(this);
                            }
                        }
                    });
                }

                @Override
                public void onError(IServiceEntity entity, String error) {
                    setLoading(false);
                    listener.onError(instance, error);
                }
            });
        }
    }

    @Override
    public void createFromResponse(IResponse response) {
        DeviantArtDeviationResponse deviantArtResponse = (DeviantArtDeviationResponse)response;
        setTitle(deviantArtResponse.getTitle());
        setURL(deviantArtResponse.getURL());
        setFaveCount(deviantArtResponse.getFavouriteCount());
        setCommentCount(deviantArtResponse.getCommentCount());
    }

    @Override
    public void synchronize(final OnSynchronizedListener listener) {
        if (isOnline()) {
            setLoading(true);
            final DeviantArtPostContainer instance = this;
            getAccount().refreshToken(new OnSynchronizedListener() {
                @Override
                public void onSynchronized(IServiceEntity entity) {
                    DeviantArtGetDeviationRequest request = DeviantArtGetDeviationRequest.builder()
                            .accessToken(getAccount().getAccessToken())
                            .deviationID(getExternalID())
                            .build();
                    final LiveData<DeviantArtDeviationResponse> asyncResponse = DeviantArtClient.getDeviation(request);
                    asyncResponse.observeForever(new Observer<DeviantArtDeviationResponse>() {
                        @Override
                        public void onChanged(@Nullable DeviantArtDeviationResponse response) {
                            if (response != null) {
                                if (response.getErrorString() == null) {
                                    createFromResponse(response);
                                    listener.onSynchronized(instance);
                                    saveInDatabase();
                                } else {
                                    listener.onError(instance, response.getErrorString());
                                }
                                setLoading(false);
                                asyncResponse.removeObserver(this);
                            }
                        }
                    });
                }

                @Override
                public void onError(IServiceEntity entity, String error) {
                    setLoading(false);
                    listener.onError(entity, error);
                }
            });
        }
    }

    @Override
    public ChildGroupStatistic getStatistic() {
        ChildGroupStatistic groupStatistic = new ChildGroupStatistic(getAccount().getProfilePictureURL(), getAccount().getName());
        groupStatistic.addChildStatistic(new ChildStatistic("Favourites", faveCount, getService().getPanelBackgroundID()));
        groupStatistic.addChildStatistic(new ChildStatistic("Comments", commentCount, getService().getPanelBackgroundID()));
        return groupStatistic;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    protected boolean validateTitle(String title) {
        return title.length() <= getMaxTitleLength();
    }

    @Override
    protected boolean validateContent(String content) {
        return content.length() <= getMaxContentLength();
    }

    @Override
    protected boolean validateAttachment(Attachment attachment) {
        switch (attachment.getAttachmentType().getID()) {
            case Image:
                if (getParent().getAttachments().size() <= MAX_IMAGE_NUMBER) {
                    if (attachment.getSizeBytes() > MAX_IMAGE_SIZE_BYTES)
                        return false;
                    else
                        return true;
                } else
                    return false;
            case Video:
                return false;
        }
        return false;
    }

    @Override
    protected boolean preValidateAttachment(Attachment attachment) {
        switch (attachment.getAttachmentType().getID()) {
            case Image:
                if (getAttachments().isEmpty()) {
                    if (attachment.getSizeBytes() > MAX_IMAGE_SIZE_BYTES)
                        return false;
                    else
                        return true;
                } else
                    return false;
            case Video:
                return false;
        }

        return false;
    }

    @Override
    protected int getMaxTitleLength() {
        return MAX_TITLE_LENGTH;
    }

    @Override
    protected int getMaxContentLength() {
        return MAX_CONTENT_LENGTH;
    }

    private void setURL(String url) {
        this.url = url;
    }
}
