package com.antonina.socialsynchro.services.deviantart.database.rows;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.services.deviantart.model.DeviantArtCategory;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "deviantart_category", foreignKeys = {
        @ForeignKey(entity = DeviantArtCategoryRow.class, parentColumns = "id", childColumns = "parent_category_id", onDelete = CASCADE)})
public class DeviantArtCategoryRow implements IDatabaseRow {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "external_id")
    public String externalID;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "has_children")
    public boolean hasChildren;

    @ColumnInfo(name = "parent_category_id", index = true)
    public Long parentCategoryID;

    @Override
    public void createFromEntity(IDatabaseEntity entity) {
        if (entity.getInternalID() != null)
            this.id = entity.getInternalID();
        DeviantArtCategory category = (DeviantArtCategory)entity;
        this.externalID = category.getExternalID();
        this.title = category.getTitle();
        this.hasChildren = category.hasChildren();
        this.parentCategoryID = category.getParentCategory().getInternalID();
    }

    @Override
    public long getID() {
        return id;
    }
}
