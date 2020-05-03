package com.antonina.socialsynchro.common.database.repositories;

import android.app.Application;

import com.antonina.socialsynchro.common.model.posts.ParentPostContainer;
import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.rows.ParentPostContainerRow;

public class ParentPostContainerRepository extends BaseRepository<ParentPostContainerRow, ParentPostContainer> {
    private static ParentPostContainerRepository instance;

    private ParentPostContainerRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.parentPostContainerDao();
    }

    public static ParentPostContainerRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new ParentPostContainerRepository(application);
    }

    @Override
    protected ParentPostContainer convertToEntity(ParentPostContainerRow dataRow) {
        return new ParentPostContainer(dataRow);
    }

    @Override
    protected ParentPostContainerRow convertToDataRow(ParentPostContainer entity) {
        ParentPostContainerRow dataRow = new ParentPostContainerRow();
        dataRow.createFromEntity(entity);
        return dataRow;
    }
}
