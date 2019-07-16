package com.antonina.socialsynchro.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.database.tables.AccountTable;

import java.util.List;

@Dao
public interface AccountDao {
    @Query("SELECT * FROM account")
    LiveData<List<AccountTable>> getAccounts();

    @Query("SELECT COUNT(*) FROM account")
    int count();

    @Query("SELECT * FROM account WHERE service_id = :serviceId")
    LiveData<List<AccountTable>> getAccountsByService(long serviceId);

    @Insert
    long insert(AccountTable accountTable);

    @Delete
    void delete(AccountTable accountTable);

    @Update
    void update(AccountTable accountTable);
}
