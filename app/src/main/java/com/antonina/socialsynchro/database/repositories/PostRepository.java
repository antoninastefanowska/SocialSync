package com.antonina.socialsynchro.database.repositories;

import android.app.Application;

import com.antonina.socialsynchro.database.ApplicationDatabase;
import com.antonina.socialsynchro.database.tables.PostTable;

public class PostRepository extends EditableRepository<PostTable> {
    public PostRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.postDao();
    }
}
