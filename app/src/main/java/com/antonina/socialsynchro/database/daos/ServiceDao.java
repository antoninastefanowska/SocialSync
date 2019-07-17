package com.antonina.socialsynchro.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.database.tables.ServiceTable;

import java.util.List;

@Dao
public interface ServiceDao {
    @Query("SELECT * FROM service")
    LiveData<List<ServiceTable>> getServices();

    @Query("SELECT * FROM service WHERE id = :serviceId")
    LiveData<ServiceTable> getServiceById(long serviceId);

    @Query("SELECT COUNT(*) FROM service")
    int count();

    @Insert
    long insert(ServiceTable serviceTable);

    @Insert
    void insertMany(List<ServiceTable> serviceTables);

    @Update
    void update(ServiceTable serviceTable);

    @Delete
    void delete(ServiceTable serviceTable);
}
