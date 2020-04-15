package com.antonina.socialsynchro.services.facebook.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.common.database.daos.BaseDao;
import com.antonina.socialsynchro.services.facebook.database.rows.FacebookPostInfoRow;

import java.util.List;

@Dao
public interface FacebookPostInfoDao extends BaseDao<FacebookPostInfoRow> {
    @Query("SELECT * FROM facebook_post_info")
    LiveData<List<FacebookPostInfoRow>> getAllData();

    @Query("SELECT COUNT(*) FROM facebook_post_info")
    int count();

    @Query("SELECT * FROM facebook_post_info WHERE id = :facebookPostID")
    LiveData<FacebookPostInfoRow> getDataByID(long facebookPostID);

    @Insert
    long insert(FacebookPostInfoRow postRow);

    @Update
    void update(FacebookPostInfoRow postRow);

    @Delete
    void delete(FacebookPostInfoRow postRow);

    @Delete
    void deleteMany(List<FacebookPostInfoRow> postRows);
}
