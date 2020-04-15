package com.antonina.socialsynchro.services.twitter.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.common.database.daos.BaseDao;
import com.antonina.socialsynchro.services.twitter.database.rows.TwitterAccountInfoRow;

import java.util.List;

@Dao
public interface TwitterAccountInfoDao extends BaseDao<TwitterAccountInfoRow> {
    @Query("SELECT * FROM twitter_account_info")
    LiveData<List<TwitterAccountInfoRow>> getAllData();

    @Query("SELECT COUNT(*) FROM twitter_account_info")
    int count();

    @Query("SELECT * FROM twitter_account_info WHERE id = :twitterAccountID")
    LiveData<TwitterAccountInfoRow> getDataByID(long twitterAccountID);

    @Insert
    long insert(TwitterAccountInfoRow accountRow);

    @Update
    void update(TwitterAccountInfoRow accountRow);

    @Delete
    void delete(TwitterAccountInfoRow accountRow);

    @Delete
    void deleteMany(List<TwitterAccountInfoRow> accountInfoRows);
}
