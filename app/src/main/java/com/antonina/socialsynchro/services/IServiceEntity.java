package com.antonina.socialsynchro.services;

import com.antonina.socialsynchro.gui.listeners.OnSynchronizedListener;

import java.util.Date;

public interface IServiceEntity {
    void createFromResponse(IResponse response);
    void setExternalID(String externalServiceIdentifier);
    String getExternalID();
    Service getService();
    void synchronize(OnSynchronizedListener listener);
    Date getSynchronizationDate();
}
