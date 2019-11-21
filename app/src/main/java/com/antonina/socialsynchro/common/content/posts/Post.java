package com.antonina.socialsynchro.common.content.posts;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.SocialSynchro;
import com.antonina.socialsynchro.common.content.attachments.Attachment;
import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.repositories.AttachmentRepository;
import com.antonina.socialsynchro.common.database.repositories.PostRepository;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.database.rows.PostRow;
import com.antonina.socialsynchro.common.gui.GUIItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Post extends GUIItem implements IPost, IDatabaseEntity {
    private Long internalID;
    private Date creationDate;
    private Date modificationDate;
    private String title;
    private String content;
    private List<Attachment> attachments;
    private List<Attachment> deletedAttachments;

    public Post() {
        title = "";
        content = "";
        attachments = new ArrayList<>();
        deletedAttachments = new ArrayList<>();
    }

    public Post(IDatabaseRow data) { createFromDatabaseRow(data); }

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
        if (title.isEmpty())
            return SocialSynchro.getInstance().getResources().getString(R.string.no_title);
        else
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
        attachment.setParentPost(this);
        attachments.add(attachment);
        notifyListener();
    }

    @Override
    public void removeAttachment(Attachment attachment) {
        if (attachments.isEmpty())
            return;
        attachments.remove(attachment);
        attachment.setParentPost(null);
        if (attachment.getInternalID() != null)
            deletedAttachments.add(attachment);
        notifyListener();
    }

    @Override
    public void createFromDatabaseRow(IDatabaseRow data) {
        PostRow postData = (PostRow)data;
        this.internalID = postData.getID();
        this.title = postData.title;
        this.content = postData.content;
        this.creationDate = postData.creationDate;
        this.modificationDate = postData.modificationDate;

        this.attachments = new ArrayList<>();
        this.deletedAttachments = new ArrayList<>();

        final Post instance = this;
        final LiveData<List<Attachment>> attachmentsLiveData = AttachmentRepository.getInstance().getDataByPost(this);
        attachmentsLiveData.observeForever(new Observer<List<Attachment>>() {
            @Override
            public void onChanged(@Nullable List<Attachment> attachments) {
                if (attachments != null) {
                    for (Attachment attachment : attachments)
                        if (attachment != null)
                            instance.addAttachment(attachment);
                    attachmentsLiveData.removeObserver(this);
                }
            }
        });
    }

    @Override
    public Long getInternalID() { return internalID; }

    @Override
    public void saveInDatabase() {
        if (internalID != null)
            updateInDatabase();
        else {
            creationDate = Calendar.getInstance().getTime();
            modificationDate = creationDate;
            PostRepository repository = PostRepository.getInstance();
            internalID = repository.insert(this);
            for (Attachment attachment : attachments)
                attachment.saveInDatabase();
        }
    }

    @Override
    public void updateInDatabase() {
        modificationDate = Calendar.getInstance().getTime();
        PostRepository repository = PostRepository.getInstance();
        repository.update(this);

        for (Attachment deletedAttachment : deletedAttachments)
            deletedAttachment.deleteFromDatabase();

        deletedAttachments.clear();

        for (Attachment attachment : attachments)
            attachment.saveInDatabase();
    }

    @Override
    public void deleteFromDatabase() {
        if (internalID == null)
            return;
        for (Attachment attachment : attachments)
            attachment.deleteFromDatabase();
        PostRepository repository = PostRepository.getInstance();
        repository.delete(this);
        internalID = null;
    }
}
