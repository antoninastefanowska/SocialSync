package com.antonina.socialsynchro.services.deviantart.database.rows;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.services.deviantart.model.DeviantArtGallery;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "deviantart_gallery", foreignKeys = {
        @ForeignKey(entity = DeviantArtAccountInfoRow.class, parentColumns = "id", childColumns = "account_id", onDelete = CASCADE)})
public class DeviantArtGalleryRow implements IDatabaseRow {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "external_id")
    public String externalID;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "account_id", index = true)
    public long accountID;

    @Override
    public void createFromEntity(IDatabaseEntity entity) {
        if (entity.getInternalID() != null)
            this.id = entity.getInternalID();

        DeviantArtGallery gallery = (DeviantArtGallery)entity;
        this.externalID = gallery.getExternalID();
        this.name = gallery.getName();
        this.accountID = gallery.getParentAccount().getInternalID();
    }

    @Override
    public long getID() {
        return id;
    }
}
