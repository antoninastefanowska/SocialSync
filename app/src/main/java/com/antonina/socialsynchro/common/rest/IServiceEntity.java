package com.antonina.socialsynchro.common.rest;

import com.antonina.socialsynchro.common.model.services.Service;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;

import java.util.Date;

public interface IServiceEntity {
    void createFromResponse(IResponse response);
    void setExternalID(String externalServiceIdentifier);
    String getExternalID();
    Service getService();
    void synchronize(OnSynchronizedListener listener);
    Date getSynchronizationDate();
}
