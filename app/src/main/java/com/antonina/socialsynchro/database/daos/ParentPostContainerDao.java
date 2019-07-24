package com.antonina.socialsynchro.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.content.ParentPostContainer;
import com.antonina.socialsynchro.database.tables.ParentPostContainerTable;

import java.util.List;

@Dao
public interface ParentPostContainerDao {
    @Query("SELECT * FROM parent_post_container")
    LiveData<List<ParentPostContainerTable>> getParentPostContainersData();

    @Query("SELECT * FROM parent_post_container WHERE id = :id")
    LiveData<ParentPostContainerTable> getParentPostContainerDataByID(long id);

    @Query("SELECT COUNT(*) FROM parent_post_container")
    int count();

    @Insert
    long insert(ParentPostContainerTable parentPostContainerData);

    @Update
    void update(ParentPostContainerTable parentPostContainerData);

    @Delete
    void delete(ParentPostContainerTable parentPostContainerData);


}
