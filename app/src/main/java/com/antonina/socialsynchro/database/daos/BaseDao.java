package com.antonina.socialsynchro.database.daos;

import android.arch.lifecycle.LiveData;

import java.util.List;

public interface BaseDao<DataTableClass> {
    LiveData<List<DataTableClass>> getAllData();
    LiveData<DataTableClass> getDataByID(long id);
    int count();
    long insert(DataTableClass dataTable);
    void delete(DataTableClass dataTable);
    void update(DataTableClass dataTable);
}
