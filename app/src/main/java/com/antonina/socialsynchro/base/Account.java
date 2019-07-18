package com.antonina.socialsynchro.base;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.SocialSynchro;
import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.tables.ITable;
import com.antonina.socialsynchro.database.tables.AccountTable;
import com.antonina.socialsynchro.database.viewmodels.ServiceViewModel;

public abstract class Account implements IDatabaseEntity {
    private long id;
    private String name;
    private String serviceExternalIdentifier;
    private String profilePictureUrl; // TODO: Zrobić placeholder. Zdecydować: Przechowywać zdjęcia profilowe? Czy pobierać je bezpośrednio z serwera i usuwać po wyjściu z aplikacji (nie będą dostępne offline)?
    private String accessToken;
    private String secretToken;
    private Service service;

    public Account(ITable table) { createFromData(table); }

    @Override
    public void createFromData(ITable data) {
        AccountTable accountData = (AccountTable)data;
        this.id = accountData.id;
        this.name = accountData.name;
        this.serviceExternalIdentifier = accountData.serviceExternalIdentifier;
        this.profilePictureUrl = accountData.profilePictureUrl;
        this.accessToken = accountData.accessToken;
        this.secretToken = accountData.secretToken;

        final Account instance = this;

        final LiveData<Service> serviceLiveData = ServiceViewModel.getInstance(SocialSynchro.getInstance()).getServiceByID(accountData.serviceID);
        serviceLiveData.observeForever(new Observer<Service>() {
            @Override
            public void onChanged(@Nullable Service service) {
                instance.setService(service);
                serviceLiveData.removeObserver(this);
            }
        });
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceExternalIdentifier() {
        return serviceExternalIdentifier;
    }

    public void setServiceExternalIdentifier(String serviceExternalIdentifier) {
        this.serviceExternalIdentifier = serviceExternalIdentifier;
    }

    public String getAccessToken() { return accessToken; }

    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public String getSecretToken() { return secretToken; }

    public void setSecretToken(String secretToken) { this.secretToken = secretToken; }

    public String getProfilePictureUrl() { return profilePictureUrl; }

    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }

    public Service getService() { return service; }

    public void setService(Service service) { this.service = service; }

    @Override
    public long getID() { return id; }
}
