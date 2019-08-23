package com.antonina.socialsynchro.content;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.SocialSynchro;
import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.content.attachments.Attachment;
import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.repositories.AccountRepository;
import com.antonina.socialsynchro.database.repositories.ChildPostContainerRepository;
import com.antonina.socialsynchro.database.repositories.ParentPostContainerRepository;
import com.antonina.socialsynchro.database.repositories.PostRepository;
import com.antonina.socialsynchro.database.tables.ChildPostContainerTable;
import com.antonina.socialsynchro.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.gui.SelectableItem;
import com.antonina.socialsynchro.services.IResponse;
import com.antonina.socialsynchro.services.IServiceEntity;

import java.util.Date;
import java.util.List;

public abstract class ChildPostContainer extends SelectableItem implements IPostContainer, IPost, IDatabaseEntity, IServiceEntity {
    private long internalID;
    private String externalID;
    private Post post;
    private boolean locked;
    private Date synchronizationDate;
    private Account account;

    protected ParentPostContainer parent;

    // TODO: dla każdej funkcji modyfikującej sprawdzić ograniczenia
    // TODO: Zapamiętać: z bazy wczytane muszą być wpierw rodzice

    public ChildPostContainer(IDatabaseTable data) {
        createFromData(data);
    }

    public ChildPostContainer(ParentPostContainer parent, Account account) {
        this.account = account;
        this.parent = parent;
        parent.addChild(this);
        lock();
    }

    @Override
    public long getInternalID() { return internalID; }

    @Override
    public String getTitle() {
        if (locked)
            return parent.getPost().getTitle();
        else
            return post.getTitle();
    }

    @Override
    public void setTitle(String title) {
        if (!locked)
            post.setTitle(title);
    }

    @Override
    public String getContent() {
        if (locked)
            return parent.getContent();
        else
            return post.getContent();
    }

    @Override
    public void setContent(String description) {
        if (!locked)
            post.setContent(description);
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
        if (!locked)
            post.addAttachment(attachment);
    }

    @Override
    public void removeAttachment(Attachment attachment) {
        if (!locked)
            post.removeAttachment(attachment);
    }

    @Override
    public Post getPost() {
        if (locked)
            return parent.getPost();
        else
            return post;
    }

    @Override
    public abstract void publish();

    @Override
    public abstract void remove();

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

    public void setAccount(Account account) { this.account = account; }

    @Override
    public String getExternalID() { return externalID; }

    @Override
    public void setExternalID(String externalID) { this.externalID = externalID; }

    public Date getSynchronizationDate() { return synchronizationDate; }

    public void setSynchronizationDate(Date date) { this.synchronizationDate = synchronizationDate; }

    public ParentPostContainer getParent() { return parent; }

    public void setParent(ParentPostContainer parent) {
        this.parent = parent;
        parent.addChild(this);
    }

    public void removeParent() {
        parent.removeChild(this);
        this.parent = null;
    }

    @Override
    public void createFromData(IDatabaseTable data) {
        ChildPostContainerTable childPostContainerData = (ChildPostContainerTable)data;
        this.internalID = childPostContainerData.id;
        this.externalID = childPostContainerData.externalID;
        this.locked = childPostContainerData.locked;
        this.synchronizationDate = childPostContainerData.synchronizationDate;

        final ChildPostContainer instance = this;

        final LiveData<ParentPostContainer> parentPostContainerLiveData = ParentPostContainerRepository.getInstance(SocialSynchro.getInstance()).getDataByID(childPostContainerData.parentID);
        parentPostContainerLiveData.observeForever(new Observer<ParentPostContainer>() {
            @Override
            public void onChanged(@Nullable ParentPostContainer parentPostContainer) {
                instance.setParent(parentPostContainer);
                parentPostContainerLiveData.removeObserver(this);
            }
        });
        final LiveData<Post> postLiveData = PostRepository.getInstance(SocialSynchro.getInstance()).getDataByID(childPostContainerData.postID);
        postLiveData.observeForever(new Observer<Post>() {
            @Override
            public void onChanged(@Nullable Post post) {
                instance.post = post;
                postLiveData.removeObserver(this);
            }
        });
        final LiveData<Account> accountLiveData = AccountRepository.getInstance(SocialSynchro.getInstance()).getDataByID(childPostContainerData.accountID);
        accountLiveData.observeForever(new Observer<Account>() {
            @Override
            public void onChanged(@Nullable Account account) {
                instance.account = account;
                accountLiveData.removeObserver(this);
            }
        });
    }

    @Override
    public void createFromResponse(IResponse response) {
        // TODO
    }

    @Override
    public void saveInDatabase() {
        ChildPostContainerRepository repository = ChildPostContainerRepository.getInstance(SocialSynchro.getInstance());
        internalID = repository.insert(this);
    }

    @Override
    public void updateInDatabase() {
        ChildPostContainerRepository repository = ChildPostContainerRepository.getInstance(SocialSynchro.getInstance());
        repository.update(this);
    }

    @Override
    public void deleteFromDatabase() {
        ChildPostContainerRepository repository = ChildPostContainerRepository.getInstance(SocialSynchro.getInstance());
        repository.delete(this);
    }
}
