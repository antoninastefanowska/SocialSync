package com.antonina.socialsynchro.base;

import android.databinding.Bindable;

import com.antonina.socialsynchro.BR;
import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.repositories.AccountRepository;
import com.antonina.socialsynchro.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.database.tables.AccountTable;
import com.antonina.socialsynchro.gui.GUIItem;
import com.antonina.socialsynchro.services.IService;
import com.antonina.socialsynchro.services.IServiceEntity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public abstract class Account extends GUIItem implements IDatabaseEntity, IServiceEntity, Serializable {
    private Long internalID;
    private String externalID;
    private String name;
    private String profilePictureUrl; // TODO: Zrobić placeholder. Zdecydować: Przechowywać zdjęcia profilowe? Czy pobierać je bezpośrednio z serwera i usuwać po wyjściu z aplikacji (nie będą dostępne offline)?
    private IService service;
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
        setProfilePictureUrl(accountData.profilePictureUrl);
        setLoading(false);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.account);
    }

    @Override
    public String getExternalID() {
        return externalID;
    }

    @Override
    public void setExternalID(String serviceExternalIdentifier) {
        this.externalID = serviceExternalIdentifier;
        notifyPropertyChanged(BR.account);
    }

    @Bindable
    public String getProfilePictureUrl() { return profilePictureUrl; }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
        notifyPropertyChanged(BR.account);
    }

    @Bindable
    public IService getService() { return service; }

    public void setService(IService service) { this.service = service; }

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
        internalID = repository.insert(this);
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
}
