package com.antonina.socialsynchro.services.twitter.database.rows;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.services.twitter.content.TwitterPostOptions;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "twitter_post_options", foreignKeys = {
        @ForeignKey(entity = TwitterPostInfoRow.class, parentColumns = "id", childColumns = "id", onDelete = CASCADE)})
public class TwitterPostOptionsRow implements IDatabaseRow {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "possibly_sensitive")
    public boolean possiblySensitive;

    @Override
    public void createFromEntity(IDatabaseEntity entity) {
        if (entity.getInternalID() != null)
            this.id = entity.getInternalID();
        TwitterPostOptions options = (TwitterPostOptions)entity;
        this.possiblySensitive = options.isPossiblySensitive();
    }

    @Override
    public long getID() {
        return id;
    }
}
