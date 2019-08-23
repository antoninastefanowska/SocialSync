package com.antonina.socialsynchro.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.content.attachments.Attachment;
import com.antonina.socialsynchro.database.IDatabaseEntity;

@Entity(tableName = "attachment", foreignKeys = {
        @ForeignKey(entity = PostTable.class, parentColumns = "id", childColumns = "post_id")})
public class AttachmentTable implements IDatabaseTable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "external_id")
    public String externalID;

    @ColumnInfo(name = "filename")
    public String filename;

    @ColumnInfo(name = "size_kb")
    public int sizeKb;

    @ColumnInfo(name = "attachment_type_id")
    public int attachmentTypeID;

    @ColumnInfo(name = "post_id", index = true)
    public long postID;

    @Override
    public void createFromExistingEntity(IDatabaseEntity entity) {
        this.id = entity.getInternalID();
        createFromNewEntity(entity);
    }

    @Override
    public void createFromNewEntity(IDatabaseEntity entity) {
        Attachment attachment = (Attachment)entity;
        this.filename = attachment.getFilename();
        this.sizeKb = attachment.getSizeKb();
        this.externalID = attachment.getExternalID();
        this.attachmentTypeID = attachment.getAttachmentType().getID().ordinal();
        this.postID = attachment.getParentPost().getInternalID();
    }

    @Override
    public long getID() {
        return id;
    }
}
