package com.antonina.socialsynchro.database;

import com.antonina.socialsynchro.database.tables.ITable;

public interface IDatabaseEntity {
    void convertFromTable(ITable table);
}
