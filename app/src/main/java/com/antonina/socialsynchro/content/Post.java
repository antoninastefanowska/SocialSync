package com.antonina.socialsynchro.content;

import com.antonina.socialsynchro.content.attachments.Attachment;
import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.repositories.PostRepository;
import com.antonina.socialsynchro.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.database.tables.PostTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Post implements IPost, IDatabaseEntity, Serializable {
    private long internalID;
    private String title;
    private String content;
    private List<Attachment> attachments;

    public Post() {
        title = "";
        content = "";
    }

    public Post(IDatabaseTable data) { createFromData(data); }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public List<Attachment> getAttachments() {
        return attachments;
    }

    @Override
    public void addAttachment(Attachment attachment) {
        if (attachments == null)
            attachments = new ArrayList<Attachment>();
        attachments.add(attachment);
    }

    @Override
    public void removeAttachment(Attachment attachment) {
        if (attachments == null || attachments.isEmpty())
            return;
        attachments.remove(attachment);
    }

    @Override
    public void createFromData(IDatabaseTable data) {
        PostTable postData = (PostTable)data;
        this.internalID = postData.id;
        this.title = postData.title;
        this.content = postData.content;
    }

    @Override
    public long getInternalID() { return internalID; }

    @Override
    public void saveInDatabase() {
        PostRepository repository = PostRepository.getInstance();
        internalID = repository.insert(this);
    }

    @Override
    public void updateInDatabase() {
        PostRepository repository = PostRepository.getInstance();
        repository.update(this);
    }

    @Override
    public void deleteFromDatabase() {
        PostRepository repository = PostRepository.getInstance();
        repository.delete(this);
    }
}
