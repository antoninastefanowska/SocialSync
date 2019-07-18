package com.antonina.socialsynchro.database.tables;

import com.antonina.socialsynchro.database.IDatabaseEntity;

public interface ITable {
    void createFromEntity(IDatabaseEntity entity);
    void createFromNewEntity(IDatabaseEntity entity);
}
