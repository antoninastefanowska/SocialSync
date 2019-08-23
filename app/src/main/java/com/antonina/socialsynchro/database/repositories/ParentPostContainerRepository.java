package com.antonina.socialsynchro.database.repositories;

import android.app.Application;

import com.antonina.socialsynchro.content.ParentPostContainer;
import com.antonina.socialsynchro.database.ApplicationDatabase;
import com.antonina.socialsynchro.database.tables.ParentPostContainerTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParentPostContainerRepository extends BaseRepository<ParentPostContainerTable, ParentPostContainer> {
    private static ParentPostContainerRepository instance;

    private ParentPostContainerRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.parentPostContainerDao();
        loadAllData();
    }

    public static ParentPostContainerRepository getInstance(Application application) {
        if (instance == null)
            instance = new ParentPostContainerRepository(application);
        return instance;
    }

    @Override
    protected Map<Long, ParentPostContainer> convertToEntities(List<ParentPostContainerTable> input) {
        Map<Long, ParentPostContainer> output = new HashMap<Long, ParentPostContainer>();
        for (ParentPostContainerTable parentPostContainerData : input) {
            ParentPostContainer parentPostContainer = new ParentPostContainer(parentPostContainerData);
            output.put(parentPostContainer.getInternalID(), parentPostContainer);
        }
        return output;
    }

    @Override
    protected ParentPostContainerTable convertToTable(ParentPostContainer entity, boolean isNew) {
        ParentPostContainerTable data = new ParentPostContainerTable();
        if (isNew)
            data.createFromNewEntity(entity);
        else
            data.createFromExistingEntity(entity);
        return data;
    }
}
