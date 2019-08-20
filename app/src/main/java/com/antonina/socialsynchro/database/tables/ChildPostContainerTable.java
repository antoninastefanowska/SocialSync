package com.antonina.socialsynchro.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.content.ChildPostContainer;
import com.antonina.socialsynchro.database.IDatabaseEntity;

import java.util.Date;

@Entity(tableName = "child_post_container", foreignKeys = {
        @ForeignKey(entity = PostTable.class, parentColumns = "id", childColumns = "post_id"),
        @ForeignKey(entity = ParentPostContainerTable.class, parentColumns = "id", childColumns = "parent_id"),
        @ForeignKey(entity = ServiceTable.class, parentColumns = "id", childColumns = "service_id"),
        @ForeignKey(entity = AccountTable.class, parentColumns = "id", childColumns = "account_id")})
public class ChildPostContainerTable implements ITable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "service_external_identifier")
    public String serviceExternalIdentifier;

    @ColumnInfo(name = "locked")
    public boolean locked;

    // TODO: Będzie potrzebny konwerter
    @ColumnInfo(name = "synchronization_date")
    public Date synchronizationDate;

    @ColumnInfo(name = "account_id", index = true)
    public long accountID;

    @ColumnInfo(name = "parent_id", index = true)
    public long parentID;

    @ColumnInfo(name = "service_id", index = true)
    public long serviceID;

    @ColumnInfo(name = "post_id", index = true)
    public Long postID;

    @Override
    public void createFromExistingEntity(IDatabaseEntity entity) {
        this.id = entity.getID();
        createFromNewEntity(entity);
    }

    @Override
    public void createFromNewEntity(IDatabaseEntity entity) {
        ChildPostContainer childPostContainer = (ChildPostContainer)entity;
        this.serviceExternalIdentifier = childPostContainer.getServiceExternalIdentifier();
        this.locked = childPostContainer.isLocked();
        this.synchronizationDate = childPostContainer.getSynchronizationDate();
        this.accountID = childPostContainer.getAccount().getID();
        this.parentID = childPostContainer.getParent().getID();
        this.serviceID = childPostContainer.getAccount().getService().getID();
        this.postID = childPostContainer.getPost().getID();
    }
}
