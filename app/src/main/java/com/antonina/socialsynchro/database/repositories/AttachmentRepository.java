package com.antonina.socialsynchro.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.antonina.socialsynchro.database.ApplicationDatabase;
import com.antonina.socialsynchro.database.daos.AttachmentDao;
import com.antonina.socialsynchro.database.tables.AttachmentTable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AttachmentRepository extends EditableRepository<AttachmentTable> {
    public AttachmentRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.attachmentDao();
    }

    public LiveData<List<AttachmentTable>> getDataByPost(long postID) {
        LiveData<List<AttachmentTable>> result = null;
        try {
            AttachmentDao attachmentDao = (AttachmentDao)dao;
            result = new GetDataByPostAsyncTask(attachmentDao).execute(postID).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static class GetDataByPostAsyncTask extends AsyncTask<Long, Void, LiveData<List<AttachmentTable>>> {
        private AttachmentDao dao;

        public GetDataByPostAsyncTask(AttachmentDao dao) { this.dao = dao; }

        @Override
        protected LiveData<List<AttachmentTable>> doInBackground(final Long... params) {
            return dao.getDataByPost(params[0]);
        }
    }
}
