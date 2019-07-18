package com.antonina.socialsynchro.base;

import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.tables.ITable;

public interface IFactory {
    IDatabaseEntity createFromData(ITable data);
}
