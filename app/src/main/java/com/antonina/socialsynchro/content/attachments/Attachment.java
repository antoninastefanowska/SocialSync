package com.antonina.socialsynchro.content.attachments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.SocialSynchro;
import com.antonina.socialsynchro.content.Post;
import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.tables.AttachmentTable;
import com.antonina.socialsynchro.database.tables.ITable;
import com.antonina.socialsynchro.database.viewmodels.AttachmentTypeViewModel;
import com.antonina.socialsynchro.database.viewmodels.PostViewModel;

public abstract class Attachment implements IDatabaseEntity {
    private long id;
    private String filename;
    private int sizeKb;
    private AttachmentType attachmentType;
    private Post parentPost;

    @Override
    public long getID() { return id; }

    public String getFilename() { return filename; }

    public void setFilename(String filename) { this.filename = filename; }

    public int getSizeKb() { return sizeKb; }

    public void setSizeKb(int sizeKb) { this.sizeKb = sizeKb; }

    public AttachmentType getAttachmentType() { return attachmentType; }

    public void setAttachmentType(AttachmentType attachmentType) { this.attachmentType = attachmentType; }

    public Post getParentPost() { return parentPost; }

    public void setParentPost(Post parentPost) {
        this.parentPost = parentPost;
        parentPost.addAttachment(this);
    }

    public Attachment(ITable data) {
        createFromData(data);
    }

    @Override
    public void createFromData(ITable data) {
        AttachmentTable attachmentData = (AttachmentTable)data;
        this.id = attachmentData.id;
        this.filename = attachmentData.filename;
        this.sizeKb = attachmentData.sizeKb;

        final Attachment instance = this;

        final LiveData<Post> postLiveData = PostViewModel.getInstance(SocialSynchro.getInstance()).getEntityByID(attachmentData.postID);
        postLiveData.observeForever(new Observer<Post>() {
            @Override
            public void onChanged(@Nullable Post post) {
                instance.setParentPost(post);
                postLiveData.removeObserver(this);
            }
        });
        final LiveData<AttachmentType> attachmentTypeLiveData = AttachmentTypeViewModel.getInstance(SocialSynchro.getInstance()).getEntityByID(attachmentData.attachmentTypeID);
        attachmentTypeLiveData.observeForever(new Observer<AttachmentType>() {
            @Override
            public void onChanged(@Nullable AttachmentType attachmentType) {
                instance.attachmentType = attachmentType;
                attachmentTypeLiveData.removeObserver(this);
            }
        });
    }
}
