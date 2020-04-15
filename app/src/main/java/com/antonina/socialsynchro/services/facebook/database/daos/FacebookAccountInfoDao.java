package com.antonina.socialsynchro.services.facebook.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.common.database.daos.BaseDao;
import com.antonina.socialsynchro.services.facebook.database.rows.FacebookAccountInfoRow;

import java.util.List;

@Dao
public interface FacebookAccountInfoDao extends BaseDao<FacebookAccountInfoRow> {
    @Query("SELECT * FROM facebook_account_info")
    LiveData<List<FacebookAccountInfoRow>> getAllData();

    @Query("SELECT COUNT(*) FROM facebook_account_info")
    int count();

    @Query("SELECT * FROM facebook_account_info WHERE id = :facebookAccountID")
    LiveData<FacebookAccountInfoRow> getDataByID(long facebookAccountID);

    @Insert
    long insert(FacebookAccountInfoRow accountRow);

    @Update
    void update(FacebookAccountInfoRow accountRow);

    @Delete
    void delete(FacebookAccountInfoRow accountRow);

    @Delete
    void deleteMany(List<FacebookAccountInfoRow> accountRows);
}
