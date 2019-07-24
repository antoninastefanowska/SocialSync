package com.antonina.socialsynchro.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.antonina.socialsynchro.database.ApplicationDatabase;
import com.antonina.socialsynchro.database.daos.PostDao;
import com.antonina.socialsynchro.database.tables.PostTable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PostRepository {
    private PostDao postDao;

    public PostRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        postDao = db.postDao();
    }

    public LiveData<List<PostTable>> getPostsData() {
        LiveData<List<PostTable>> result = null;
        try {
            result = new GetPostsDataAsyncTask(postDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public LiveData<PostTable> getPostDataByID(long id) {
        LiveData<PostTable> result = null;
        try {
            result = new GetPostDataByIDAsyncTask(postDao).execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int count() {
        int result = 0;
        try {
            result = new CountAsyncTask(postDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Long insert(PostTable postData) {
        Long result = null;
        try {
            result = new InsertAsyncTask(postDao).execute(postData).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void update(PostTable postData) {
        new UpdateAsyncTask(postDao).execute(postData);
    }

    public void delete(PostTable postData) {
        new DeleteAsyncTask(postDao).execute(postData);
    }

    private static class GetPostsDataAsyncTask extends AsyncTask<Void, Void, LiveData<List<PostTable>>> {
        private PostDao postDao;

        public GetPostsDataAsyncTask(PostDao dao) { postDao = dao; }

        @Override
        protected LiveData<List<PostTable>> doInBackground(Void... params) {
            return postDao.getPostsData();
        }
    }

    private static class GetPostDataByIDAsyncTask extends AsyncTask<Long, Void, LiveData<PostTable>> {
        private PostDao postDao;

        public GetPostDataByIDAsyncTask(PostDao dao) { postDao = dao; }

        @Override
        protected LiveData<PostTable> doInBackground(Long... params) {
            return postDao.getPostDataByID(params[0]);
        }
    }

    private static class CountAsyncTask extends AsyncTask<Void, Void, Integer> {
        private PostDao postDao;

        public CountAsyncTask(PostDao dao) { postDao = dao; }

        @Override
        protected Integer doInBackground(Void... params) {
            return postDao.count();
        }
    }

    private static class InsertAsyncTask extends AsyncTask<PostTable, Void, Long> {
        private PostDao postDao;

        public InsertAsyncTask(PostDao dao) { postDao = dao; }

        @Override
        protected Long doInBackground(PostTable... params) {
            return postDao.insert(params[0]);
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<PostTable, Void, Void> {
        private PostDao postDao;

        public UpdateAsyncTask(PostDao dao) { postDao = dao; }

        @Override
        protected Void doInBackground(PostTable... params) {
            postDao.update(params[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<PostTable, Void, Void> {
        private PostDao postDao;

        public DeleteAsyncTask(PostDao dao) { postDao = dao; }

        @Override
        protected Void doInBackground(PostTable... params) {
            postDao.delete(params[0]);
            return null;
        }
    }
}
