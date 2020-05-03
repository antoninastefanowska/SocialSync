package com.antonina.socialsynchro.services.facebook.database.rows;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.rows.ChildPostContainerRow;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.services.facebook.model.FacebookPostContainer;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "facebook_post_info", foreignKeys = {
        @ForeignKey(entity = ChildPostContainerRow.class, parentColumns = "id", childColumns = "id", onDelete = CASCADE)})
public class FacebookPostInfoRow implements IDatabaseRow {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "reaction_count")
    public int reactionCount;

    @ColumnInfo(name = "comment_count")
    public int commentCount;

    @Override
    public void createFromEntity(IDatabaseEntity entity) {
        FacebookPostContainer post = (FacebookPostContainer)entity;
        this.id = post.getInternalID();
        this.reactionCount = post.getReactionCount();
        this.commentCount = post.getCommentCount();
    }

    @Override
    public long getID() {
        return id;
    }
}
