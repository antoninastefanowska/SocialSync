package com.antonina.socialsynchro.services.deviantart.content;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.databinding.Bindable;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.common.content.posts.PostOptions;
import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.services.deviantart.database.repositories.DeviantArtCategoryRepository;
import com.antonina.socialsynchro.services.deviantart.database.repositories.DeviantArtGalleryRepository;
import com.antonina.socialsynchro.services.deviantart.database.repositories.DeviantArtPostOptionsRepository;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtPostOptionsRow;
import com.antonina.socialsynchro.services.deviantart.utils.DeviantArtConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviantArtPostOptions extends PostOptions implements IDatabaseEntity {
    public static final String[] MATURE_LEVELS = {
            "strict",
            "moderate"
    };
    public static final String[] MATURE_CLASSIFICATIONS = {
            "nudity",
            "sexual",
            "gore",
            "language",
            "ideology"
    };
    public static final Map<String, Integer> RESOLUTIONS = new HashMap<String, Integer>() {{
            put("original", 0);
            put("400px", 1);
            put("600px", 2);
            put("800px", 3);
            put("900px", 4);
            put("1024px", 5);
            put("1280px", 6);
            put("1600px", 7);
            put("1920px", 8);
    }};
    public static final String[] SHARING_OPTIONS = {
            "allow",
            "hide_share_buttons",
            "hide_and_members_only"
    };

    private boolean isMature;
    private String matureLevel;
    private List<String> matureClassification;

    private boolean agreeSubmission;
    private boolean agreeTOS;
    private DeviantArtCategory category;
    private boolean feature;
    private boolean allowComments;
    private boolean requestCritique; //TODO: tylko dla niektórych użytkowników
    private int displayResolution;
    private String sharing;
    private LicenseOptions licenseOptions;
    private List<DeviantArtGallery> galleries;
    private boolean allowFreeDownload;
    private boolean addWatermark; //TODO: Tylko jeśli display_resolution

    public DeviantArtPostOptions(DeviantArtPostContainer parentPost) {
        this();
        setParentPost(parentPost);
    }

    public DeviantArtPostOptions() {
        isMature = false;
        matureLevel = MATURE_LEVELS[1];
        matureClassification = new ArrayList<>();
        matureClassification.add(MATURE_CLASSIFICATIONS[0]);

        agreeSubmission = false;
        agreeTOS = false;
        category = DeviantArtConfig.getInstance().getRootCategory();
        feature = true;
        allowComments = true;
        requestCritique = false;
        displayResolution = RESOLUTIONS.get("original");
        sharing = SHARING_OPTIONS[0];
        licenseOptions = new LicenseOptions();
        galleries = new ArrayList<>();
        allowFreeDownload = false;
        addWatermark = false;
    }

    public DeviantArtPostOptions(IDatabaseRow data) {
        this();
        if (data != null)
            createFromDatabaseRow(data);
    }

    @Override
    public void createFromDatabaseRow(IDatabaseRow data) {
        setInternalID(data.getID());

        DeviantArtPostOptionsRow dataRow = (DeviantArtPostOptionsRow)data;

        setMature(dataRow.isMature);
        setMatureLevel(dataRow.matureLevel);
        setMatureClassification(Arrays.asList(dataRow.matureClassification.split(",")));
        setAgreeSubmission(dataRow.agreeSubmission);
        setAgreeTOS(dataRow.agreeTOS);

        final LiveData<DeviantArtCategory> categoryLiveData = DeviantArtCategoryRepository.getInstance().getDataByID(dataRow.categoryID);
        categoryLiveData.observeForever(new Observer<DeviantArtCategory>() {
            @Override
            public void onChanged(@Nullable DeviantArtCategory deviantArtCategory) {
                setCategory(deviantArtCategory);
                categoryLiveData.removeObserver(this);
            }
        });
        setFeature(dataRow.feature);
        setAllowComments(dataRow.allowComments);
        setRequestCritique(dataRow.requestCritique);
        setDisplayResolution(dataRow.displayResolution);
        setSharing(dataRow.sharing);

        LicenseOptions licenseOptions = new LicenseOptions();
        licenseOptions.setCreativeCommons(dataRow.creativeCommons);
        licenseOptions.setCommercial(dataRow.commercial);
        licenseOptions.setModify(dataRow.modify);
        setLicenseOptions(licenseOptions);

        final LiveData<List<DeviantArtGallery>> galleriesLiveData = DeviantArtGalleryRepository.getInstance().getDataByIDs(dataRow.galleryIDs);
        galleriesLiveData.observeForever(new Observer<List<DeviantArtGallery>>() {
            @Override
            public void onChanged(@Nullable List<DeviantArtGallery> deviantArtGalleries) {
                setGalleries(deviantArtGalleries);
                galleriesLiveData.removeObserver(this);
            }
        });
        setAllowFreeDownload(dataRow.allowFreeDownload);
        setAddWatermark(dataRow.addWatermark);
    }

    @Override
    public void saveInDatabase() {
        if (getInternalID() != null)
            updateInDatabase();
        else {
            setInternalID(getParentPost().getInternalID());
            DeviantArtPostOptionsRepository repository = DeviantArtPostOptionsRepository.getInstance();
            repository.insert(this);
        }
    }

    @Override
    public void updateInDatabase() {
        DeviantArtPostOptionsRepository repository = DeviantArtPostOptionsRepository.getInstance();
        repository.update(this);
    }

    @Override
    public void deleteFromDatabase() {
        DeviantArtPostOptionsRepository repository = DeviantArtPostOptionsRepository.getInstance();
        repository.delete(this);
    }

    public static class LicenseOptions {
        public static String[] MODIFY_VALUES = {"yes", "no", "share"};

        private boolean creativeCommons;
        private boolean commercial;
        private String modify;

        public LicenseOptions() {
            creativeCommons = false;
            commercial = false;
            modify = MODIFY_VALUES[1];
        }

        public boolean isCreativeCommons() {
            return creativeCommons;
        }

        public void setCreativeCommons(boolean creativeCommons) {
            this.creativeCommons = creativeCommons;
        }

        public boolean isCommercial() {
            return commercial;
        }

        public void setCommercial(boolean commercial) {
            this.commercial = commercial;
        }

        public String getModify() {
            return modify;
        }

        public void setModify(String modify) {
            this.modify = modify;
        }
    }

    public boolean isMature() {
        return isMature;
    }

    @Bindable
    public void setMature(boolean mature) {
        isMature = mature;
    }

    @Bindable
    public String getMatureLevel() {
        return matureLevel;
    }

    @Bindable
    public void setMatureLevel(String matureLevel) {
        this.matureLevel = matureLevel;
    }

    @Bindable
    public List<String> getMatureClassification() {
        return matureClassification;
    }

    @Bindable
    public void setMatureClassification(List<String> matureClassification) {
        this.matureClassification = matureClassification;
    }

    @Bindable
    public boolean isAgreeSubmission() {
        return agreeSubmission;
    }

    @Bindable
    public void setAgreeSubmission(boolean agreeSubmission) {
        this.agreeSubmission = agreeSubmission;
    }

    @Bindable
    public boolean isAgreeTOS() {
        return agreeTOS;
    }

    @Bindable
    public void setAgreeTOS(boolean agreeTOS) {
        this.agreeTOS = agreeTOS;
    }

    @Bindable
    public DeviantArtCategory getCategory() {
        return category;
    }

    @Bindable
    public void setCategory(DeviantArtCategory category) {
        this.category = category;
    }

    @Bindable
    public boolean isFeature() {
        return feature;
    }

    @Bindable
    public void setFeature(boolean feature) {
        this.feature = feature;
    }

    @Bindable
    public boolean isAllowComments() {
        return allowComments;
    }

    @Bindable
    public void setAllowComments(boolean allowComments) {
        this.allowComments = allowComments;
    }

    @Bindable
    public boolean isRequestCritique() {
        return requestCritique;
    }

    @Bindable
    public void setRequestCritique(boolean requestCritique) {
        this.requestCritique = requestCritique;
    }

    @Bindable
    public int getDisplayResolution() {
        return displayResolution;
    }

    @Bindable
    public void setDisplayResolution(int displayResolution) {
        this.displayResolution = displayResolution;
    }

    @Bindable
    public String getSharing() {
        return sharing;
    }

    @Bindable
    public void setSharing(String sharing) {
        this.sharing = sharing;
    }

    @Bindable
    public LicenseOptions getLicenseOptions() {
        return licenseOptions;
    }

    @Bindable
    public void setLicenseOptions(LicenseOptions licenseOptions) {
        this.licenseOptions = licenseOptions;
    }

    @Bindable
    public List<DeviantArtGallery> getGalleries() {
        return galleries;
    }

    @Bindable
    public void setGalleries(List<DeviantArtGallery> galleries) {
        this.galleries = galleries;
    }

    @Bindable
    public boolean isAllowFreeDownload() {
        return allowFreeDownload;
    }

    @Bindable
    public void setAllowFreeDownload(boolean allowFreeDownload) {
        this.allowFreeDownload = allowFreeDownload;
    }

    @Bindable
    public boolean isAddWatermark() {
        return addWatermark;
    }

    @Bindable
    public void setAddWatermark(boolean addWatermark) {
        this.addWatermark = addWatermark;
    }
}
