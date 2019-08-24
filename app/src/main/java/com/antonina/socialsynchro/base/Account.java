package com.antonina.socialsynchro.base;

import android.databinding.Bindable;

import com.antonina.socialsynchro.BR;
import com.antonina.socialsynchro.SocialSynchro;
import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.repositories.AccountRepository;
import com.antonina.socialsynchro.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.database.tables.AccountTable;
import com.antonina.socialsynchro.gui.SelectableItem;
import com.antonina.socialsynchro.services.IService;
import com.antonina.socialsynchro.services.IServiceEntity;

public abstract class Account extends SelectableItem implements IDatabaseEntity, IServiceEntity {
    private long internalID;
    private String externalID;
    private String name;
    private String profilePictureUrl; // TODO: Zrobić placeholder. Zdecydować: Przechowywać zdjęcia profilowe? Czy pobierać je bezpośrednio z serwera i usuwać po wyjściu z aplikacji (nie będą dostępne offline)?
    private IService service;
    private boolean loading;

    public Account(IDatabaseTable table) { createFromData(table); }

    public Account() { }

    @Override
    public void createFromData(IDatabaseTable data) {
        AccountTable accountData = (AccountTable)data;
        this.internalID = accountData.id;
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

    @Override
    public long getInternalID() { return internalID; }

    @Override
    public void saveInDatabase() {
        AccountRepository repository = AccountRepository.getInstance(SocialSynchro.getInstance());
        internalID = repository.insert(this);
    }

    @Override
    public void updateInDatabase() {
        AccountRepository repository = AccountRepository.getInstance(SocialSynchro.getInstance());
        repository.update(this);
    }

    @Override
    public void deleteFromDatabase() {
        AccountRepository repository = AccountRepository.getInstance(SocialSynchro.getInstance());
        repository.delete(this);
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
