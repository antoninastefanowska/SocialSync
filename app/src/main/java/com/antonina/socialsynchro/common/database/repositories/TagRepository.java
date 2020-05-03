package com.antonina.socialsynchro.common.database.repositories;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;

import com.antonina.socialsynchro.common.model.posts.Post;
import com.antonina.socialsynchro.common.model.posts.Tag;
import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.daos.TagDao;
import com.antonina.socialsynchro.common.database.rows.TagRow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TagRepository extends BaseRepository<TagRow, Tag> {
    private static TagRepository instance;

    private TagRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.tagDao();
    }

    public static TagRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new TagRepository(application);
    }

    @Override
    protected Tag convertToEntity(TagRow dataRow) {
        return new Tag(dataRow);
    }

    @Override
    protected TagRow convertToDataRow(Tag entity) {
        TagRow dataRow = new TagRow();
        dataRow.createFromEntity(entity);
        return dataRow;
    }

    public LiveData<List<Tag>> getDataByPost(Post post) {
        try {
            long postID = post.getInternalID();
            TagDao tagDao = (TagDao)dao;
            GetDataByPostAsyncTask asyncTask = new GetDataByPostAsyncTask(tagDao);
            LiveData<List<TagRow>> dataRows = asyncTask.execute(postID).get();
            LiveData<List<Tag>> entities = Transformations.map(dataRows, new Function<List<TagRow>, List<Tag>>() {
                @Override
                public List<Tag> apply(List<TagRow> input) {
                    List<Tag> output = new ArrayList<>();
                    for (TagRow dataRow : input)
                        output.add(convertToEntity(dataRow));
                    return output;
                }
            });
            return entities;
        } catch (ExecutionException | InterruptedException e) {
            handleException(e);
            return null;
        }
    }

    private static class GetDataByPostAsyncTask extends AsyncTask<Long, Void, LiveData<List<TagRow>>> {
        private TagDao dao;

        public GetDataByPostAsyncTask(TagDao tagDao) {
            this.dao = tagDao;
        }

        @Override
        protected LiveData<List<TagRow>> doInBackground(Long... params) {
            return dao.getDataByPost(params[0]);
        }
    }
}
