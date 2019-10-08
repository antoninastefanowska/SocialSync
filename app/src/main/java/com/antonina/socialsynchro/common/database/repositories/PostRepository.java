package com.antonina.socialsynchro.common.database.repositories;

import android.app.Application;

import com.antonina.socialsynchro.common.content.posts.Post;
import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.rows.PostRow;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PostRepository extends BaseRepository<PostRow, Post> {
    private static PostRepository instance;

    private PostRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.postDao();
        loadAllData();
    }

    public static PostRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new PostRepository(application);
    }

    @Override
    protected Map<Long, Post> convertToEntities(List<PostRow> input) {
        Map<Long, Post> output = new TreeMap<>();
        for (PostRow postData : input) {
            Post post = new Post(postData);
            output.put(post.getInternalID(), post);
        }
        return output;
    }

    @Override
    protected PostRow convertToRow(Post entity) {
        PostRow data = new PostRow();
        data.createFromEntity(entity);
        return data;
    }

    @Override
    protected List<Post> sortList(List<Post> list) {
        return list;
    }
}