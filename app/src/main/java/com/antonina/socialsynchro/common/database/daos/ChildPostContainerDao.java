package com.antonina.socialsynchro.common.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.common.database.rows.ChildPostContainerRow;

import java.util.List;

@Dao
public interface ChildPostContainerDao extends BaseDao<ChildPostContainerRow> {
    @Query("SELECT * FROM child_post_container")
    LiveData<List<ChildPostContainerRow>> getAllData();

    @Query("SELECT * FROM child_post_container WHERE id = :id")
    LiveData<ChildPostContainerRow> getDataByID(long id);

    @Query("SELECT id FROM child_post_container WHERE parent_id = :parentID")
    LiveData<List<Long>> getIDsByParent(long parentID);

    @Query("SELECT COUNT(*) FROM child_post_container")
    int count();

    @Insert
    long insert(ChildPostContainerRow childPostContainerData);

    @Update
    void update(ChildPostContainerRow childPostContainerData);

    @Delete
    void delete(ChildPostContainerRow childPostContainerData);
}
