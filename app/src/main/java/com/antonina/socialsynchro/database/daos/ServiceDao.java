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
    LiveData<List<ServiceTable>> getServicesData();

    @Query("SELECT * FROM service WHERE id = :id")
    LiveData<ServiceTable> getServiceDataByID(long id);

    @Query("SELECT COUNT(*) FROM service")
    int count();

    @Insert
    long insert(ServiceTable serviceData);

    @Insert
    void insertMany(List<ServiceTable> servicesData);

    @Update
    void update(ServiceTable serviceData);

    @Delete
    void delete(ServiceTable serviceData);
}
