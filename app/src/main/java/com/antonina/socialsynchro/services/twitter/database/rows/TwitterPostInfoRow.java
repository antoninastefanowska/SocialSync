package com.antonina.socialsynchro.services.twitter.database.rows;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.rows.ChildPostContainerRow;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.services.twitter.content.TwitterPostContainer;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "twitter_post_info", foreignKeys = {
        @ForeignKey(entity = ChildPostContainerRow.class, parentColumns = "id", childColumns = "id", onDelete = CASCADE)})
public class TwitterPostInfoRow implements IDatabaseRow {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "retweet_count")
    public int retweetCount;

    @ColumnInfo(name = "favorite_count")
    public int favoriteCount;

    @Override
    public void createFromEntity(IDatabaseEntity entity) {
        TwitterPostContainer post = (TwitterPostContainer)entity;
        this.id = post.getInternalID();
        this.retweetCount = post.getRetweetCount();
        this.favoriteCount = post.getFavoriteCount();
        Log.d("baza", "Saving, favorites: " + favoriteCount);
    }

    @Override
    public long getID() {
        return id;
    }
}
