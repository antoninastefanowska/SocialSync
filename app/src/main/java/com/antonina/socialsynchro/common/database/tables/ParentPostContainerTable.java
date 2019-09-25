package com.antonina.socialsynchro.common.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.common.content.posts.ParentPostContainer;
import com.antonina.socialsynchro.common.database.IDatabaseEntity;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "parent_post_container", foreignKeys = {
        @ForeignKey(entity = PostTable.class, parentColumns = "id", childColumns = "post_id", onDelete = CASCADE)})
public class ParentPostContainerTable implements IDatabaseTable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "post_id", index = true)
    public long postID;

    @Override
    public void createFromExistingEntity(IDatabaseEntity entity) {
        this.id = entity.getInternalID();
        createFromNewEntity(entity);
    }

    @Override
    public void createFromNewEntity(IDatabaseEntity entity) {
        ParentPostContainer parentPostContainer = (ParentPostContainer)entity;
        this.postID = parentPostContainer.getPost().getInternalID();
    }

    @Override
    public long getID() {
        return id;
    }
}
