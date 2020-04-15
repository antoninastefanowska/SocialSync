package com.antonina.socialsynchro.common.content.posts;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.databinding.Bindable;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.common.content.services.Service;
import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.content.attachments.Attachment;
import com.antonina.socialsynchro.common.content.statistics.ChildGroupStatistic;
import com.antonina.socialsynchro.common.database.repositories.AccountRepository;
import com.antonina.socialsynchro.common.database.repositories.ChildPostContainerRepository;
import com.antonina.socialsynchro.common.database.repositories.PostRepository;
import com.antonina.socialsynchro.common.database.rows.ChildPostContainerRow;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.gui.listeners.OnAttachmentUploadedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnPublishedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnUnlockedListener;
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

    protected ChildPostContainer(IDatabaseRow data) {
        createFromDatabaseRow(data);
    }

    private transient OnUnlockedListener unlockedListener;

    protected ChildPostContainer(Account account) {
        locked = true;
        removePost();
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
        if (!validateTitle(title)) {
            unlock(false);
            title = trimText(title, getMaxTitleLength());
        }
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
        if (!validateContent(content)) {
            unlock(false);
            content = trimText(content, getMaxContentLength());
        }
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
            return parent.getAttachments();
        else
            return post.getAttachments();
    }

    @Override
    public void addAttachment(Attachment attachment) {
        if (preValidateAttachment(attachment)) {
            if (!locked) {
                post.addAttachment(attachment);
                notifyGUI();
            }
        } else
            unlock(false);
    }

    @Override
    public void removeAttachment(Attachment attachment) {
        if (!locked) {
            post.removeAttachment(attachment);
            notifyGUI();
        }
    }

    @Override
    public void setAttachments(List<Attachment> attachments) {
        if (validateAttachments(attachments)) {
            if (!locked)
                post.setAttachments(attachments);
        } else
            unlock(false);
    }

    @Override
    public List<Tag> getTags() {
        if (locked)
            return parent.getTags();
        else
            return post.getTags();
    }

    @Override
    public void setTags(List<Tag> tags) {
        if (!locked)
            post.setTags(tags);
    }

    @Override
    public void addTag(Tag tag) {
        if (!locked)
            post.addTag(tag);
    }

    @Override
    public void removeTag(Tag tag) {
        if (!locked)
            post.removeTag(tag);
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
        if (!locked && validateParent()) {
            locked = true;
            removePost();
            notifyGUI();
        }
    }

    public void unlock(boolean isManual) {
        if (locked) {
            Post parentPost = parent.getPost();
            locked = false;
            setPost(new Post());
            setTitle(parentPost.getTitle());
            setContent(parentPost.getContent());
            for (Attachment attachment : parentPost.getAttachments()) {
                Attachment copy = attachment.createCopy();
                addAttachment(copy);
            }
            if (unlockedListener != null && !isManual)
                unlockedListener.onUnlocked(this);
            notifyGUI();
        }
    }

    private void removePost() {
        post = null;
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
        if (internalID != null)
            saveInDatabase();
        notifyGUI();
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
        if (!validateParent())
            unlock(false);
        notifyGUI();
    }

    @Override
    public void createFromDatabaseRow(IDatabaseRow data) {
        ChildPostContainerRow childPostContainerData = (ChildPostContainerRow)data;
        this.setInternalID(childPostContainerData.getID());
        this.externalID = childPostContainerData.externalID;
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
                        notifyGUI();
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
                    notifyGUI();
                    accountLiveData.removeObserver(this);
                }
            }
        });
    }

    @Override
    public void saveInDatabase() {
        if (internalID != null)
            updateInDatabase();
        else {
            if (!locked)
                post.saveInDatabase();
            ChildPostContainerRepository repository = ChildPostContainerRepository.getInstance();
            internalID = repository.insert(this);
        }
    }

    @Override
    public void updateInDatabase() {
        if (!locked)
            post.saveInDatabase();
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

    private void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public boolean isParent() {
        return false;
    }

    @Override
    public abstract ChildGroupStatistic getStatistic();

    @Bindable
    public boolean isOnline() {
        return externalID != null && !externalID.isEmpty();
    }

    public abstract String getURL();

    protected abstract boolean validateTitle(String title);

    protected abstract boolean validateContent(String content);

    private boolean validateAttachments(List<Attachment> attachments) {
        for (Attachment attachment : attachments) {
            if (!validateAttachment(attachment))
                return false;
        }
        return true;
    }

    protected abstract boolean validateAttachment(Attachment attachment);

    protected abstract boolean preValidateAttachment(Attachment attachment);

    private boolean validateParent() {
        if (getParent() != null)
            return validateTitle(getParent().getTitle()) & validateContent(getParent().getContent()) & validateAttachments(getParent().getAttachments());
        else
            return true;
    }

    private String trimText(String text, int size) {
        return text.substring(0, size - 3) + "...";
    }

    protected abstract int getMaxTitleLength();

    protected abstract int getMaxContentLength();

    public void setUnlockedListener(OnUnlockedListener unlockedListener) {
        this.unlockedListener = unlockedListener;
    }
}
