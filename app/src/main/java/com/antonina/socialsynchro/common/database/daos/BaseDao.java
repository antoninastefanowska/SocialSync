package com.antonina.socialsynchro.common.database.daos;

import android.arch.lifecycle.LiveData;

import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;

import java.util.List;

@SuppressWarnings("EmptyMethod")
public interface BaseDao<DataRowType extends IDatabaseRow> {
    LiveData<List<DataRowType>> getAllData();
    LiveData<DataRowType> getDataByID(long id);
    int count();
    long insert(DataRowType dataRow);
    void update(DataRowType dataRows);
    void delete(DataRowType dataRow);
    void deleteMany(List<DataRowType> dataRows);
}
