package com.antonina.socialsynchro.database.repositories;

import android.app.Application;

import com.antonina.socialsynchro.database.ApplicationDatabase;
import com.antonina.socialsynchro.database.tables.AttachmentTypeTable;

public class AttachmentTypeRepository extends ReadOnlyRepository<AttachmentTypeTable> {
    public AttachmentTypeRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.attachmentTypeDao();
    }
}
