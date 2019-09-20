package com.antonina.socialsynchro.content;

import com.antonina.socialsynchro.content.attachments.Attachment;
import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.repositories.PostRepository;
import com.antonina.socialsynchro.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.database.tables.PostTable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Post implements IPost, IDatabaseEntity {
    private Long internalID;
    private Date creationDate;
    private Date modificationDate;
    private String title;
    private String content;
    private List<Attachment> attachments;

    public Post() {
        title = "";
        content = "";
        attachments = new ArrayList<Attachment>();
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
    public Date getCreationDate() {
        return creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    @Override
    public List<Attachment> getAttachments() {
        return attachments;
    }

    @Override
    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    @Override
    public void addAttachment(Attachment attachment) {
        attachments.add(attachment);
        attachment.setParentPost(this);
    }

    @Override
    public void removeAttachment(Attachment attachment) {
        if (attachments.isEmpty())
            return;
        attachments.remove(attachment);
        attachment.setParentPost(null);
    }

    @Override
    public void createFromData(IDatabaseTable data) {
        PostTable postData = (PostTable)data;
        this.internalID = postData.id;
        this.title = postData.title;
        this.content = postData.content;
        this.creationDate = postData.creationDate;
        this.modificationDate = postData.modificationDate;
        this.attachments = new ArrayList<Attachment>();
        // TODO: Pobrać listę załączników
    }

    @Override
    public Long getInternalID() { return internalID; }

    @Override
    public void saveInDatabase() {
        if (internalID != null)
            return;
        creationDate = Calendar.getInstance().getTime();
        modificationDate = creationDate;
        PostRepository repository = PostRepository.getInstance();
        internalID = repository.insert(this);
    }

    @Override
    public void updateInDatabase() {
        if (internalID == null)
            return;
        modificationDate = Calendar.getInstance().getTime();
        PostRepository repository = PostRepository.getInstance();
        repository.update(this);
    }

    @Override
    public void deleteFromDatabase() {
        if (internalID == null)
            return;
        PostRepository repository = PostRepository.getInstance();
        repository.delete(this);
        internalID = null;
    }
}
