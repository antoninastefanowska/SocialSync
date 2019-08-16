package com.antonina.socialsynchro.database.viewmodels;

import android.arch.lifecycle.LiveData;

import java.util.Map;

public interface IReadOnlyViewModel<Entity> {
    LiveData<Map<Long, Entity>> getAllEntities();
    LiveData<Entity> getEntityByID(long id);
    int count();
}
