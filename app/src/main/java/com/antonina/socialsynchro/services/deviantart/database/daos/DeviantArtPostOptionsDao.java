package com.antonina.socialsynchro.services.deviantart.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.common.database.daos.BaseDao;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtPostOptionsRow;

import java.util.List;

@Dao
public interface DeviantArtPostOptionsDao extends BaseDao<DeviantArtPostOptionsRow> {
    @Query("SELECT * FROM deviantart_post_options")
    LiveData<List<DeviantArtPostOptionsRow>> getAllData();

    @Query("SELECT * FROM deviantart_post_options WHERE id = :id")
    LiveData<DeviantArtPostOptionsRow> getDataByID(long id);

    @Query("SELECT COUNT(*) FROM deviantart_post_options")
    int count();

    @Insert
    long insert(DeviantArtPostOptionsRow dataRow);

    @Update
    void update(DeviantArtPostOptionsRow dataRows);

    @Delete
    void delete(DeviantArtPostOptionsRow dataRow);

    @Delete
    void deleteMany(List<DeviantArtPostOptionsRow> dataRows);
}
