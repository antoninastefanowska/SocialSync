package com.antonina.socialsynchro.database.daos;

import android.arch.lifecycle.LiveData;

import java.util.List;

public interface ReadOnlyDao<DataTable> {
    LiveData<List<DataTable>> getAllData();
    LiveData<DataTable> getDataByID(long id);
    int count();
}
