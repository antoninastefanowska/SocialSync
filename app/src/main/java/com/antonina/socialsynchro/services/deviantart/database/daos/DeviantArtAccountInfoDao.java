package com.antonina.socialsynchro.services.deviantart.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.common.database.daos.BaseDao;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtAccountInfoRow;

import java.util.List;

@Dao
public interface DeviantArtAccountInfoDao extends BaseDao<DeviantArtAccountInfoRow> {
    @Query("SELECT * FROM deviantart_account_info")
    LiveData<List<DeviantArtAccountInfoRow>> getAllData();

    @Query("SELECT COUNT(*) FROM deviantart_account_info")
    int count();

    @Query("SELECT * FROM deviantart_account_info WHERE id = :deviantartAccountID")
    LiveData<DeviantArtAccountInfoRow> getDataByID(long deviantartAccountID);

    @Insert
    long insert(DeviantArtAccountInfoRow accountData);

    @Update
    void update(DeviantArtAccountInfoRow accountData);

    @Delete
    void delete(DeviantArtAccountInfoRow accountData);
}
