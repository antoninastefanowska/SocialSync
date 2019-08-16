package com.antonina.socialsynchro.database.viewmodels;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.antonina.socialsynchro.content.attachments.Attachment;
import com.antonina.socialsynchro.content.attachments.AttachmentFactory;
import com.antonina.socialsynchro.database.repositories.AttachmentRepository;
import com.antonina.socialsynchro.database.tables.AttachmentTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttachmentViewModel extends AndroidViewModel implements IEditableViewModel<Attachment> {
    private static AttachmentViewModel instance;
    private AttachmentRepository repository;
    private LiveData<Map<Long, Attachment>> attachments;

    public AttachmentViewModel getInstance(@NonNull Application application) {
        if (instance == null)
            instance = new AttachmentViewModel(application);
        return instance;
    }

    private AttachmentViewModel(@NonNull Application application) {
        super(application);

        LiveData<List<AttachmentTable>> attachmentsData = repository.getAllData();
        attachments = Transformations.map(attachmentsData, new Function<List<AttachmentTable>, Map<Long, Attachment>>() {
            @Override
            public Map<Long, Attachment> apply(List<AttachmentTable> input) {
                Map<Long, Attachment> output = new HashMap<Long, Attachment>();
                for (AttachmentTable data : input) {
                    Attachment attachment = (Attachment) AttachmentFactory.getInstance().createFromData(data);
                    output.put(attachment.getID(), attachment);
                }
                return output;
            }
        });
    }

    @Override
    public LiveData<Map<Long, Attachment>> getAllEntities() {
        return attachments;
    }

    @Override
    public LiveData<Attachment> getEntityByID(long attachmentID) {
        final long id = attachmentID;
        LiveData<Attachment> attachment = Transformations.map(attachments, new Function<Map<Long, Attachment>, Attachment>() {
            @Override
            public Attachment apply(Map<Long, Attachment> input) {
                return input.get(id);
            }
        });
        return attachment;
    }

    @Override
    public int count() {
        return repository.count();
    }

    @Override
    public long insert(Attachment attachment) {
        AttachmentTable data = new AttachmentTable();
        data.createFromNewEntity(attachment);
        return repository.insert(data);
    }

    @Override
    public void update(Attachment attachment) {
        AttachmentTable data = new AttachmentTable();
        data.createFromExistingEntity(attachment);
        repository.update(data);
    }

    @Override
    public void delete(Attachment attachment) {
        AttachmentTable data = new AttachmentTable();
        data.createFromExistingEntity(attachment);
        repository.delete(data);
    }
}
