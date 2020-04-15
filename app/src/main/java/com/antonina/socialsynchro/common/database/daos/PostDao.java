package com.antonina.socialsynchro.common.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.common.database.rows.PostRow;

import java.util.List;

@Dao
public interface PostDao extends BaseDao<PostRow> {
    @Query("SELECT * FROM post")
    LiveData<List<PostRow>> getAllData();

    @Query("SELECT * FROM post WHERE id = :postID")
    LiveData<PostRow> getDataByID(long postID);

    @Query("SELECT COUNT(*) FROM post")
    int count();

    @Insert
    long insert(PostRow postRow);

    @Update
    void update(PostRow postRow);

    @Delete
    void delete(PostRow postRow);

    @Delete
    void deleteMany(List<PostRow> postRows);
}
