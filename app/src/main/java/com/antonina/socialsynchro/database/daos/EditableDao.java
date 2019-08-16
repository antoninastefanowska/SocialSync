package com.antonina.socialsynchro.database.daos;

public interface EditableDao<DataTable> extends ReadOnlyDao<DataTable> {
    long insert(DataTable dataTable);
    void delete(DataTable dataTable);
    void update(DataTable dataTable);
}
