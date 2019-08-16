package com.antonina.socialsynchro.database.viewmodels;

import android.arch.lifecycle.LiveData;

import java.util.Map;

public interface IEditableViewModel<Entity> extends IReadOnlyViewModel<Entity> {
    long insert(Entity entity);
    void update(Entity entity);
    void delete(Entity entity);
}
