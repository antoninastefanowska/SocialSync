package com.antonina.socialsynchro.services.twitter.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.common.database.daos.BaseDao;
import com.antonina.socialsynchro.services.twitter.database.rows.TwitterPostOptionsRow;

import java.util.List;

@Dao
public interface TwitterPostOptionsDao extends BaseDao<TwitterPostOptionsRow> {
    @Query("SELECT * FROM twitter_post_options")
    LiveData<List<TwitterPostOptionsRow>> getAllData();

    @Query("SELECT * FROM twitter_post_options WHERE id = :id")
    LiveData<TwitterPostOptionsRow> getDataByID(long id);

    @Query("SELECT COUNT(*) FROM twitter_post_options")
    int count();

    @Insert
    long insert(TwitterPostOptionsRow dataRow);

    @Update
    void update(TwitterPostOptionsRow dataRows);

    @Delete
    void delete(TwitterPostOptionsRow dataRow);

    @Delete
    void deleteMany(List<TwitterPostOptionsRow> dataRows);
}
