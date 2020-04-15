package com.antonina.socialsynchro.common.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.common.database.rows.ParentPostContainerRow;

import java.util.List;

@Dao
public interface ParentPostContainerDao extends BaseDao<ParentPostContainerRow> {
    @Query("SELECT parent.* FROM parent_post_container parent JOIN post ON parent.post_id = post.id ORDER BY post.creation_date DESC")
    LiveData<List<ParentPostContainerRow>> getAllData();

    @Query("SELECT * FROM parent_post_container WHERE id = :id")
    LiveData<ParentPostContainerRow> getDataByID(long id);

    @Query("SELECT COUNT(*) FROM parent_post_container")
    int count();

    @Insert
    long insert(ParentPostContainerRow parentPostContainerRow);

    @Update
    void update(ParentPostContainerRow parentPostContainerRow);

    @Delete
    void delete(ParentPostContainerRow parentPostContainerRow);

    @Delete
    void deleteMany(List<ParentPostContainerRow> parentPostContainerRows);
}
