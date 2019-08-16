package com.antonina.socialsynchro.database.viewmodels;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.antonina.socialsynchro.content.ChildPostContainer;
import com.antonina.socialsynchro.content.ChildPostContainerFactory;
import com.antonina.socialsynchro.database.repositories.ChildPostContainerRepository;
import com.antonina.socialsynchro.database.tables.ChildPostContainerTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChildPostContainerViewModel extends AndroidViewModel implements IEditableViewModel<ChildPostContainer> {
    private static ChildPostContainerViewModel instance;
    private ChildPostContainerRepository repository;
    private LiveData<Map<Long, ChildPostContainer>> childPostContainers;

    public ChildPostContainerViewModel getInstance(@NonNull Application application) {
        if (instance == null)
            instance = new ChildPostContainerViewModel(application);
        return instance;
    }

    private ChildPostContainerViewModel(@NonNull Application application) {
        super(application);

        LiveData<List<ChildPostContainerTable>> childPostContainersData = repository.getAllData();
        childPostContainers = Transformations.map(childPostContainersData, new Function<List<ChildPostContainerTable>, Map<Long, ChildPostContainer>>() {
            @Override
            public Map<Long, ChildPostContainer> apply(List<ChildPostContainerTable> input) {
                Map<Long, ChildPostContainer> output = new HashMap<Long, ChildPostContainer>();
                for (ChildPostContainerTable data : input) {
                    ChildPostContainer childPostContainer = (ChildPostContainer)ChildPostContainerFactory.getInstance().createFromData(data);
                    output.put(childPostContainer.getID(), childPostContainer);
                }
                return output;
            }
        });
    }

    @Override
    public LiveData<Map<Long, ChildPostContainer>> getAllEntities() {
        return childPostContainers;
    }

    @Override
    public LiveData<ChildPostContainer> getEntityByID(long childPostContainerID) {
        final long id = childPostContainerID;
        LiveData<ChildPostContainer> childPostContainer = Transformations.map(childPostContainers, new Function<Map<Long, ChildPostContainer>, ChildPostContainer>() {
            @Override
            public ChildPostContainer apply(Map<Long, ChildPostContainer> input) {
                return input.get(id);
            }
        });
        return null;
    }

    @Override
    public int count() {
        return repository.count();
    }

    @Override
    public long insert(ChildPostContainer childPostContainer) {
        ChildPostContainerTable data = new ChildPostContainerTable();
        data.createFromNewEntity(childPostContainer);
        return repository.insert(data);
    }

    @Override
    public void update(ChildPostContainer childPostContainer) {
        ChildPostContainerTable data = new ChildPostContainerTable();
        data.createFromExistingEntity(childPostContainer);
        repository.update(data);
    }

    @Override
    public void delete(ChildPostContainer childPostContainer) {
        ChildPostContainerTable data = new ChildPostContainerTable();
        data.createFromExistingEntity(childPostContainer);
        repository.delete(data);
    }
}
