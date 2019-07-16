package com.antonina.socialsynchro.base;

import com.antonina.socialsynchro.database.tables.AccountTable;

public abstract class Account implements IConvertedData {
    private Long id;
    private String name;
    private String serviceExternalIdentifier;
    private String profilePictureUrl;
    private String accessToken;
    private String secretToken;
    private Service service;

    public Account(AccountTable table) {
        convertFromTable((ITable)table);
    }

    @Override
    public void convertFromTable(ITable table) {
        AccountTable accountTable = (AccountTable)table;
        this.id = accountTable.id;
        this.name = accountTable.name;
        this.serviceExternalIdentifier = accountTable.serviceExternalIdentifier;
        this.profilePictureUrl = accountTable.profilePictureUrl;
        this.accessToken = accountTable.accessToken;
        this.secretToken = accountTable.secretToken;
        // TODO: uzyskac service z bazy
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
}
