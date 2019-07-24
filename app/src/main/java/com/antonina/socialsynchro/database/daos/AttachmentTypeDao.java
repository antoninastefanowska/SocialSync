package com.antonina.socialsynchro.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.antonina.socialsynchro.database.tables.AttachmentTypeTable;

import java.util.List;

@Dao
public interface AttachmentTypeDao {
    @Query("SELECT * FROM attachment_type")
    LiveData<List<AttachmentTypeTable>> getAttachmentTypesData();

    @Query("SELECT * FROM attachment_type WHERE id = :id")
    LiveData<AttachmentTypeTable> getAttachmentTypeDataByID(long id);

    @Insert
    void insertMany(List<AttachmentTypeTable> attachmentTypesData);
}
