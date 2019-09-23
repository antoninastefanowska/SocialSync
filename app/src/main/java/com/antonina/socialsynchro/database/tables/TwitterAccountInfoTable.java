package com.antonina.socialsynchro.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.services.twitter.TwitterAccount;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "twitter_account_info", foreignKeys = {
        @ForeignKey(entity = AccountTable.class, parentColumns = "id", childColumns = "id", onDelete = CASCADE)})
public class TwitterAccountInfoTable implements IDatabaseTable {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "access_token")
    public String accessToken;

    @ColumnInfo(name = "secret_token")
    public String secretToken;

    @Override
    public void createFromExistingEntity(IDatabaseEntity entity) {
        createFromNewEntity(entity);
    }

    @Override
    public void createFromNewEntity(IDatabaseEntity entity) {
        TwitterAccount account = (TwitterAccount)entity;
        this.id = account.getInternalID(); // TODO: ID musi być pobrane z tabeli AccountTable
        this.accessToken = account.getAccessToken();
        this.secretToken = account.getSecretToken();
    }

    @Override
    public long getID() {
        return id;
    }
}
