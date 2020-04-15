package com.antonina.socialsynchro.services.twitter.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.common.database.daos.BaseDao;
import com.antonina.socialsynchro.services.twitter.database.rows.TwitterPostInfoRow;

import java.util.List;

@Dao
public interface TwitterPostInfoDao extends BaseDao<TwitterPostInfoRow> {
    @Query("SELECT * FROM twitter_post_info")
    LiveData<List<TwitterPostInfoRow>> getAllData();

    @Query("SELECT COUNT(*) FROM twitter_post_info")
    int count();

    @Query("SELECT * FROM twitter_post_info WHERE id = :twitterPostID")
    LiveData<TwitterPostInfoRow> getDataByID(long twitterPostID);

    @Insert
    long insert(TwitterPostInfoRow postRow);

    @Update
    void update(TwitterPostInfoRow postRow);

    @Delete
    void delete(TwitterPostInfoRow postRow);

    @Delete
    void deleteMany(List<TwitterPostInfoRow> postRows);
}
