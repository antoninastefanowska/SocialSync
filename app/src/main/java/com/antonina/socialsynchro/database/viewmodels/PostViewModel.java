package com.antonina.socialsynchro.database.viewmodels;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.antonina.socialsynchro.content.Post;
import com.antonina.socialsynchro.database.repositories.PostRepository;
import com.antonina.socialsynchro.database.tables.PostTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostViewModel extends AndroidViewModel implements IEditableViewModel<Post> {
    private static PostViewModel instance;
    private PostRepository repository;
    private LiveData<Map<Long, Post>> posts;

    public static PostViewModel getInstance(@NonNull Application application) {
        if (instance == null)
            instance = new PostViewModel(application);
        return instance;
    }

    private PostViewModel(@NonNull Application application) {
        super(application);
        repository = new PostRepository(application);

        LiveData<List<PostTable>> postData = repository.getAllData();
        posts = Transformations.map(postData, new Function<List<PostTable>, Map<Long, Post>>() {
            @Override
            public Map<Long, Post> apply(List<PostTable> input) {
                Map<Long, Post> output = new HashMap<Long, Post>();
                for (PostTable postData : input) {
                    Post post = new Post(postData);
                    output.put(post.getID(), post);
                }
                return output;
            }
        });
    }

    @Override
    public LiveData<Map<Long, Post>> getAllEntities() {
        return posts;
    }

    @Override
    public LiveData<Post> getEntityByID(long postID) {
        final long id = postID;
        LiveData<Post> post = Transformations.map(posts, new Function<Map<Long, Post>, Post>() {
            @Override
            public Post apply(Map<Long, Post> input) {
                return input.get(id);
            }
        });
        return post;
    }

    @Override
    public int count() {
        return repository.count();
    }

    @Override
    public long insert(Post post) {
        PostTable data = new PostTable();
        data.createFromNewEntity(post);
        return repository.insert(data);
    }

    @Override
    public void update(Post post) {
        PostTable data = new PostTable();
        data.createFromExistingEntity(post);
        repository.update(data);
    }

    @Override
    public void delete(Post post) {
        PostTable data = new PostTable();
        data.createFromExistingEntity(post);
        repository.delete(data);
    }
}
