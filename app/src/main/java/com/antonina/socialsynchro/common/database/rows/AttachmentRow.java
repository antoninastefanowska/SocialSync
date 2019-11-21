package com.antonina.socialsynchro.common.database.rows;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import com.antonina.socialsynchro.common.content.attachments.Attachment;
import com.antonina.socialsynchro.common.database.IDatabaseEntity;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "attachment", foreignKeys = {
        @ForeignKey(entity = PostRow.class, parentColumns = "id", childColumns = "post_id", onDelete = CASCADE)})
public class AttachmentRow implements IDatabaseRow {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "external_id")
    public String externalID;

    @ColumnInfo(name = "filepath")
    public String filepath;

    @ColumnInfo(name = "attachment_type_id")
    public int attachmentTypeID;

    @ColumnInfo(name = "post_id", index = true)
    public long postID;

    @Override
    public void createFromEntity(IDatabaseEntity entity) {
        if (entity.getInternalID() != null)
            this.id = entity.getInternalID();

        Attachment attachment = (Attachment)entity;
        this.filepath = attachment.getFile().getAbsolutePath();
        this.externalID = attachment.getExternalID();
        this.attachmentTypeID = attachment.getAttachmentType().getID().ordinal();
        if (attachment.getParentPost().getInternalID() == null) {
            Log.d("blad", "tu jestem");
        }
        this.postID = attachment.getParentPost().getInternalID();
    }

    @Override
    public long getID() {
        return id;
    }
}
