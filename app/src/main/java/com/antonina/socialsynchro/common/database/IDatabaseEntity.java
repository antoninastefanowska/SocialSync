package com.antonina.socialsynchro.common.database;

import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;

public interface IDatabaseEntity {
    void createFromDatabaseRow(IDatabaseRow data);
    Long getInternalID();
    void saveInDatabase();
    void updateInDatabase();
    void deleteFromDatabase();
}
