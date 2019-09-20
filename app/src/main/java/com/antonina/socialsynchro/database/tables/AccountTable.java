package com.antonina.socialsynchro.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.database.IDatabaseEntity;

import java.util.Date;

@Entity(tableName = "account")
public class AccountTable implements IDatabaseTable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "external_id")
    public String externalID;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "profile_picture_url")
    public String profilePictureUrl;

    @ColumnInfo(name = "service_id")
    public int serviceID;

    @ColumnInfo(name = "connecting_date")
    public Date connectingDate;

    @Override
    public void createFromExistingEntity(IDatabaseEntity entity) {
        this.id = entity.getInternalID();
        createFromNewEntity(entity);
    }

    @Override
    public void createFromNewEntity(IDatabaseEntity entity) {
        Account account = (Account)entity;
        this.name = account.getName();
        this.externalID = account.getExternalID();
        this.profilePictureUrl = account.getExternalID();
        this.serviceID = account.getService().getID().ordinal();
        this.connectingDate = account.getConnectingDate();
    }

    @Override
    public long getID() {
        return id;
    }
}
