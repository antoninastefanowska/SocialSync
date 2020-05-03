package com.antonina.socialsynchro.services.deviantart.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.common.model.services.Service;
import com.antonina.socialsynchro.common.model.services.ServiceID;
import com.antonina.socialsynchro.common.model.services.Services;
import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.gui.GUIItem;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.rest.IResponse;
import com.antonina.socialsynchro.common.rest.IServiceEntity;
import com.antonina.socialsynchro.services.deviantart.database.repositories.DeviantArtCategoryRepository;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtCategoryRow;
import com.antonina.socialsynchro.services.deviantart.rest.responses.DeviantArtGetCategoryTreeResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeviantArtCategory extends GUIItem implements IDatabaseEntity, IServiceEntity {
    private Long internalID;
    private String externalID;
    private String title;
    private boolean hasChildren;

    private List<DeviantArtCategory> childCategories;
    private DeviantArtCategory parentCategory;

    public DeviantArtCategory(IResponse response) {
        createFromResponse(response);
    }

    public DeviantArtCategory(String catpath) {
        setExternalID(catpath);
        childCategories = new ArrayList<>();
    }

    public DeviantArtCategory(String catpath, DeviantArtCategory parentCategory) {
        setExternalID(catpath);
        setParentCategory(parentCategory);
    }

    public DeviantArtCategory(IDatabaseRow data) {
        createFromDatabaseRow(data);
    }

    @Override
    public void createFromDatabaseRow(IDatabaseRow data) {
        setInternalID(data.getID());
        DeviantArtCategoryRow categoryRow = (DeviantArtCategoryRow)data;
        setExternalID(categoryRow.externalID);
        setTitle(categoryRow.title);
        setHasChildren(categoryRow.hasChildren);

        if (hasChildren()) {
            DeviantArtCategoryRepository repository = DeviantArtCategoryRepository.getInstance();
            final LiveData<List<DeviantArtCategory>> liveDataChildren = repository.getDataByParentCategory(this);
            liveDataChildren.observeForever(new Observer<List<DeviantArtCategory>>() {
                @Override
                public void onChanged(@Nullable List<DeviantArtCategory> deviantArtCategories) {
                    if (deviantArtCategories != null) {
                        for (DeviantArtCategory category : deviantArtCategories)
                            addCategoryChild(category);
                        notifyGUI();
                    }
                }
            });
        }
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
        DeviantArtGetCategoryTreeResponse.CategoryResponse categoryResponse = (DeviantArtGetCategoryTreeResponse.CategoryResponse)response;
        setExternalID(categoryResponse.getCatpath());
        setTitle(categoryResponse.getTitle());
        setHasChildren(categoryResponse.getHasSubcategory());
    }

    @Override
    public void setExternalID(String externalServiceIdentifier) {
        this.externalID = externalServiceIdentifier;
    }

    @Override
    public String getExternalID() {
        return externalID;
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public boolean hasChildren() {
        return hasChildren;
    }

    private void setParentCategory(DeviantArtCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    public DeviantArtCategory getParentCategory() {
        return parentCategory;
    }

    private void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public void addCategoryChild(DeviantArtCategory categoryChild) {
        categoryChild.setParentCategory(this);
        childCategories.add(categoryChild);
    }

    @Override
    public Service getService() {
        return Services.getService(ServiceID.DeviantArt);
    }

    @Override
    public void synchronize(final OnSynchronizedListener listener) { }

    @Override
    public Date getSynchronizationDate() {
        return null;
    }

    @Override
    public void saveInDatabase() {
        if (getInternalID() != null)
            updateInDatabase();
        else {
            DeviantArtCategoryRepository repository = DeviantArtCategoryRepository.getInstance();
            boolean updated = repository.categoryExists(getExternalID());
            if (!updated)
                setInternalID(repository.insert(this));
            else {
                setInternalID(repository.getIDByExternalID(getExternalID()));
                updateInDatabase();
            }
            for (DeviantArtCategory childCategory : childCategories)
                childCategory.saveInDatabase();
        }
    }

    @Override
    public void updateInDatabase() {
        DeviantArtCategoryRepository repository = DeviantArtCategoryRepository.getInstance();
        repository.update(this);
        for (DeviantArtCategory childCategory : childCategories)
            childCategory.updateInDatabase();
    }

    @Override
    public void deleteFromDatabase() {
        for (DeviantArtCategory childCategory : childCategories)
            childCategory.deleteFromDatabase();
        DeviantArtCategoryRepository repository = DeviantArtCategoryRepository.getInstance();
        repository.delete(this);
    }
}
