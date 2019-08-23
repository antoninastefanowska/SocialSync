package com.antonina.socialsynchro.database;

import com.antonina.socialsynchro.database.tables.IDatabaseTable;

public interface IDatabaseEntityFactory {
    IDatabaseEntity createFromData(IDatabaseTable data);
}
