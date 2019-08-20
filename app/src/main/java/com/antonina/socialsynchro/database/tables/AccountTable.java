package com.antonina.socialsynchro.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.base.Service;

@Entity(tableName = "account", foreignKeys = @ForeignKey(entity = ServiceTable.class, parentColumns = "id", childColumns = "service_id"), indices = @Index(value = "id", unique = true))
public class AccountTable implements ITable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "service_external_identifier")
    public String serviceExternalIdentifier;

    @ColumnInfo(name = "profile_picture_url")
    public String profilePictureUrl;

    @ColumnInfo(name = "access_token")
    public String accessToken;

    @ColumnInfo(name = "secret_token")
    public String secretToken;

    @ColumnInfo(name = "service_id", index = true)
    public long serviceID;

    @Override
    public void createFromExistingEntity(IDatabaseEntity entity) {
        this.id = entity.getID();
        createFromNewEntity(entity);
    }

    @Override
    public void createFromNewEntity(IDatabaseEntity entity) {
        Account account = (Account)entity;
        this.name = account.getName();
        this.serviceExternalIdentifier = account.getServiceExternalIdentifier();
        this.profilePictureUrl = account.getServiceExternalIdentifier();
        this.accessToken = account.getAccessToken();
        this.secretToken = account.getSecretToken();
        this.serviceID = account.getService().getID();
    }
}
