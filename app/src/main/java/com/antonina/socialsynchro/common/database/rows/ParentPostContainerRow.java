package com.antonina.socialsynchro.common.database.rows;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.common.model.posts.ParentPostContainer;
import com.antonina.socialsynchro.common.database.IDatabaseEntity;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "parent_post_container", foreignKeys = {
        @ForeignKey(entity = PostRow.class, parentColumns = "id", childColumns = "post_id", onDelete = CASCADE)})
public class ParentPostContainerRow implements IDatabaseRow {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "post_id", index = true)
    public long postID;

    @Override
    public void createFromEntity(IDatabaseEntity entity) {
        if (entity.getInternalID() != null)
            this.id = entity.getInternalID();

        ParentPostContainer parentPostContainer = (ParentPostContainer)entity;
        this.postID = parentPostContainer.getPost().getInternalID();
    }

    @Override
    public long getID() {
        return id;
    }
}
