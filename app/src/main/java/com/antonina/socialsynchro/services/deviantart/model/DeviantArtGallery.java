package com.antonina.socialsynchro.services.deviantart.model;

import com.antonina.socialsynchro.common.model.services.Service;
import com.antonina.socialsynchro.common.model.services.ServiceID;
import com.antonina.socialsynchro.common.model.services.Services;
import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.rest.IResponse;
import com.antonina.socialsynchro.common.rest.IServiceEntity;
import com.antonina.socialsynchro.services.deviantart.database.repositories.DeviantArtGalleryRepository;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtGalleryRow;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtGetGalleriesResponse;

import java.util.Date;

public class DeviantArtGallery implements IDatabaseEntity, IServiceEntity {
    private Long internalID;
    private String externalID;
    private String name;

    private DeviantArtAccount parentAccount;

    public DeviantArtGallery(IDatabaseRow data) {
        createFromDatabaseRow(data);
    }

    public DeviantArtGallery(IResponse response) {
        createFromResponse(response);
    }

    @Override
    public void createFromDatabaseRow(IDatabaseRow data) {
        setInternalID(data.getID());
        DeviantArtGalleryRow galleryRow = (DeviantArtGalleryRow)data;
        setExternalID(galleryRow.externalID);
        setName(galleryRow.name);
    }

    private void setInternalID(Long internalID) {
        this.internalID = internalID;
    }

    @Override
    public Long getInternalID() {
        return internalID;
    }

    @Override
    public void createFromResponse(IResponse response) {
        DeviantArtGetGalleriesResponse.GalleryResponse galleryResponse = (DeviantArtGetGalleriesResponse.GalleryResponse)response;
        setExternalID(galleryResponse.getID());
        setName(galleryResponse.getName());
    }

    @Override
    public void setExternalID(String externalServiceIdentifier) {
        this.externalID = externalServiceIdentifier;
    }

    @Override
    public String getExternalID() {
        return externalID;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    @Override
    public Service getService() {
        return Services.getService(ServiceID.DeviantArt);
    }

    public DeviantArtAccount getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(DeviantArtAccount parentAccount) {
        this.parentAccount = parentAccount;
    }

    @Override
    public void synchronize(OnSynchronizedListener listener) { }

    @Override
    public Date getSynchronizationDate() {
        return parentAccount.getSynchronizationDate();
    }

    @Override
    public void saveInDatabase() {
        if (getInternalID() != null)
            updateInDatabase();
        else {
            DeviantArtGalleryRepository repository = DeviantArtGalleryRepository.getInstance();
            boolean updated = repository.galleryExists(getExternalID());
            if (!updated)
                setInternalID(repository.insert(this));
            else {
                setInternalID(repository.getIDByExternalID(getExternalID()));
                updateInDatabase();
            }
        }
    }

    @Override
    public void updateInDatabase() {
        DeviantArtGalleryRepository repository = DeviantArtGalleryRepository.getInstance();
        repository.update(this);
    }

    @Override
    public void deleteFromDatabase() {
        DeviantArtGalleryRepository repository = DeviantArtGalleryRepository.getInstance();
        repository.delete(this);
    }
}
