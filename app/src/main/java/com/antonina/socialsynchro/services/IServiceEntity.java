package com.antonina.socialsynchro.services;

public interface IServiceEntity {
    void createFromResponse(IResponse response);
    void setExternalID(String externalServiceIdentifier);
    String getExternalID();
}