package com.antonina.socialsynchro.services.deviantart.utils;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.services.deviantart.model.DeviantArtCategory;
import com.antonina.socialsynchro.services.deviantart.database.repositories.DeviantArtCategoryRepository;

public class DeviantArtConfig {
    private static DeviantArtConfig instance;
    private DeviantArtCategory rootCategory;

    private DeviantArtConfig() { }

    public static DeviantArtConfig getInstance() {
        if (instance == null)
            instance = new DeviantArtConfig();
        return instance;
    }

    public DeviantArtCategory getRootCategory() {
        return rootCategory;
    }

    public void setRootCategory(DeviantArtCategory rootCategory) {
        this.rootCategory = rootCategory;
    }

    public void loadData() {
        DeviantArtCategoryRepository repository = DeviantArtCategoryRepository.getInstance();
        LiveData<DeviantArtCategory> liveDataRootCategory = repository.getRootCategory();
        liveDataRootCategory.observeForever(new Observer<DeviantArtCategory>() {
            @Override
            public void onChanged(@Nullable DeviantArtCategory rootCategory) {
                setRootCategory(rootCategory);
            }
        });
    }
}
