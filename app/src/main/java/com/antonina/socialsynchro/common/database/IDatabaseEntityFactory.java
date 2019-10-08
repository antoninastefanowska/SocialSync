package com.antonina.socialsynchro.common.database;

import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;

public interface IDatabaseEntityFactory {
    IDatabaseEntity createFromDatabaseRow(IDatabaseRow data);
}
