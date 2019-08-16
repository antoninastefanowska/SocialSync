package com.antonina.socialsynchro.database.tables;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.antonina.socialsynchro.content.Post;
import com.antonina.socialsynchro.database.IDatabaseEntity;

@Entity(tableName = "post")
public class PostTable implements ITable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "content")
    public String content;

    public PostTable() { }

    public PostTable(IDatabaseEntity entity) {
        createFromExistingEntity(entity);
    }

    @Override
    public void createFromExistingEntity(IDatabaseEntity entity) {
        this.id = entity.getID();
        createFromNewEntity(entity);
    }

    @Override
    public void createFromNewEntity(IDatabaseEntity entity) {
        Post post = (Post)entity;
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
