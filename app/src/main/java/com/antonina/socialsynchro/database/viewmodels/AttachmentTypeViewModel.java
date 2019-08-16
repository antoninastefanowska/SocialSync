package com.antonina.socialsynchro.database.viewmodels;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.antonina.socialsynchro.content.attachments.AttachmentType;
import com.antonina.socialsynchro.database.repositories.AttachmentTypeRepository;
import com.antonina.socialsynchro.database.tables.AttachmentTypeTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttachmentTypeViewModel extends AndroidViewModel implements IReadOnlyViewModel<AttachmentType> {
    private static AttachmentTypeViewModel instance;
    private AttachmentTypeRepository repository;
    private LiveData<Map<Long, AttachmentType>> attachmentTypes;

    public static AttachmentTypeViewModel getInstance(@NonNull Application application) {
        if (instance == null)
            instance = new AttachmentTypeViewModel(application);
        return instance;
    }

    private AttachmentTypeViewModel(@NonNull Application application) {
        super(application);
        LiveData<List<AttachmentTypeTable>> attachmentTypesData = repository.getAllData();
        attachmentTypes = Transformations.map(attachmentTypesData, new Function<List<AttachmentTypeTable>, Map<Long, AttachmentType>>() {
            @Override
            public Map<Long, AttachmentType> apply(List<AttachmentTypeTable> input) {
                Map<Long, AttachmentType> output = new HashMap<Long, AttachmentType>();
                for (AttachmentTypeTable data : input) {
                    AttachmentType attachmentType = new AttachmentType(data);
                    output.put(attachmentType.getID(), attachmentType);
                }
                return output;
            }
        });
    }

    @Override
    public LiveData<Map<Long, AttachmentType>> getAllEntities() {
        return attachmentTypes;
    }

    @Override
    public LiveData<AttachmentType> getEntityByID(long attachmentTypeID) {
        final long id = attachmentTypeID;
        LiveData<AttachmentType> attachmentType = Transformations.map(attachmentTypes, new Function<Map<Long, AttachmentType>, AttachmentType>() {
            @Override
            public AttachmentType apply(Map<Long, AttachmentType> input) {
                return input.get(id);
            }
        });
        return null;
    }

    @Override
    public int count() {
        return repository.count();
    }
}
