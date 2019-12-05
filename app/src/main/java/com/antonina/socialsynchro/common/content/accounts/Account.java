package com.antonina.socialsynchro.common.content.accounts;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.databinding.Bindable;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.common.content.statistics.AccountStatistic;
import com.antonina.socialsynchro.common.content.services.Service;
import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.repositories.AccountRepository;
import com.antonina.socialsynchro.common.database.repositories.RequestLimitRepository;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.database.rows.AccountRow;
import com.antonina.socialsynchro.common.gui.GUIItem;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.rest.IServiceEntity;
import com.antonina.socialsynchro.common.rest.RequestLimit;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("WeakerAccess")
public abstract class Account extends GUIItem implements IDatabaseEntity, IServiceEntity {
    protected boolean updated = false;

    private Long internalID;
    private String externalID;
    private String name;
    private String profilePictureURL; // TODO: Zrobić placeholder.
    private Service service;
    private Date connectingDate;
    private Date synchronizationDate;

    protected Map<String, RequestLimit> requestLimits;

    public Account(IDatabaseRow table) {
        createFromDatabaseRow(table);
        setLoading(false);
    }

    public Account() {
        requestLimits = new TreeMap<>();
        setLoading(false);
    }

    @Override
    public void createFromDatabaseRow(IDatabaseRow data) {
        AccountRow accountData = (AccountRow)data;
        this.internalID = accountData.getID();
        this.connectingDate = accountData.connectingDate;
        setName(accountData.name);
        setExternalID(accountData.externalID);
        setProfilePictureURL(accountData.profilePictureURL);
        requestLimits = new TreeMap<>();

        RequestLimitRepository requestLimitRepository = RequestLimitRepository.getInstance();
        final LiveData<List<RequestLimit>> requestLimitsLiveData = requestLimitRepository.getIDsByAccount(this);
        requestLimitsLiveData.observeForever(new Observer<List<RequestLimit>>() {
            @Override
            public void onChanged(@Nullable List<RequestLimit> requestLimits) {
                if (requestLimits != null) {
                    for (RequestLimit requestLimit : requestLimits) {
                        if (requestLimit != null)
                            addRequestLimit(requestLimit);
                    }
                }
            }
        });
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getExternalID() {
        return externalID;
    }

    @Override
    public void setExternalID(String serviceExternalIdentifier) {
        this.externalID = serviceExternalIdentifier;
    }

    @Bindable
    public String getProfilePictureURL() { return profilePictureURL; }

    protected void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    @Bindable
    @Override
    public Service getService() {
        return service;
    }

    protected void setService(Service service) {
        this.service = service;
    }

    @Bindable
    public Date getConnectingDate() {
        return connectingDate;
    }

    @Override
    public Long getInternalID() { return internalID; }

    @Override
    public void saveInDatabase() {
        if (internalID != null)
            updateInDatabase();
        else {
            connectingDate = Calendar.getInstance().getTime();
            AccountRepository repository = AccountRepository.getInstance();
            updated = repository.accountExists(externalID, service);
            if (!updated)
                internalID = repository.insert(this);
            else {
                internalID = repository.getIDByExternalIDAndService(externalID, service);
                updateInDatabase();
            }
        }
    }

    @Override
    public void updateInDatabase() {
        AccountRepository repository = AccountRepository.getInstance();
        repository.update(this);
    }

    @Override
    public void deleteFromDatabase() {
        if (internalID == null)
            return;
        AccountRepository repository = AccountRepository.getInstance();
        repository.delete(this);
        internalID = null;
    }

    public boolean hasBeenUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    @Override
    public Date getSynchronizationDate() {
        return synchronizationDate;
    }

    protected void setSynchronizationDate(Date synchronizationDate) {
        this.synchronizationDate = synchronizationDate;
    }

    public abstract void synchronizeRequestLimits(OnSynchronizedListener listener);

    protected void addRequestLimit(RequestLimit requestLimit) {
        requestLimit.setAccount(this);
        requestLimits.put(requestLimit.getEndpoint(), requestLimit);
    }

    public RequestLimit getRequestLimit(String endpoint) {
        return requestLimits.get(endpoint);
    }

    public abstract AccountStatistic getStatistic();

    public abstract String getURL();
}
