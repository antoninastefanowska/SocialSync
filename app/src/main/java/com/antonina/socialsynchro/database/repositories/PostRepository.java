package com.antonina.socialsynchro.database.repositories;

import android.app.Application;

import com.antonina.socialsynchro.content.Post;
import com.antonina.socialsynchro.database.ApplicationDatabase;
import com.antonina.socialsynchro.database.tables.PostTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostRepository extends BaseRepository<PostTable, Post> {
    private static PostRepository instance;

    private PostRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.postDao();
        loadAllData();
    }

    public static PostRepository getInstance(Application application) {
        if (instance == null)
            instance = new PostRepository(application);
        return instance;
    }

    @Override
    protected Map<Long, Post> convertToEntities(List<PostTable> input) {
        Map<Long, Post> output = new HashMap<Long, Post>();
        for (PostTable postData : input) {
            Post post = new Post(postData);
            output.put(post.getInternalID(), post);
        }
        return output;
    }

    @Override
    protected PostTable convertToTable(Post entity, boolean isNew) {
        PostTable data = new PostTable();
        if (isNew)
            data.createFromNewEntity(entity);
        else
            data.createFromExistingEntity(entity);
        return data;
    }
}