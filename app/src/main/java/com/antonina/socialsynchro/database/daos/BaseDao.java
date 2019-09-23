package com.antonina.socialsynchro.database.daos;

import android.arch.lifecycle.LiveData;

import com.antonina.socialsynchro.database.tables.IDatabaseTable;

import java.util.List;

@SuppressWarnings("EmptyMethod")
public interface BaseDao<DataTableType extends IDatabaseTable> {
    LiveData<List<DataTableType>> getAllData();
    LiveData<DataTableType> getDataByID(long id);
    int count();
    long insert(DataTableType dataTable);
    void delete(DataTableType dataTable);
    void update(DataTableType dataTable);
}
