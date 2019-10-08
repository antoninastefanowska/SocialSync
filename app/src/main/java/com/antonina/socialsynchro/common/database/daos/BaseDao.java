package com.antonina.socialsynchro.common.database.daos;

import android.arch.lifecycle.LiveData;

import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;

import java.util.List;

@SuppressWarnings("EmptyMethod")
public interface BaseDao<DataTableType extends IDatabaseRow> {
    LiveData<List<DataTableType>> getAllData();
    LiveData<DataTableType> getDataByID(long id);
    int count();
    long insert(DataTableType dataTable);
    void delete(DataTableType dataTable);
    void update(DataTableType dataTable);
}
