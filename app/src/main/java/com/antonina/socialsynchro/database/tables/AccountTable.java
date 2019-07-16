package com.antonina.socialsynchro.database.tables;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import com.antonina.socialsynchro.base.Service;

@Entity(tableName = "account", foreignKeys = @ForeignKey(entity = Service.class, parentColumns = "id", childColumns = "service_id"))
public class AccountTable {
    public Long id;

    public String name;
    public String serviceExternalIdentifier;
    public String profilePictureUrl;
    public String accessToken;
    public String secretToken;

    public Long serviceId;
}
