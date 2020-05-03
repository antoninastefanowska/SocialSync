package com.antonina.socialsynchro.services.twitter.database.rows;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.rows.AccountRow;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.utils.SecurityUtils;
import com.antonina.socialsynchro.services.twitter.model.TwitterAccount;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "twitter_account_info", foreignKeys = {
        @ForeignKey(entity = AccountRow.class, parentColumns = "id", childColumns = "id", onDelete = CASCADE)})
public class TwitterAccountInfoRow implements IDatabaseRow {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "access_token")
    public String accessToken;

    @ColumnInfo(name = "secret_token")
    public String secretToken;

    @ColumnInfo(name = "follower_count")
    public int followerCount;

    @Override
    public void createFromEntity(IDatabaseEntity entity) {
        TwitterAccount account = (TwitterAccount)entity;
        this.id = account.getInternalID();
        this.accessToken = SecurityUtils.encrypt(account.getAccessToken());
        this.secretToken = SecurityUtils.encrypt(account.getSecretToken());
        this.followerCount = account.getFollowerCount();
    }

    @Override
    public long getID() {
        return id;
    }
}
