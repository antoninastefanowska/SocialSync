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
public interface AccountDao extends BaseDao<AccountTable> {
    @Query("SELECT * FROM account")
    LiveData<List<AccountTable>> getAllData();

    @Query("SELECT COUNT(*) FROM account")
    int count();

    @Query("SELECT * FROM account WHERE id = :accountID")
    LiveData<AccountTable> getDataByID(long accountID);

    @Query("SELECT id FROM account WHERE service_id = :serviceID")
    LiveData<List<Long>> getIDByService(long serviceID);

    @Insert
    long insert(AccountTable accountData);

    @Update
    void update(AccountTable accountData);

    @Delete
    void delete(AccountTable accountData);
}
