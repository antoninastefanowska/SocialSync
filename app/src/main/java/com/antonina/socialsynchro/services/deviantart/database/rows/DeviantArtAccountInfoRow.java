package com.antonina.socialsynchro.services.deviantart.database.rows;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.rows.AccountRow;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.utils.SecurityUtils;
import com.antonina.socialsynchro.services.deviantart.model.DeviantArtAccount;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "deviantart_account_info", foreignKeys = {
        @ForeignKey(entity = AccountRow.class, parentColumns = "id", childColumns = "id", onDelete = CASCADE)})
public class DeviantArtAccountInfoRow implements IDatabaseRow {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "access_token")
    public String accessToken;

    @ColumnInfo(name = "refresh_token")
    public String refreshToken;

    @ColumnInfo(name = "token_date")
    public Date tokenDate;

    @ColumnInfo(name = "token_expires_in")
    public long tokenExiresIn;

    @ColumnInfo(name = "watcher_count")
    public int watcherCount;

    @Override
    public void createFromEntity(IDatabaseEntity entity) {
        DeviantArtAccount account = (DeviantArtAccount)entity;
        this.id = account.getInternalID();
        this.accessToken = SecurityUtils.encrypt(account.getAccessToken());
        this.refreshToken = SecurityUtils.encrypt(account.getRefreshToken());
        this.tokenDate = account.getTokenDate();
        this.tokenExiresIn = account.getTokenExpiresIn();
        this.watcherCount = account.getWatcherCount();
    }

    @Override
    public long getID() {
        return id;
    }
}
