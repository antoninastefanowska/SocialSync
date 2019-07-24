package com.antonina.socialsynchro.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.database.tables.PostTable;

import java.util.List;

@Dao
public interface PostDao {
    @Query("SELECT * FROM post")
    LiveData<List<PostTable>> getPostsData();

    @Query("SELECT * FROM post WHERE id = :id")
    LiveData<PostTable> getPostDataByID(long id);

    @Query("SELECT COUNT(*) FROM post")
    int count();

    @Insert
    long insert(PostTable postData);

    @Update
    void update(PostTable postData);

    @Delete
    void delete(PostTable postData);
}
