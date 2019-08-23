package com.antonina.socialsynchro.database.tables;

import com.antonina.socialsynchro.database.IDatabaseEntity;

public interface IDatabaseTable {
    void createFromExistingEntity(IDatabaseEntity entity);
    void createFromNewEntity(IDatabaseEntity entity);
    long getID();
}
