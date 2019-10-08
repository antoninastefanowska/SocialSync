package com.antonina.socialsynchro.common.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.common.database.rows.RequestLimitRow;

import java.util.List;

@Dao
public interface RequestLimitDao extends BaseDao<RequestLimitRow> {
    @Query("SELECT * FROM request_limit")
    LiveData<List<RequestLimitRow>> getAllData();

    @Query("SELECT * FROM request_limit WHERE id = :id")
    LiveData<RequestLimitRow> getDataByID(long id);

    @Query("SELECT COUNT(*) FROM request_limit")
    int count();

    @Query("SELECT id FROM request_limit WHERE account_id = :accountID")
    LiveData<List<Long>> getIDsByAccount(long accountID);

    @Query("SELECT id FROM request_limit WHERE service_id = :serviceID AND account_id IS NULL")
    LiveData<List<Long>> getIDsForApplicationByService(int serviceID);

    @Insert
    long insert(RequestLimitRow dataTable);

    @Delete
    void delete(RequestLimitRow dataTable);

    @Update
    void update(RequestLimitRow dataTable);
}
