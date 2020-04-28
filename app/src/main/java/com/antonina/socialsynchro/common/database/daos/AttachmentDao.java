package com.antonina.socialsynchro.common.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.antonina.socialsynchro.common.database.rows.AttachmentRow;

import java.util.List;

@Dao
public interface AttachmentDao extends BaseDao<AttachmentRow> {
    @Query("SELECT * FROM attachment")
    LiveData<List<AttachmentRow>> getAllData();

    @Query("SELECT * FROM attachment WHERE id = :attachmentID")
    LiveData<AttachmentRow> getDataByID(long attachmentID);

    @Query("SELECT * FROM attachment WHERE post_id = :postID")
    LiveData<List<AttachmentRow>> getDataByPost(long postID);

    @Query("SELECT COUNT(*) FROM attachment")
    int count();

    @Insert
    long insert(AttachmentRow attachmentRow);

    @Update
    void update(AttachmentRow attachmentRow);

    @Delete
    void delete(AttachmentRow attachmentRow);

    @Delete
    void deleteMany(List<AttachmentRow> attachmentRows);
}
