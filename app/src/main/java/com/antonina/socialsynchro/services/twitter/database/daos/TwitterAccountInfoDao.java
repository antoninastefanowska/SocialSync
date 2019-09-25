package com.antonina.socialsynchro.services.twitter.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.common.database.daos.BaseDao;
import com.antonina.socialsynchro.services.twitter.database.tables.TwitterAccountInfoTable;

import java.util.List;

@Dao
public interface TwitterAccountInfoDao extends BaseDao<TwitterAccountInfoTable> {
    @Query("SELECT * FROM twitter_account_info")
    LiveData<List<TwitterAccountInfoTable>> getAllData();

    @Query("SELECT COUNT(*) FROM twitter_account_info")
    int count();

    @Query("SELECT * FROM twitter_account_info WHERE id = :twitterAccountID")
    LiveData<TwitterAccountInfoTable> getDataByID(long twitterAccountID);

    @Insert
    long insert(TwitterAccountInfoTable accountData);

    @Update
    void update(TwitterAccountInfoTable accountData);

    @Delete
    void delete(TwitterAccountInfoTable accountData);
}
