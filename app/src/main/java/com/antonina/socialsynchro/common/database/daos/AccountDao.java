package com.antonina.socialsynchro.common.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.common.database.rows.AccountRow;

import java.util.List;

@Dao
public interface AccountDao extends BaseDao<AccountRow> {
    @Query("SELECT * FROM account ORDER BY connecting_date DESC")
    LiveData<List<AccountRow>> getAllData();

    @Query("SELECT COUNT(*) FROM account")
    int count();

    @Query("SELECT * FROM account WHERE id = :accountID")
    LiveData<AccountRow> getDataByID(long accountID);

    @Query("SELECT * FROM account WHERE service_id = :serviceID ORDER BY connecting_date DESC")
    LiveData<List<AccountRow>> getDataByService(int serviceID);

    @Query("SELECT id FROM account WHERE external_id = :externalID AND service_id = :serviceID")
    long getIDByExternalIDAndService(String externalID, int serviceID);

    @Query("SELECT COUNT(*) FROM account WHERE external_id = :externalID AND service_id = :serviceID")
    boolean accountExists(String externalID, int serviceID);

    @Insert
    long insert(AccountRow accountRow);

    @Update
    void update(AccountRow accountRow);

    @Delete
    void delete(AccountRow accountRow);

    @Delete
    void deleteMany(List<AccountRow> accountRows);
}
