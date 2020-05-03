package com.antonina.socialsynchro.services.deviantart.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.databinding.Bindable;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.common.model.accounts.Account;
import com.antonina.socialsynchro.common.model.services.ServiceID;
import com.antonina.socialsynchro.common.model.services.Services;
import com.antonina.socialsynchro.common.model.statistics.AccountStatistic;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.rest.IResponse;
import com.antonina.socialsynchro.common.rest.IServiceEntity;
import com.antonina.socialsynchro.common.utils.ApplicationConfig;
import com.antonina.socialsynchro.common.utils.Counter;
import com.antonina.socialsynchro.common.utils.SecurityUtils;
import com.antonina.socialsynchro.services.deviantart.database.repositories.DeviantArtAccountInfoRepository;
import com.antonina.socialsynchro.services.deviantart.database.repositories.DeviantArtGalleryRepository;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtAccountInfoRow;
import com.antonina.socialsynchro.services.deviantart.rest.DeviantArtClient;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtGetCategoryTreeRequest;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtGetGalleriesRequest;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtGetUserdataRequest;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtRefreshTokenRequest;
import com.antonina.socialsynchro.services.deviantart.rest.requests.DeviantArtWhoAmIRequest;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtAccessTokenResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtGetCategoryTreeResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtGetGalleriesResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtGetUserdataResponse;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtUserResponse;
import com.antonina.socialsynchro.services.deviantart.utils.DeviantArtConfig;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DeviantArtAccount extends Account {
    private String accessToken;
    private String refreshToken;
    private Date tokenDate;
    private long tokenExpiresIn;
    private boolean critiqueEnabled;

    private List<DeviantArtGallery> galleries;

    private int watcherCount;

    public DeviantArtAccount(IDatabaseRow table) {
        createFromDatabaseRow(table);
        setService(Services.getService(ServiceID.DeviantArt));
    }

    public DeviantArtAccount() {
        setService(Services.getService(ServiceID.DeviantArt));
        watcherCount = 0;
        galleries = new ArrayList<>();
    }

    @Override
    public void createFromDatabaseRow(IDatabaseRow data) {
        super.createFromDatabaseRow(data);

        DeviantArtAccountInfoRepository repository = DeviantArtAccountInfoRepository.getInstance();
        final LiveData<DeviantArtAccountInfoRow> dataTable = repository.getDataRowByID(data.getID());
        dataTable.observeForever(new Observer<DeviantArtAccountInfoRow>() {
            @Override
            public void onChanged(@Nullable DeviantArtAccountInfoRow data) {
                if (data != null) {
                    setAccessToken(SecurityUtils.decrypt(data.accessToken));
                    setRefreshToken(SecurityUtils.decrypt(data.refreshToken));
                    setTokenDate(data.tokenDate);
                    setTokenExpiresIn(data.tokenExiresIn);
                    setWatcherCount(data.watcherCount);
                    notifyGUI();
                    dataTable.removeObserver(this);
                }
            }
        });
        DeviantArtGalleryRepository galleryRepository = DeviantArtGalleryRepository.getInstance();
        final LiveData<List<DeviantArtGallery>> liveDataGalleries = galleryRepository.getDataByAccount(this);
        liveDataGalleries.observeForever(new Observer<List<DeviantArtGallery>>() {
            @Override
            public void onChanged(@Nullable List<DeviantArtGallery> galleries) {
                if (galleries != null) {
                    for (DeviantArtGallery gallery : galleries)
                        addGallery(gallery);
                    notifyGUI();
                    liveDataGalleries.removeObserver(this);
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

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Date getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(Date tokenDate) {
        this.tokenDate = tokenDate;
    }

    public long getTokenExpiresIn() {
        return tokenExpiresIn;
    }

    public void setTokenExpiresIn(long tokenExpiresIn) {
        this.tokenExpiresIn = tokenExpiresIn;
    }

    public int getWatcherCount() {
        return watcherCount;
    }

    public void setWatcherCount(int watcherCount) {
        this.watcherCount = watcherCount;
    }

    @Bindable
    public boolean isCritiqueEnabled() {
        return critiqueEnabled;
    }

    private void setCritiqueEnabled(boolean critiqueEnabled) {
        this.critiqueEnabled = critiqueEnabled;
    }

    public List<DeviantArtGallery> getGalleries() {
        return galleries;
    }

    private void addGallery(DeviantArtGallery gallery) {
        gallery.setParentAccount(this);
        galleries.add(gallery);
    }

    @Override
    public void saveInDatabase() {
        if (getInternalID() != null)
            updateInDatabase();
        else {
            super.saveInDatabase();
            DeviantArtAccountInfoRepository repository = DeviantArtAccountInfoRepository.getInstance();
            if (!updated)
                repository.insert(this);
            else
                updateInDatabase();
            for (DeviantArtGallery gallery : galleries)
                gallery.saveInDatabase();
        }
    }

    @Override
    public void updateInDatabase() {
        DeviantArtAccountInfoRepository repository = DeviantArtAccountInfoRepository.getInstance();
        repository.update(this);
        super.updateInDatabase();
        for (DeviantArtGallery gallery : galleries)
            gallery.updateInDatabase();
    }

    @Override
    public void deleteFromDatabase() {
        for (DeviantArtGallery gallery : galleries)
            gallery.deleteFromDatabase();
        DeviantArtAccountInfoRepository repository = DeviantArtAccountInfoRepository.getInstance();
        repository.delete(this);
        super.deleteFromDatabase();
    }

    @Override
    public AccountStatistic getStatistic() {
        return new AccountStatistic("Watchers", watcherCount, getProfilePictureURL(), getName(), getService().getPanelBackgroundID());
    }

    @Override
    public String getURL() {
        return "https://www.deviantart.com/" + getName();
    }

    @Override
    public void createFromResponse(IResponse response) {
        DeviantArtUserResponse deviantArtResponse = (DeviantArtUserResponse)response;
        setExternalID(deviantArtResponse.getUserID());
        setName(deviantArtResponse.getUsername());
        setProfilePictureURL(deviantArtResponse.getUserIcon());
        setWatcherCount(deviantArtResponse.getWatcherCount());
    }

    public void refreshToken(final OnSynchronizedListener listener) {
        ApplicationConfig config = ApplicationConfig.getInstance();
        Date currentDate = Calendar.getInstance().getTime();
        long elapsed = (currentDate.getTime() - tokenDate.getTime()) / 1000;
        if (elapsed <= tokenExpiresIn) {
            listener.onSynchronized(this);
        } else {
            DeviantArtRefreshTokenRequest request = DeviantArtRefreshTokenRequest.builder()
                    .clientID(config.getKey("deviantart_client_id"))
                    .clientSecret(config.getKey("deviantart_client_secret"))
                    .refreshToken(getRefreshToken())
                    .build();
            final DeviantArtAccount instance = this;
            final LiveData<DeviantArtAccessTokenResponse> asyncResponse = DeviantArtClient.refreshToken(request);
            asyncResponse.observeForever(new Observer<DeviantArtAccessTokenResponse>() {
                @Override
                public void onChanged(@Nullable DeviantArtAccessTokenResponse response) {
                    if (response != null) {
                        if (response.getErrorString() == null) {
                            setAccessToken(response.getAccessToken());
                            setRefreshToken(response.getRefreshToken());
                            setTokenDate(Calendar.getInstance().getTime());
                            setTokenExpiresIn(response.getExpiresIn());
                            listener.onSynchronized(instance);
                        } else {
                            listener.onError(instance, response.getErrorString());
                        }
                    }
                }
            });
        }
    }

    @Override
    public void synchronize(final OnSynchronizedListener listener) {
        setLoading(true);
        final DeviantArtAccount instance = this;
        refreshToken(new OnSynchronizedListener() {
            @Override
            public void onSynchronized(IServiceEntity entity) {
                DeviantArtWhoAmIRequest request = DeviantArtWhoAmIRequest.builder()
                        .accessToken(getAccessToken())
                        .build();
                final LiveData<DeviantArtUserResponse> asyncResponse = DeviantArtClient.whoAmI(request);
                asyncResponse.observeForever(new Observer<DeviantArtUserResponse>() {
                    @Override
                    public void onChanged(@Nullable DeviantArtUserResponse response) {
                        if (response != null) {
                            if (response.getErrorString() == null) {
                                createFromResponse(response);
                                loadUserdata(listener);
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
            public void onError(IServiceEntity entity, String error) {
                listener.onError(entity, error);
            }
        });
    }

    private void loadUserdata(final OnSynchronizedListener listener) {
        final DeviantArtAccount instance = this;
        DeviantArtGetUserdataRequest request = DeviantArtGetUserdataRequest.builder()
                .accessToken(getAccessToken())
                .build();
        final LiveData<DeviantArtGetUserdataResponse> asyncResponse = DeviantArtClient.getUserdata(request);
        asyncResponse.observeForever(new Observer<DeviantArtGetUserdataResponse>() {
            @Override
            public void onChanged(@Nullable DeviantArtGetUserdataResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        setCritiqueEnabled(response.getFeatures().contains("critique"));
                        loadGalleries(listener, null);
                    } else {
                        setLoading(false);
                        listener.onError(instance, response.getErrorString());
                    }
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    private void loadGalleries(final OnSynchronizedListener listener, Integer offset) {
        final DeviantArtAccount instance = this;
        DeviantArtGetGalleriesRequest request = DeviantArtGetGalleriesRequest.builder()
                .offset(offset)
                .accessToken(getAccessToken())
                .build();
        final LiveData<DeviantArtGetGalleriesResponse> asyncResponse = DeviantArtClient.getGalleries(request);
        asyncResponse.observeForever(new Observer<DeviantArtGetGalleriesResponse>() {
            @Override
            public void onChanged(@Nullable DeviantArtGetGalleriesResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        for (DeviantArtGetGalleriesResponse.GalleryResponse galleryResponse : response.getGalleries()) {
                            DeviantArtGallery gallery = new DeviantArtGallery(galleryResponse);
                            addGallery(gallery);
                        }
                        if (response.getHasMore())
                            loadGalleries(listener, response.getNextOffset());
                        else {
                            final DeviantArtCategory rootCategory = new DeviantArtCategory("/");
                            loadCategories(new OnSynchronizedListener() {
                                @Override
                                public void onSynchronized(IServiceEntity entity) {
                                    DeviantArtConfig config = DeviantArtConfig.getInstance();
                                    rootCategory.saveInDatabase();
                                    config.setRootCategory(rootCategory);
                                    if (getInternalID() != null)
                                        updateInDatabase();
                                    listener.onSynchronized(instance);
                                    setLoading(false);
                                }

                                @Override
                                public void onError(IServiceEntity entity, String error) {
                                    listener.onError(instance, error);
                                }
                            }, rootCategory);
                        }
                    } else {
                        setLoading(false);
                        listener.onError(instance, response.getErrorString());
                    }
                    asyncResponse.removeObserver(this);
                }
            }
        });
    }

    private void loadCategories(final OnSynchronizedListener listener, final DeviantArtCategory parentCategory) {
        if (parentCategory.hasChildren())
            listener.onSynchronized(parentCategory);
        else {
            final Counter loadedChildrenCounter = new Counter();
            DeviantArtGetCategoryTreeRequest request = DeviantArtGetCategoryTreeRequest.builder()
                    .catpath(parentCategory.getExternalID())
                    .accessToken(getAccessToken())
                    .build();
            final LiveData<DeviantArtGetCategoryTreeResponse> asyncResponse = DeviantArtClient.getCategoryTree(request);
            asyncResponse.observeForever(new Observer<DeviantArtGetCategoryTreeResponse>() {
                @Override
                public void onChanged(@Nullable DeviantArtGetCategoryTreeResponse response) {
                    if (response != null) {
                        if (response.getErrorString() == null) {
                            final int childrenCount = response.getCategories().size();
                            for (DeviantArtGetCategoryTreeResponse.CategoryResponse categoryResponse : response.getCategories()) {
                                final DeviantArtCategory category = new DeviantArtCategory(categoryResponse);
                                loadCategories(new OnSynchronizedListener() {
                                    @Override
                                    public void onSynchronized(IServiceEntity entity) {
                                        loadedChildrenCounter.increment();
                                        if (loadedChildrenCounter.getValue() == childrenCount)
                                            listener.onSynchronized(category);
                                    }

                                    @Override
                                    public void onError(IServiceEntity entity, String error) {
                                        listener.onError(entity, error);
                                    }
                                }, category);
                                parentCategory.addCategoryChild(category);
                                asyncResponse.removeObserver(this);
                            }
                        }
                    }
                }
            });
        }
    }
}
