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
    @Query("SELECT * FROM account")
    LiveData<List<AccountRow>> getAllData();

    @Query("SELECT COUNT(*) FROM account")
    int count();

    @Query("SELECT * FROM account WHERE id = :accountID")
    LiveData<AccountRow> getDataByID(long accountID);

    @Query("SELECT id FROM account WHERE service_id = :serviceID")
    LiveData<List<Long>> getIDsByService(int serviceID);

    @Query("SELECT id FROM account WHERE external_id = :externalID AND service_id = :serviceID")
    long getIDByExternalIDAndService(String externalID, int serviceID);

    @Query("SELECT COUNT(*) FROM account WHERE external_id = :externalID AND service_id = :serviceID")
    boolean accountExists(String externalID, int serviceID);

    @Insert
    long insert(AccountRow accountData);

    @Update
    void update(AccountRow accountData);

    @Delete
    void delete(AccountRow accountData);
}
