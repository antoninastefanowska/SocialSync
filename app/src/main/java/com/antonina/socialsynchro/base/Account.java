package com.antonina.socialsynchro.base;

import android.databinding.Bindable;

import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.repositories.AccountRepository;
import com.antonina.socialsynchro.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.database.tables.AccountTable;
import com.antonina.socialsynchro.gui.GUIItem;
import com.antonina.socialsynchro.services.Service;
import com.antonina.socialsynchro.services.IServiceEntity;

import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("WeakerAccess")
public abstract class Account extends GUIItem implements IDatabaseEntity, IServiceEntity {
    protected boolean updated = false;

    private Long internalID;
    private String externalID;
    private String name;
    private String profilePictureURL; // TODO: Zrobić placeholder.
    private Service service;
    private Date connectingDate;
    private boolean loading;

    public Account(IDatabaseTable table) { createFromData(table); }

    public Account() { }

    @Override
    public void createFromData(IDatabaseTable data) {
        AccountTable accountData = (AccountTable)data;
        this.internalID = accountData.id;
        this.connectingDate = accountData.connectingDate;
        setName(accountData.name);
        setExternalID(accountData.externalID);
        setProfilePictureURL(accountData.profilePictureUrl);
        setLoading(false);
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
    public Service getService() { return service; }

    protected void setService(Service service) { this.service = service; }

    @Bindable
    public Date getConnectingDate() {
        return connectingDate;
    }

    @Override
    public Long getInternalID() { return internalID; }

    @Override
    public void saveInDatabase() {
        if (internalID != null)
            return;
        connectingDate = Calendar.getInstance().getTime();
        AccountRepository repository = AccountRepository.getInstance();
        updated = repository.accountExists(externalID);
        if (!updated)
            internalID = repository.insert(this);
        else {
            internalID = repository.getIDByExternalID(externalID);
            updateInDatabase();
        }
    }

    @Override
    public void updateInDatabase() {
        if (internalID == null)
            return;
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

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public boolean hasBeenUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
}
