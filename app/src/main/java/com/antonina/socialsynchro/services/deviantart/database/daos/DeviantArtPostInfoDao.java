package com.antonina.socialsynchro.services.deviantart.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.common.database.daos.BaseDao;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtPostInfoRow;

import java.util.List;

@Dao
public interface DeviantArtPostInfoDao extends BaseDao<DeviantArtPostInfoRow> {
    @Query("SELECT * FROM deviantart_post_info")
    LiveData<List<DeviantArtPostInfoRow>> getAllData();

    @Query("SELECT COUNT(*) FROM deviantart_post_info")
    int count();

    @Query("SELECT * FROM deviantart_post_info WHERE id = :deviantartPostID")
    LiveData<DeviantArtPostInfoRow> getDataByID(long deviantartPostID);

    @Insert
    long insert(DeviantArtPostInfoRow postData);

    @Update
    void update(DeviantArtPostInfoRow postData);

    @Delete
    void delete(DeviantArtPostInfoRow postData);
}
