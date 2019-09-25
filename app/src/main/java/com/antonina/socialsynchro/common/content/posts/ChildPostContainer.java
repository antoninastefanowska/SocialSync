package com.antonina.socialsynchro.common.content.posts;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.databinding.Bindable;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.common.content.services.Service;
import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.content.attachments.Attachment;
import com.antonina.socialsynchro.common.database.repositories.AccountRepository;
import com.antonina.socialsynchro.common.database.repositories.ChildPostContainerRepository;
import com.antonina.socialsynchro.common.database.repositories.PostRepository;
import com.antonina.socialsynchro.common.database.tables.ChildPostContainerTable;
import com.antonina.socialsynchro.common.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.common.gui.listeners.OnAttachmentUploadedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnPublishedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnUnpublishedListener;
import com.antonina.socialsynchro.common.rest.IServiceEntity;

import java.util.Date;
import java.util.List;

public abstract class ChildPostContainer extends PostContainer implements IServiceEntity {
    private String externalID;
    private boolean locked;
    private Date synchronizationDate;
    private Account account;

    protected ParentPostContainer parent;

    // TODO: dla każdej funkcji modyfikującej sprawdzić ograniczenia

    protected ChildPostContainer(IDatabaseTable data) {
        createFromData(data);
    }

    protected ChildPostContainer(Account account) {
        locked = true;
        post = null;
        setAccount(account);
    }

    @Bindable
    @Override
    public String getTitle() {
        if (locked)
            return parent.getPost().getTitle();
        else
            return post.getTitle();
    }

    @Bindable
    @Override
    public void setTitle(String title) {
        if (!locked)
            post.setTitle(title);
    }

    @Bindable
    @Override
    public String getContent() {
        if (locked)
            return parent.getContent();
        else
            return post.getContent();
    }

    @Bindable
    @Override
    public void setContent(String content) {
        if (!locked)
            post.setContent(content);
    }

    @Bindable
    @Override
    public Date getCreationDate() {
        if (locked)
            return parent.getCreationDate();
        else
            return post.getCreationDate();
    }

    @Bindable
    @Override
    public Date getModificationDate() {
        if (locked)
            return parent.getModificationDate();
        else
            return post.getModificationDate();
    }

    @Override
    public List<Attachment> getAttachments() {
        if (locked)
            return parent.getPost().getAttachments();
        else
            return post.getAttachments();
    }

    @Override
    public void addAttachment(Attachment attachment) {
        if (!locked) {
            post.addAttachment(attachment);
            notifyListener();
        }
    }

    @Override
    public void removeAttachment(Attachment attachment) {
        if (!locked) {
            post.removeAttachment(attachment);
            notifyListener();
        }
    }

    @Override
    public void setAttachments(List<Attachment> attachments) {
        if (!locked)
            post.setAttachments(attachments);
    }

    @Override
    public Post getPost() {
        if (locked)
            return parent.getPost();
        else
            return post;
    }

    @Override
    public abstract void publish(OnPublishedListener listener, OnAttachmentUploadedListener attachmentListener);

    @Override
    public abstract void unpublish(OnUnpublishedListener listener);

    @Bindable
    public boolean isLocked() {
        return locked;
    }

    public void lock() {
        locked = true;
        post = null;
    }

    public void unlock() {
        IPost parentPost = parent.getPost();
        locked = false;
        post = new Post();
        setTitle(parentPost.getTitle());
        setContent(parentPost.getContent());
        for (Attachment attachment : parentPost.getAttachments()) {
            addAttachment(attachment);
        }
        // TODO: kopia głęboka postu z parenta
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String getExternalID() {
        return externalID;
    }

    @Override
    public void setExternalID(String externalID) {
        this.externalID = externalID;
    }

    @Override
    public Service getService() {
        return account.getService();
    }

    public Date getSynchronizationDate() {
        return synchronizationDate;
    }

    protected void setSynchronizationDate(Date synchronizationDate) {
        this.synchronizationDate = synchronizationDate;
    }

    public ParentPostContainer getParent() {
        return parent;
    }

    public void setParent(ParentPostContainer parent) {
        this.parent = parent;
        notifyListener();
    }

    @Override
    public void createFromData(IDatabaseTable data) {
        ChildPostContainerTable childPostContainerData = (ChildPostContainerTable)data;
        this.setInternalID(childPostContainerData.id);
        this.setExternalID(childPostContainerData.externalID);
        this.setLocked(childPostContainerData.locked);
        this.setSynchronizationDate(childPostContainerData.synchronizationDate);

        final ChildPostContainer instance = this;

        if (!locked) {
            final LiveData<Post> postLiveData = PostRepository.getInstance().getDataByID(childPostContainerData.postID);
            postLiveData.observeForever(new Observer<Post>() {
                @Override
                public void onChanged(@Nullable Post post) {
                    if (post != null) {
                        instance.setPost(post);
                        notifyListener();
                        postLiveData.removeObserver(this);
                    }
                }
            });
        }
        final LiveData<Account> accountLiveData = AccountRepository.getInstance().getDataByID(childPostContainerData.accountID);
        accountLiveData.observeForever(new Observer<Account>() {
            @Override
            public void onChanged(@Nullable Account account) {
                if (account != null) {
                    instance.setAccount(account);
                    notifyListener();
                    accountLiveData.removeObserver(this);
                }
            }
        });
    }

    @Override
    public void saveInDatabase() {
        if (internalID != null)
            return;
        if (!locked)
            post.saveInDatabase();
        ChildPostContainerRepository repository = ChildPostContainerRepository.getInstance();
        internalID = repository.insert(this);
    }

    @Override
    public void updateInDatabase() {
        if (internalID == null)
            return;
        if (!locked)
            post.updateInDatabase();
        ChildPostContainerRepository repository = ChildPostContainerRepository.getInstance();
        repository.update(this);
    }

    @Override
    public void deleteFromDatabase() {
        if (internalID == null)
            return;
        ChildPostContainerRepository repository = ChildPostContainerRepository.getInstance();
        repository.delete(this);
        if (!locked)
            post.deleteFromDatabase();
        internalID = null;
    }

    @Override
    public boolean isPublished() {
        return (externalID != null);
    }

    private void setLocked(boolean locked) {
        this.locked = locked;
    }
}
