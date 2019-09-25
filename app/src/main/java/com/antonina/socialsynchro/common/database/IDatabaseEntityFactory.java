package com.antonina.socialsynchro.common.database;

import com.antonina.socialsynchro.common.database.tables.IDatabaseTable;

public interface IDatabaseEntityFactory {
    IDatabaseEntity createFromData(IDatabaseTable data);
}
