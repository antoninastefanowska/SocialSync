package com.antonina.socialsynchro.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.database.tables.ChildPostContainerTable;

import java.util.List;

@Dao
public interface ChildPostContainerDao extends EditableDao<ChildPostContainerTable> {
    @Query("SELECT * FROM child_post_container")
    LiveData<List<ChildPostContainerTable>> getAllData();

    @Query("SELECT * FROM child_post_container WHERE id = :id")
    LiveData<ChildPostContainerTable> getDataByID(long id);

    @Query("SELECT * FROM child_post_container WHERE parent_id = :parentID")
    LiveData<List<ChildPostContainerTable>> getDataByParent(long parentID);

    @Query("SELECT COUNT(*) FROM child_post_container")
    int count();

    @Insert
    long insert(ChildPostContainerTable childPostContainerData);

    @Update
    void update(ChildPostContainerTable childPostContainerData);

    @Delete
    void delete(ChildPostContainerTable childPostContainerData);
}
