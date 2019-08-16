package com.antonina.socialsynchro.database.viewmodels;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.antonina.socialsynchro.content.ParentPostContainer;
import com.antonina.socialsynchro.database.repositories.ParentPostContainerRepository;
import com.antonina.socialsynchro.database.tables.ParentPostContainerTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParentPostContainerViewModel extends AndroidViewModel implements IEditableViewModel<ParentPostContainer> {
    private static ParentPostContainerViewModel instance;
    private ParentPostContainerRepository repository;
    private LiveData<Map<Long, ParentPostContainer>> parentPostContainers;

    public static ParentPostContainerViewModel getInstance(@NonNull Application application) {
        if (instance == null)
            instance = new ParentPostContainerViewModel(application);
        return instance;
    }

    private ParentPostContainerViewModel(@NonNull Application application) {
        super(application);
        LiveData<List<ParentPostContainerTable>> parentPostContainersData = repository.getAllData();

        parentPostContainers = Transformations.map(parentPostContainersData, new Function<List<ParentPostContainerTable>, Map<Long, ParentPostContainer>>() {
            @Override
            public Map<Long, ParentPostContainer> apply(List<ParentPostContainerTable> input) {
                Map<Long, ParentPostContainer> output = new HashMap<Long, ParentPostContainer>();
                for (ParentPostContainerTable parentPostContainerData : input) {
                    ParentPostContainer parentPostContainer = new ParentPostContainer(parentPostContainerData);
                    output.put(parentPostContainer.getID(), parentPostContainer);
                }
                return output;
            }
        });
    }

    @Override
    public LiveData<Map<Long, ParentPostContainer>> getAllEntities() {
        return parentPostContainers;
    }

    @Override
    public LiveData<ParentPostContainer> getEntityByID(long parentPostContainerID) {
        final long id = parentPostContainerID;
        LiveData<ParentPostContainer> parentPostContainer = Transformations.map(parentPostContainers, new Function<Map<Long, ParentPostContainer>, ParentPostContainer>() {
            @Override
            public ParentPostContainer apply(Map<Long, ParentPostContainer> input) {
                return input.get(id);
            }
        });
        return parentPostContainer;
    }

    @Override
    public int count() {
        return repository.count();
    }

    @Override
    public long insert(ParentPostContainer parentPostContainer) {
        ParentPostContainerTable data = new ParentPostContainerTable();
        data.createFromNewEntity(parentPostContainer);
        return repository.insert(data);
    }

    @Override
    public void update(ParentPostContainer parentPostContainer) {
        ParentPostContainerTable data = new ParentPostContainerTable();
        data.createFromExistingEntity(parentPostContainer);
        repository.update(data);
    }

    @Override
    public void delete(ParentPostContainer parentPostContainer) {
        ParentPostContainerTable data = new ParentPostContainerTable();
        data.createFromExistingEntity(parentPostContainer);
        repository.delete(data);
    }
}
