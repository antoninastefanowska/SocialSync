package com.antonina.socialsynchro.content.attachments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.databinding.Bindable;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.content.Post;
import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.repositories.AttachmentRepository;
import com.antonina.socialsynchro.database.repositories.PostRepository;
import com.antonina.socialsynchro.database.tables.AttachmentTable;
import com.antonina.socialsynchro.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.gui.GUIItem;
import com.antonina.socialsynchro.services.IResponse;
import com.antonina.socialsynchro.services.IServiceEntity;

import java.io.File;
import java.io.Serializable;

public abstract class Attachment extends GUIItem implements IDatabaseEntity, IServiceEntity, Serializable {
    private Long internalID;
    private String externalID;
    private File file;
    private int sizeKb;
    private AttachmentType attachmentType;
    private Post parentPost;
    private boolean loading;

    public Attachment() { }

    public Attachment(File file) {
        this.file = file;
    }

    @Override
    public Long getInternalID() { return internalID; }

    @Override
    public String getExternalID() {
        return externalID;
    }

    public void setExternalID(String externalID) {
        this.externalID = externalID;
    }

    @Bindable
    public File getFile() { return file; }

    public void setFile(File file) { this.file = file; }

    @Bindable
    public int getSizeKb() { return sizeKb; }

    public void setSizeKb(int sizeKb) { this.sizeKb = sizeKb; }

    public AttachmentType getAttachmentType() { return attachmentType; }

    public void setAttachmentType(AttachmentType attachmentType) { this.attachmentType = attachmentType; }

    public Post getParentPost() { return parentPost; }

    public void setParentPost(Post parentPost) {
        this.parentPost = parentPost;
    }

    public Attachment(IDatabaseTable data) {
        createFromData(data);
    }

    @Override
    public void createFromData(IDatabaseTable data) {
        AttachmentTable attachmentData = (AttachmentTable)data;
        this.internalID = attachmentData.id;
        this.externalID = attachmentData.externalID;
        this.file = new File(attachmentData.filepath);
        this.sizeKb = attachmentData.sizeKb;
        attachmentType = AttachmentTypes.getAttachmentType(attachmentData.attachmentTypeID);

        final Attachment instance = this;

        //TODO: Niech lista załączników pobierana będzie w obiekcie postu.
        final LiveData<Post> postLiveData = PostRepository.getInstance().getDataByID(attachmentData.postID);
        postLiveData.observeForever(new Observer<Post>() {
            @Override
            public void onChanged(@Nullable Post post) {
                if (post != null) {
                    post.addAttachment(instance);
                    notifyListener();
                    postLiveData.removeObserver(this);
                }
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
        internalID = null;
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
