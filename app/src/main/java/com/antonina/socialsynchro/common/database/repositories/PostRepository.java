package com.antonina.socialsynchro.common.database.repositories;

import android.app.Application;

import com.antonina.socialsynchro.common.model.posts.Post;
import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.rows.PostRow;

public class PostRepository extends BaseRepository<PostRow, Post> {
    private static PostRepository instance;

    private PostRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.postDao();
    }

    public static PostRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new PostRepository(application);
    }

    @Override
    protected Post convertToEntity(PostRow dataRow) {
        return new Post(dataRow);
    }

    @Override
    protected PostRow convertToDataRow(Post entity) {
        PostRow dataRow = new PostRow();
        dataRow.createFromEntity(entity);
        return dataRow;
    }
}
