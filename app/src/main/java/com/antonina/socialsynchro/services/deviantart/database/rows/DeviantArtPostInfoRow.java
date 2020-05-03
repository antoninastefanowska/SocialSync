package com.antonina.socialsynchro.services.deviantart.database.rows;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.rows.ChildPostContainerRow;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.services.deviantart.model.DeviantArtPostContainer;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "deviantart_post_info", foreignKeys = {
        @ForeignKey(entity = ChildPostContainerRow.class, parentColumns = "id", childColumns = "id", onDelete = CASCADE)})
public class DeviantArtPostInfoRow implements IDatabaseRow {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "stash_id")
    public String stashID;

    @ColumnInfo(name = "url")
    public String url;

    @ColumnInfo(name = "fave_count")
    public int faveCount;

    @ColumnInfo(name = "comment_count")
    public int commentCount;

    @Override
    public void createFromEntity(IDatabaseEntity entity) {
        DeviantArtPostContainer post = (DeviantArtPostContainer)entity;
        this.id = post.getInternalID();
        this.stashID = post.getStashID();
        this.url = post.getURL();
        this.faveCount = post.getFaveCount();
        this.commentCount = post.getCommentCount();
    }

    @Override
    public long getID() {
        return id;
    }
}
