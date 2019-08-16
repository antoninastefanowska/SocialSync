package com.antonina.socialsynchro.database.repositories;

import android.app.Application;

import com.antonina.socialsynchro.database.ApplicationDatabase;
import com.antonina.socialsynchro.database.tables.ParentPostContainerTable;

public class ParentPostContainerRepository extends EditableRepository<ParentPostContainerTable> {
    public ParentPostContainerRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.parentPostContainerDao();
    }
}
