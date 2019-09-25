package com.antonina.socialsynchro.common.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.common.content.posts.Post;
import com.antonina.socialsynchro.common.database.IDatabaseEntity;

import java.util.Date;

@Entity(tableName = "post")
public class PostTable implements IDatabaseTable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "content")
    public String content;

    @ColumnInfo(name = "creation_date")
    public Date creationDate;

    @ColumnInfo(name = "modification_date")
    public Date modificationDate;

    public PostTable() { }

    public PostTable(IDatabaseEntity entity) {
        createFromExistingEntity(entity);
    }

    @Override
    public void createFromExistingEntity(IDatabaseEntity entity) {
        this.id = entity.getInternalID();
        createFromNewEntity(entity);
    }

    @Override
    public void createFromNewEntity(IDatabaseEntity entity) {
        Post post = (Post)entity;
        this.title = post.getTitle();
        this.content = post.getContent();
        this.creationDate = post.getCreationDate();
        this.modificationDate = post.getModificationDate();
    }

    @Override
    public long getID() {
        return id;
    }
}
