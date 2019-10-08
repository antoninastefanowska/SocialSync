package com.antonina.socialsynchro.common.database.rows;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.common.content.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.database.IDatabaseEntity;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "child_post_container", foreignKeys = {
        @ForeignKey(entity = PostRow.class, parentColumns = "id", childColumns = "post_id"),
        @ForeignKey(entity = ParentPostContainerRow.class, parentColumns = "id", childColumns = "parent_id", onDelete = CASCADE),
        @ForeignKey(entity = AccountRow.class, parentColumns = "id", childColumns = "account_id", onDelete = CASCADE)})
public class ChildPostContainerRow implements IDatabaseRow {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "external_id")
    public String externalID;

    @ColumnInfo(name = "locked")
    public boolean locked;

    @ColumnInfo(name = "synchronization_date")
    public Date synchronizationDate;

    @ColumnInfo(name = "account_id", index = true)
    public long accountID;

    @ColumnInfo(name = "parent_id", index = true)
    public long parentID;

    @ColumnInfo(name = "service_id")
    public int serviceID;

    @ColumnInfo(name = "post_id", index = true)
    public Long postID;

    @Override
    public void createFromEntity(IDatabaseEntity entity) {
        if (entity.getInternalID() != null)
            this.id = entity.getInternalID();

        ChildPostContainer childPostContainer = (ChildPostContainer)entity;
        this.externalID = childPostContainer.getExternalID();
        this.locked = childPostContainer.isLocked();
        this.synchronizationDate = childPostContainer.getSynchronizationDate();
        this.accountID = childPostContainer.getAccount().getInternalID();
        this.parentID = childPostContainer.getParent().getInternalID();
        this.serviceID = childPostContainer.getAccount().getService().getID().ordinal();
        this.postID = childPostContainer.getPost().getInternalID();
    }

    @Override
    public long getID() {
        return id;
    }
}
