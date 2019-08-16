package com.antonina.socialsynchro.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.content.attachments.AttachmentType;
import com.antonina.socialsynchro.database.IDatabaseEntity;

@Entity(tableName = "attachment_type")
public class AttachmentTypeTable implements ITable {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "icon_url")
    public String iconUrl;

    public AttachmentTypeTable(long id, String name, String iconUrl) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
    }

    @Override
    public void createFromExistingEntity(IDatabaseEntity entity) {
        this.id = entity.getID();
        createFromNewEntity(entity);
    }

    @Override
    public void createFromNewEntity(IDatabaseEntity entity) {
        AttachmentType attachmentType = (AttachmentType)entity;
        this.name = attachmentType.getName();
        this.iconUrl = attachmentType.getIconUrl();
    }
}
