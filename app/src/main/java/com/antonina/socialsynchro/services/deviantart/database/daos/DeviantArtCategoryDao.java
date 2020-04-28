package com.antonina.socialsynchro.services.deviantart.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.common.database.daos.BaseDao;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtCategoryRow;

import java.util.List;

@Dao
public interface DeviantArtCategoryDao extends BaseDao<DeviantArtCategoryRow> {
    @Query("SELECT * FROM deviantart_category")
    LiveData<List<DeviantArtCategoryRow>> getAllData();

    @Query("SELECT * FROM deviantart_category WHERE id = :deviantartCategoryID")
    LiveData<DeviantArtCategoryRow> getDataByID(long deviantartCategoryID);

    @Query("SELECT * FROM deviantart_category WHERE external_id = '/'")
    LiveData<DeviantArtCategoryRow> getRootCategory();

    @Query("SELECT * FROM deviantart_category WHERE parent_category_id = :parentCategoryID")
    LiveData<List<DeviantArtCategoryRow>> getDataByParentCategory(long parentCategoryID);

    @Query("SELECT COUNT(*) FROM deviantart_category WHERE external_id = :externalID")
    boolean categoryExists(String externalID);

    @Query("SELECT id FROM deviantart_category WHERE external_id = :externalID")
    long getIDByExternalID(String externalID);

    @Query("SELECT COUNT(*) FROM deviantart_category")
    int count();

    @Insert
    long insert(DeviantArtCategoryRow categoryRow);

    @Update
    void update(DeviantArtCategoryRow categoryRows);

    @Delete
    void delete(DeviantArtCategoryRow categoryRow);

    @Delete
    void deleteMany(List<DeviantArtCategoryRow> categoryRows);
}
