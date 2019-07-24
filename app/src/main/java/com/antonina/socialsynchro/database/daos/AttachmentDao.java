package com.antonina.socialsynchro.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.database.tables.AttachmentTable;

import java.util.List;

@Dao
public interface AttachmentDao {
    @Query("SELECT * FROM attachment")
    LiveData<List<AttachmentTable>> getAttachmentsData();

    @Query("SELECT * FROM attachment WHERE post_id = :postID")
    LiveData<List<AttachmentTable>> getAttachmentsDataByPost(long postID);

    @Query("SELECT COUNT(*) FROM attachment")
    int count();

    @Insert
    long insert(AttachmentTable attachmentData);

    @Update
    void update(AttachmentTable attachmentData);

    @Delete
    void delete(AttachmentTable attachmentData);
}
