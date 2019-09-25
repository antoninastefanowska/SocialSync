package com.antonina.socialsynchro.common.database.tables;

import com.antonina.socialsynchro.common.database.IDatabaseEntity;

public interface IDatabaseTable {
    void createFromExistingEntity(IDatabaseEntity entity);
    void createFromNewEntity(IDatabaseEntity entity);
    long getID();
}
