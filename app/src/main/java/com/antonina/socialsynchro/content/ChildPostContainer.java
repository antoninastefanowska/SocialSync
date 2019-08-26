package com.antonina.socialsynchro.content;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.databinding.Bindable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.antonina.socialsynchro.BR;
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
    private boolean loading;

    protected ParentPostContainer parent;

    // TODO: dla każdej funkcji modyfikującej sprawdzić ograniczenia
    // TODO: Zapamiętać: z bazy wczytane muszą być wpierw rodzice

    public ChildPostContainer(IDatabaseTable data) {
        createFromData(data);
    }

    public ChildPostContainer(ParentPostContainer parent, Account account) {
        locked = true;
        post = null;
        setParent(parent);
        setAccount(account);
    }

    @Override
    public long getInternalID() { return internalID; }

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
        notifyPropertyChanged(BR.child);
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
        notifyPropertyChanged(BR.child);
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
    public abstract void publish(OnPublishedListener listener);

    @Override
    public abstract void remove();

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
        Log.d("rodzic", "załadowano dziecko nr. " + String.valueOf(this.internalID));

        final ChildPostContainer instance = this;

        final LiveData<ParentPostContainer> parentPostContainerLiveData = ParentPostContainerRepository.getInstance().getDataByID(childPostContainerData.parentID);
        parentPostContainerLiveData.observeForever(new Observer<ParentPostContainer>() {
            @Override
            public void onChanged(@Nullable ParentPostContainer parentPostContainer) {
                instance.setParent(parentPostContainer);
                //Log.d("rodzic", "tu jestem");
                //instance.notifyPropertyChanged(BR.parent);
                //instance.notifyPropertyChanged(BR.child);
                parentPostContainerLiveData.removeObserver(this);
            }
        });
        if (!locked) {
            final LiveData<Post> postLiveData = PostRepository.getInstance().getDataByID(childPostContainerData.postID);
            postLiveData.observeForever(new Observer<Post>() {
                @Override
                public void onChanged(@Nullable Post post) {
                    instance.post = post;
                    //instance.notifyPropertyChanged(BR.parent);
                    //instance.notifyPropertyChanged(BR.child);
                    //instance.notifyPropertyChanged(BR.content);
                    postLiveData.removeObserver(this);
                }
            });
        }
        final LiveData<Account> accountLiveData = AccountRepository.getInstance().getDataByID(childPostContainerData.accountID);
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
        if (!locked)
            post.saveInDatabase();
        ChildPostContainerRepository repository = ChildPostContainerRepository.getInstance();
        internalID = repository.insert(this);
    }

    @Override
    public void updateInDatabase() {
        if (!locked)
            post.updateInDatabase();
        ChildPostContainerRepository repository = ChildPostContainerRepository.getInstance();
        repository.update(this);
    }

    @Override
    public void deleteFromDatabase() {
        ChildPostContainerRepository repository = ChildPostContainerRepository.getInstance();
        repository.delete(this);
        if (!locked)
            post.deleteFromDatabase();
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    @Override
    public boolean isPublished() {
        return (externalID != null);
    }
}
