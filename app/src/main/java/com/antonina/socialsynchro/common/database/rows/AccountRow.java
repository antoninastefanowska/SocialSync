package com.antonina.socialsynchro.common.database.rows;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.common.model.accounts.Account;
import com.antonina.socialsynchro.common.database.IDatabaseEntity;

import java.util.Date;

@Entity(tableName = "account")
public class AccountRow implements IDatabaseRow {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "external_id")
    public String externalID;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "profile_picture_url")
    public String profilePictureURL;

    @ColumnInfo(name = "service_id")
    public int serviceID;

    @ColumnInfo(name = "connecting_date")
    public Date connectingDate;

    @Override
    public void createFromEntity(IDatabaseEntity entity) {
        if (entity.getInternalID() != null)
            this.id = entity.getInternalID();

        Account account = (Account)entity;
        this.name = account.getName();
        this.externalID = account.getExternalID();
        this.profilePictureURL = account.getProfilePictureURL();
        this.serviceID = account.getService().getID().ordinal();
        this.connectingDate = account.getConnectingDate();
    }

    @Override
    public long getID() {
        return id;
    }
}
