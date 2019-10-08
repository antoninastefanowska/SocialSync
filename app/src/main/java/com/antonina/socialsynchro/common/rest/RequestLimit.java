package com.antonina.socialsynchro.common.rest;

import android.util.Log;

import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.content.services.Service;
import com.antonina.socialsynchro.common.content.services.Services;
import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.repositories.RequestLimitRepository;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.database.rows.RequestLimitRow;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class RequestLimit implements IDatabaseEntity, IServiceEntity, Serializable {
    private Long internalID;
    private Account account;
    private Service service;
    private String endpoint;
    private Date synchronizationDate;
    private Date initializationDate;

    private int limit;
    private int remaining;
    private long resetWindowSeconds;
    private long secondsUntilReset;

    public RequestLimit() {
        initializationDate = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
    }

    public RequestLimit(String endpoint) {
        initializationDate = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
        this.endpoint = endpoint;
    }

    public RequestLimit(IDatabaseRow data) {
        createFromDatabaseRow(data);
    }

    public int getLimit() {
        return limit;
    }

    public long getResetWindowSeconds() {
        return resetWindowSeconds;
    }

    public int getRemaining() {
        updateState();
        return remaining;
    }

    public int getRemainingStatic() {
        return remaining;
    }

    public long getSecondsUntilReset() {
        updateState();
        return secondsUntilReset;
    }

    public long getSecondsUntilResetStatic() {
        return secondsUntilReset;
    }

    private void updateState() {
        if (remaining < limit) {
            Date currentDate = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
            long elapsed = (currentDate.getTime() - initializationDate.getTime()) / 1000;
            if (elapsed > resetWindowSeconds) {
                remaining = limit;
                secondsUntilReset = resetWindowSeconds;
            } else
                secondsUntilReset = resetWindowSeconds - elapsed;
            saveInDatabase();
        }
    }

    public void decrement() {
        if (remaining > 0) {
            if (remaining == limit)
                initializationDate = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
            remaining--;
            saveInDatabase();
        }
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public void setResetWindowSeconds(long resetWindowSeconds) {
        this.resetWindowSeconds = resetWindowSeconds;
    }

    public void setSecondsUntilReset(long secondsUntilReset) {
        this.secondsUntilReset = secondsUntilReset;
    }

    public void setAccount(Account account) {
        this.account = account;
        this.service = account.getService();
    }

    public void setService(Service service) {
        this.service = service;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setSynchronizationDate(Date synchronizationDate) {
        this.synchronizationDate = synchronizationDate;
    }

    @Override
    public void createFromDatabaseRow(IDatabaseRow data) {
        RequestLimitRow requestLimitData = (RequestLimitRow)data;
        this.internalID = requestLimitData.getID();
        this.service = Services.getService(requestLimitData.serviceID);
        this.endpoint = requestLimitData.endpoint;
        this.synchronizationDate = requestLimitData.synchronizationDate;
        this.initializationDate = requestLimitData.initializationDate;
        this.limit = requestLimitData.limit;
        this.remaining = requestLimitData.remaining;
        this.resetWindowSeconds = requestLimitData.resetWindowSeconds;
        this.secondsUntilReset = requestLimitData.secondsUntilReset;
    }

    @Override
    public Long getInternalID() {
        return internalID;
    }

    @Override
    public void saveInDatabase() {
        if (internalID != null)
            updateInDatabase();
        else {
            RequestLimitRepository repository = RequestLimitRepository.getInstance();
            internalID = repository.insert(this);
        }
    }

    @Override
    public void updateInDatabase() {
        RequestLimitRepository repository = RequestLimitRepository.getInstance();
        repository.update(this);
    }

    @Override
    public void deleteFromDatabase() {
        if (internalID == null)
            return;
        RequestLimitRepository repository = RequestLimitRepository.getInstance();
        repository.delete(this);
    }

    @Override
    public void createFromResponse(IResponse response) { }

    @Override
    public void setExternalID(String externalServiceIdentifier) { }

    @Override
    public String getExternalID() { return null; }

    @Override
    public Service getService() {
        return service;
    }

    @Override
    public void synchronize(OnSynchronizedListener listener) { }

    @Override
    public Date getSynchronizationDate() {
        return synchronizationDate;
    }

    public Date getInitializationDate() {
        return initializationDate;
    }

    public Account getAccount() {
        return account;
    }
}
