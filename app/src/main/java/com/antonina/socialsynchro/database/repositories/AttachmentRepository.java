package com.antonina.socialsynchro.database.repositories;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;

import com.antonina.socialsynchro.content.Post;
import com.antonina.socialsynchro.content.attachments.Attachment;
import com.antonina.socialsynchro.content.attachments.AttachmentFactory;
import com.antonina.socialsynchro.database.ApplicationDatabase;
import com.antonina.socialsynchro.database.daos.AttachmentDao;
import com.antonina.socialsynchro.database.tables.AttachmentTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class AttachmentRepository extends BaseRepository<AttachmentTable, Attachment> {
    private static AttachmentRepository instance;

    private AttachmentRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.attachmentDao();
        loadAllData();
    }

    public static AttachmentRepository getInstance(Application application) {
        if (instance == null)
            instance = new AttachmentRepository(application);
        return instance;
    }

    @Override
    protected Map<Long, Attachment> convertToEntities(List<AttachmentTable> input) {
        Map<Long, Attachment> output = new HashMap<Long, Attachment>();
        for (AttachmentTable attachmentData : input) {
            Attachment attachment = (Attachment)AttachmentFactory.getInstance().createFromData(attachmentData);
            output.put(attachment.getInternalID(), attachment);
        }
        return output;
    }

    @Override
    protected AttachmentTable convertToTable(Attachment entity, boolean isNew) {
        AttachmentTable data = new AttachmentTable();
        if (isNew)
            data.createFromNewEntity(entity);
        else
            data.createFromExistingEntity(entity);
        return data;
    }

    public LiveData<List<Attachment>> getDataByPost(Post post) {
        long postID = post.getInternalID();
        LiveData<List<Attachment>> result = null;
        try {
            AttachmentDao attachmentDao = (AttachmentDao)dao;
            LiveData<List<AttachmentTable>> databaseData = new GetDataByPostAsyncTask(attachmentDao).execute(postID).get();
            result = Transformations.map(databaseData, new Function<List<AttachmentTable>, List<Attachment>>() {
                @Override
                public List<Attachment> apply(List<AttachmentTable> input) {
                    List<Attachment> output = new ArrayList<Attachment>();
                    for (AttachmentTable attachmentData : input) {
                        Attachment attachment = (Attachment)AttachmentFactory.getInstance().createFromData(attachmentData);
                        output.add(attachment);
                    }
                    return output;
                }
            });
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
