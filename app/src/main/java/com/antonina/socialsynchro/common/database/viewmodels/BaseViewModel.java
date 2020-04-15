package com.antonina.socialsynchro.common.database.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.repositories.BaseRepository;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;

import java.util.List;

public abstract class BaseViewModel<DataRowType extends IDatabaseRow, EntityType extends IDatabaseEntity> extends AndroidViewModel {
    protected BaseRepository<DataRowType, EntityType> repository;
    protected LiveData<List<EntityType>> data;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        repository = getRepository(application);
        data = getAllData();
    }

    protected abstract BaseRepository<DataRowType, EntityType> getRepository(Application application);

    public LiveData<List<EntityType>> getCurrentData() {
        return data;
    }

    public LiveData<List<EntityType>> getAllData() {
        return repository.getAllData();
    }

    public LiveData<EntityType> getDataByID(long id) {
        return repository.getDataByID(id);
    }

    public int count() {
        return repository.count();
    }

    public Long insert(EntityType entity) {
        return repository.insert(entity);
    }

    public void update(EntityType entity) {
        repository.update(entity);
    }

    public void delete(EntityType entity) {
        repository.delete(entity);
    }

    public void deleteMany(List<EntityType> entities) {
        repository.deleteMany(entities);
    }
}
