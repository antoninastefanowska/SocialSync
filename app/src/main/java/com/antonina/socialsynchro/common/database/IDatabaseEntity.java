package com.antonina.socialsynchro.common.database;

import com.antonina.socialsynchro.common.database.tables.IDatabaseTable;

public interface IDatabaseEntity {
    void createFromData(IDatabaseTable data);
    Long getInternalID();
    void saveInDatabase();
    void updateInDatabase();
    void deleteFromDatabase();
}
