package com.antonina.socialsynchro.common.database.repositories;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;

import com.antonina.socialsynchro.common.content.attachments.Attachment;
import com.antonina.socialsynchro.common.content.attachments.AttachmentType;
import com.antonina.socialsynchro.common.content.attachments.AttachmentTypes;
import com.antonina.socialsynchro.common.content.posts.Post;
import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.daos.AttachmentDao;
import com.antonina.socialsynchro.common.database.rows.AttachmentRow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AttachmentRepository extends BaseRepository<AttachmentRow, Attachment> {
    private static AttachmentRepository instance;

    private AttachmentRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.attachmentDao();
    }

    public static AttachmentRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new AttachmentRepository(application);
    }

    @Override
    protected Attachment convertToEntity(AttachmentRow dataRow) {
        AttachmentType attachmentType = AttachmentTypes.getAttachmentType(dataRow.attachmentTypeID);
        return attachmentType.createAttachment(dataRow);
    }

    @Override
    protected AttachmentRow convertToDataRow(Attachment entity) {
        AttachmentRow dataRow = new AttachmentRow();
        dataRow.createFromEntity(entity);
        return dataRow;
    }

    public LiveData<List<Attachment>> getDataByPost(Post post) {
        try {
            long postID = post.getInternalID();
            AttachmentDao attachmentDao = (AttachmentDao)dao;
            GetDataByPostAsyncTask asyncTask = new GetDataByPostAsyncTask(attachmentDao);
            LiveData<List<AttachmentRow>> dataRows = asyncTask.execute(postID).get();
            LiveData<List<Attachment>> entities = Transformations.map(dataRows, new Function<List<AttachmentRow>, List<Attachment>>() {
                @Override
                public List<Attachment> apply(List<AttachmentRow> input) {
                    List<Attachment> output = new ArrayList<>();
                    for (AttachmentRow dataRow : input)
                        output.add(convertToEntity(dataRow));
                    return output;
                }
            });
            return entities;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class GetDataByPostAsyncTask extends AsyncTask<Long, Void, LiveData<List<AttachmentRow>>> {
        private final AttachmentDao dao;

        public GetDataByPostAsyncTask(AttachmentDao attachmentDao) {
            this.dao = attachmentDao;
        }

        @Override
        protected LiveData<List<AttachmentRow>> doInBackground(Long... params) {
            return dao.getDataByPost(params[0]);
        }
    }
}
