package com.antonina.socialsynchro.services.deviantart.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.common.database.daos.BaseDao;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtGalleryRow;

import java.util.List;

@Dao
public interface DeviantArtGalleryDao extends BaseDao<DeviantArtGalleryRow> {
    @Query("SELECT * FROM deviantart_gallery")
    LiveData<List<DeviantArtGalleryRow>> getAllData();

    @Query("SELECT COUNT(*) FROM deviantart_gallery")
    int count();

    @Query("SELECT * FROM deviantart_gallery WHERE id = :deviantartGalleryID")
    LiveData<DeviantArtGalleryRow> getDataByID(long deviantartGalleryID);

    @Query("SELECT * FROM deviantart_gallery WHERE account_id = :accountID")
    LiveData<List<DeviantArtGalleryRow>> getDataByAccount(long accountID);

    @Query("SELECT COUNT(*) FROM deviantart_gallery WHERE external_id = :externalID")
    boolean galleryExists(String externalID);

    @Query("SELECT id FROM deviantart_gallery WHERE external_id = :externalID")
    long getIDByExternalID(String externalID);

    @RawQuery(observedEntities = DeviantArtGalleryRow.class)
    LiveData<List<DeviantArtGalleryRow>> getDataByQuery(SimpleSQLiteQuery query);

    @Insert
    long insert(DeviantArtGalleryRow galleryRow);

    @Update
    void update(DeviantArtGalleryRow galleryRow);

    @Delete
    void delete(DeviantArtGalleryRow galleryRow);

    @Delete
    void deleteMany(List<DeviantArtGalleryRow> galleryRows);
}
