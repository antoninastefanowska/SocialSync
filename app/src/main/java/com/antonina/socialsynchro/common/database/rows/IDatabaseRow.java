package com.antonina.socialsynchro.common.database.rows;

import com.antonina.socialsynchro.common.database.IDatabaseEntity;

public interface IDatabaseRow {
    void createFromEntity(IDatabaseEntity entity);
    long getID();
}
