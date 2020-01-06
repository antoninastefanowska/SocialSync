package com.antonina.socialsynchro.common.database.repositories;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;
import android.util.Pair;

import com.antonina.socialsynchro.common.content.posts.Post;
import com.antonina.socialsynchro.common.content.attachments.Attachment;
import com.antonina.socialsynchro.common.content.attachments.AttachmentFactory;
import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.daos.AttachmentDao;
import com.antonina.socialsynchro.common.database.rows.AttachmentRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("WeakerAccess")
public class AttachmentRepository extends BaseRepository<AttachmentRow, Attachment> {
    private static AttachmentRepository instance;

    private AttachmentRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.attachmentDao();
        loadAllData();
    }

    public static AttachmentRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new AttachmentRepository(application);
    }

    @Override
    protected Map<Long, Attachment> convertToEntities(List<AttachmentRow> input) {
        Map<Long, Attachment> output = new TreeMap<>();
        for (AttachmentRow attachmentData : input) {
            Attachment attachment = (Attachment)AttachmentFactory.getInstance().createFromDatabaseRow(attachmentData);
            output.put(attachment.getInternalID(), attachment);
        }
        return output;
    }

    @Override
    protected AttachmentRow convertToRow(Attachment entity) {
        AttachmentRow data = new AttachmentRow();
        data.createFromEntity(entity);
        return data;
    }

    @Override
    protected List<Attachment> sortList(List<Attachment> list) {
        return list;
    }

    public LiveData<List<Attachment>> getDataByPost(Post post) {
        long postID = post.getInternalID();
        LiveData<List<Attachment>> result = null;
        try {
            AttachmentDao attachmentDao = (AttachmentDao)dao;
            LiveData<List<Long>> IDs = new GetIDByPostAsyncTask(attachmentDao).execute(postID).get();
            FilterSource<Attachment> filterSource = new FilterSource<>(IDs, getAllData());

            result = Transformations.map(filterSource, new Function<Pair<List<Long>, Map<Long, Attachment>>, List<Attachment>>() {
                @Override
                public List<Attachment> apply(Pair<List<Long>, Map<Long, Attachment>> input) {
                    List<Attachment> output = new ArrayList<>();
                    for (Long id : input.first)
                        output.add(input.second.get(id));
                    return sortList(output);
                }
            });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static class GetIDByPostAsyncTask extends AsyncTask<Long, Void, LiveData<List<Long>>> {
        private final AttachmentDao dao;

        public GetIDByPostAsyncTask(AttachmentDao dao) { this.dao = dao; }

        @Override
        protected LiveData<List<Long>> doInBackground(final Long... params) {
            return dao.getIDsByPost(params[0]);
        }
    }
}
