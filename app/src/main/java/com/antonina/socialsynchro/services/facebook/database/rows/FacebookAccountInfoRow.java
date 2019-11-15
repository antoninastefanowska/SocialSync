package com.antonina.socialsynchro.services.facebook.database.rows;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.rows.AccountRow;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.utils.SecurityUtils;
import com.antonina.socialsynchro.services.facebook.content.FacebookAccount;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "facebook_account_info", foreignKeys = {
        @ForeignKey(entity = AccountRow.class, parentColumns = "id", childColumns = "id", onDelete = CASCADE)})
public class FacebookAccountInfoRow implements IDatabaseRow {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "access_token")
    public String accessToken;

    @Override
    public void createFromEntity(IDatabaseEntity entity) {
        FacebookAccount account = (FacebookAccount)entity;
        this.id = account.getInternalID();
        this.accessToken = SecurityUtils.encrypt(account.getAccessToken());
    }

    @Override
    public long getID() {
        return id;
    }
}
