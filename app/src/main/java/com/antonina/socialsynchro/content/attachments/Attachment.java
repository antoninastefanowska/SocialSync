package com.antonina.socialsynchro.content.attachments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.content.Post;
import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.repositories.AttachmentRepository;
import com.antonina.socialsynchro.database.repositories.PostRepository;
import com.antonina.socialsynchro.database.tables.AttachmentTable;
import com.antonina.socialsynchro.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.services.IResponse;
import com.antonina.socialsynchro.services.IServiceEntity;

public abstract class Attachment implements IDatabaseEntity, IServiceEntity {
    private long internalID;
    private String externalID;
    private String filename;
    private int sizeKb;
    private IAttachmentType attachmentType;
    private Post parentPost;
    private boolean loading;

    public Attachment() { }

    @Override
    public long getInternalID() { return internalID; }

    @Override
    public String getExternalID() {
        return externalID;
    }

    public void setExternalID(String externalID) {
        this.externalID = externalID;
    }

    public String getFilename() { return filename; }

    public void setFilename(String filename) { this.filename = filename; }

    public int getSizeKb() { return sizeKb; }

    public void setSizeKb(int sizeKb) { this.sizeKb = sizeKb; }

    public IAttachmentType getAttachmentType() { return attachmentType; }

    public void setAttachmentType(IAttachmentType attachmentType) { this.attachmentType = attachmentType; }

    public Post getParentPost() { return parentPost; }

    public void setParentPost(Post parentPost) {
        this.parentPost = parentPost;
        parentPost.addAttachment(this);
    }

    public Attachment(IDatabaseTable data) {
        createFromData(data);
    }

    @Override
    public void createFromData(IDatabaseTable data) {
        AttachmentTable attachmentData = (AttachmentTable)data;
        this.internalID = attachmentData.id;
        this.externalID = attachmentData.externalID;
        this.filename = attachmentData.filename;
        this.sizeKb = attachmentData.sizeKb;
        attachmentType = AttachmentTypes.getAttachmentType(attachmentData.attachmentTypeID);

        final Attachment instance = this;

        final LiveData<Post> postLiveData = PostRepository.getInstance().getDataByID(attachmentData.postID);
        postLiveData.observeForever(new Observer<Post>() {
            @Override
            public void onChanged(@Nullable Post post) {
                instance.setParentPost(post);
                postLiveData.removeObserver(this);
            }
        });
    }

    @Override
    public void createFromResponse(IResponse response) {
        // TODO
    }

    @Override
    public void saveInDatabase() {
        AttachmentRepository repository = AttachmentRepository.getInstance();
        internalID = repository.insert(this);
    }

    @Override
    public void updateInDatabase() {
        AttachmentRepository repository = AttachmentRepository.getInstance();
        repository.update(this);
    }

    @Override
    public void deleteFromDatabase() {
        AttachmentRepository repository = AttachmentRepository.getInstance();
        repository.delete(this);
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public void setLoading(boolean loading) {
        this.loading = loading;
    }
}
