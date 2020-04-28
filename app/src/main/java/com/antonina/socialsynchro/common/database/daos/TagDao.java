package com.antonina.socialsynchro.common.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.common.database.rows.TagRow;

import java.util.List;

@Dao
public interface TagDao extends BaseDao<TagRow> {
    @Query("SELECT * FROM tag")
    LiveData<List<TagRow>> getAllData();

    @Query("SELECT COUNT(*) FROM tag")
    int count();

    @Query("SELECT * FROM tag WHERE id = :tagID")
    LiveData<TagRow> getDataByID(long tagID);

    @Query("SELECT * FROM tag WHERE post_id = :postID")
    LiveData<List<TagRow>> getDataByPost(long postID);

    @Insert
    long insert(TagRow tagRow);

    @Update
    void update(TagRow tagRow);

    @Delete
    void delete(TagRow tagRow);

    @Delete
    void deleteMany(List<TagRow> tagRows);
}
