package com.antonina.socialsynchro.common.database.rows;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.rest.RequestLimit;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "request_limit", foreignKeys = {
        @ForeignKey(entity = AccountRow.class, parentColumns = "id", childColumns = "account_id", onDelete = CASCADE)})
public class RequestLimitRow implements IDatabaseRow {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "endpoint")
    public String endpoint;

    @ColumnInfo(name = "synchronization_date")
    public Date synchronizationDate;

    @ColumnInfo(name = "initialization_date")
    public Date initializationDate;

    @ColumnInfo(name = "limit")
    public int limit;

    @ColumnInfo(name = "remaining")
    public int remaining;

    @ColumnInfo(name = "reset_window")
    public long resetWindowSeconds;

    @ColumnInfo(name = "until_reset")
    public long secondsUntilReset;

    @ColumnInfo(name = "account_id", index = true)
    public Long accountID;

    @ColumnInfo(name = "service_id")
    public int serviceID;

    @Override
    public void createFromEntity(IDatabaseEntity entity) {
        if (entity.getInternalID() != null)
            this.id = entity.getInternalID();

        RequestLimit requestLimit = (RequestLimit)entity;
        this.endpoint = requestLimit.getEndpoint();
        this.synchronizationDate = requestLimit.getSynchronizationDate();
        this.initializationDate = requestLimit.getInitializationDate();
        this.limit = requestLimit.getLimit();
        this.remaining = requestLimit.getRemainingStatic();
        this.resetWindowSeconds = requestLimit.getResetWindowSeconds();
        this.secondsUntilReset = requestLimit.getSecondsUntilResetStatic();
        if (requestLimit.getAccount() != null)
            this.accountID = requestLimit.getAccount().getInternalID();
        this.serviceID = requestLimit.getService().getID().ordinal();
    }

    @Override
    public long getID() {
        return id;
    }
}
