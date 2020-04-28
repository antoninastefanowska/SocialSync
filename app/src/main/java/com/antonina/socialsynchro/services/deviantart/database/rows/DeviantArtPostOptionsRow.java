package com.antonina.socialsynchro.services.deviantart.database.rows;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.services.deviantart.content.DeviantArtGallery;
import com.antonina.socialsynchro.services.deviantart.content.DeviantArtPostOptions;

import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "deviantart_post_options", foreignKeys = {
        @ForeignKey(entity = DeviantArtPostInfoRow.class, parentColumns = "id", childColumns = "id", onDelete = CASCADE),
        @ForeignKey(entity = DeviantArtCategoryRow.class, parentColumns = "id", childColumns = "category_id")})
public class DeviantArtPostOptionsRow implements IDatabaseRow {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "is_mature")
    public boolean isMature;

    @ColumnInfo(name = "mature_level")
    public String matureLevel;

    @ColumnInfo(name = "mature_classification")
    public String matureClassification;

    @ColumnInfo(name = "agree_submission")
    public boolean agreeSubmission;

    @ColumnInfo(name = "agree_tos")
    public boolean agreeTOS;

    @ColumnInfo(name = "category_id", index = true)
    public Long categoryID;

    @ColumnInfo(name = "feature")
    public boolean feature;

    @ColumnInfo(name = "allow_comments")
    public boolean allowComments;

    @ColumnInfo(name = "request_critique")
    public boolean requestCritique;

    @ColumnInfo(name = "display_resolution")
    public int displayResolution;

    @ColumnInfo(name = "sharing")
    public String sharing;

    @ColumnInfo(name = "creative_commons")
    public boolean creativeCommons;

    @ColumnInfo(name = "commercial")
    public boolean commercial;

    @ColumnInfo(name = "modify")
    public String modify;

    @ColumnInfo(name = "gallery_ids")
    public String galleryIDs;

    @ColumnInfo(name = "allow_free_download")
    public boolean allowFreeDownload;

    @ColumnInfo(name = "add_watermark")
    public boolean addWatermark;

    @Override
    public void createFromEntity(IDatabaseEntity entity) {
        if (entity.getInternalID() != null)
            this.id = entity.getInternalID();
        DeviantArtPostOptions options = (DeviantArtPostOptions)entity;
        StringBuilder sb = new StringBuilder();
        String separator = "";

        this.isMature = options.isMature();
        this.matureLevel = options.getMatureLevel();
        List<String> matureClassificationList = options.getMatureClassification();
        for (String matureClassification : matureClassificationList) {
            sb.append(separator);
            sb.append(matureClassification);
            separator = ",";
        }
        this.matureClassification = sb.toString();

        this.agreeSubmission = options.isAgreeSubmission();
        this.agreeTOS = options.isAgreeTOS();
        this.categoryID = options.getCategory() != null ? options.getCategory().getInternalID() : null;
        this.feature = options.isFeature();
        this.allowComments = options.isAllowComments();
        this.requestCritique = options.isRequestCritique();
        this.displayResolution = options.getDisplayResolution();
        this.sharing = options.getSharing();
        this.creativeCommons = options.getLicenseOptions().isCreativeCommons();
        this.commercial = options.getLicenseOptions().isCommercial();
        this.modify = options.getLicenseOptions().getModify();

        sb.setLength(0);
        separator = "";
        List<DeviantArtGallery> galleries = options.getGalleries();
        for (DeviantArtGallery gallery : galleries) {
            sb.append(separator);
            sb.append(gallery.getInternalID());
            separator = ",";
        }
        this.galleryIDs = sb.toString();

        this.allowFreeDownload = options.isAllowFreeDownload();
        this.addWatermark = options.isAddWatermark();
    }

    @Override
    public long getID() {
        return id;
    }
}
