package com.antonina.socialsynchro.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.content.ParentPostContainer;
import com.antonina.socialsynchro.database.IDatabaseEntity;

import java.util.Date;

@Entity(tableName = "parent_post_container", foreignKeys = {
        @ForeignKey(entity = PostTable.class, parentColumns = "id", childColumns = "post_id")})
public class ParentPostContainerTable implements IDatabaseTable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "post_id", index = true)
    public long postID;

    @ColumnInfo(name = "creation_date")
    public Date creationDate;

    @Override
    public void createFromExistingEntity(IDatabaseEntity entity) {
        this.id = entity.getInternalID();
        createFromNewEntity(entity);
    }

    @Override
    public void createFromNewEntity(IDatabaseEntity entity) {
        ParentPostContainer parentPostContainer = (ParentPostContainer)entity;
        this.creationDate = parentPostContainer.getCreationDate();
        this.postID = parentPostContainer.getPost().getInternalID();
    }

    @Override
    public long getID() {
        return id;
    }
}
