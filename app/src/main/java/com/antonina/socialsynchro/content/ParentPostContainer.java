package com.antonina.socialsynchro.content;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.databinding.Bindable;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.BR;
import com.antonina.socialsynchro.content.attachments.Attachment;
import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.repositories.ParentPostContainerRepository;
import com.antonina.socialsynchro.database.repositories.PostRepository;
import com.antonina.socialsynchro.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.database.tables.ParentPostContainerTable;
import com.antonina.socialsynchro.gui.SelectableItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParentPostContainer extends SelectableItem implements IPostContainer, IPost, IDatabaseEntity {
    private long internalID;
    private List<ChildPostContainer> children;
    private Post post;
    private Date creationDate;

    public ParentPostContainer() {
        post = new Post();
        children = new ArrayList<ChildPostContainer>();
    }

    public ParentPostContainer(IDatabaseTable data) {
        createFromData(data);
    }

    @Override
    public Post getPost() {
        return post;
    }

    @Bindable
    @Override
    public String getTitle() {
        return post.getTitle();
    }

    @Bindable
    @Override
    public void setTitle(String title) {
        post.setTitle(title);
    }

    @Bindable
    @Override
    public String getContent() {
        return post.getContent();
    }

    @Bindable
    @Override
    public void setContent(String content) {
        post.setContent(content);
        notifyPropertyChanged(BR.child);
    }

    public List<ChildPostContainer> getChildren() {
        return children;
    }

    @Override
    public List<Attachment> getAttachments() {
        return post.getAttachments();
    }

    @Override
    public void setAttachments(List<Attachment> attachments) {
        post.setAttachments(attachments);
    }

    @Override
    public void addAttachment(Attachment attachment) {
        post.addAttachment(attachment);
    }

    @Override
    public void removeAttachment(Attachment attachment) {
        post.removeAttachment(attachment);
    }

    @Override
    public void publish(OnPublishedListener listener) {
        for (ChildPostContainer child : children) {
            child.publish(listener);
        }
    }

    @Override
    public boolean isPublished() {
        return false;
    }

    @Override
    public void unpublish(OnUnpublishedListener listener) {
        for (ChildPostContainer child : children) {
            child.unpublish(listener);
        }
    }

    public void addChild(ChildPostContainer child) {
        children.add(child);
        child.setParent(this);
        child.lock();
        notifyPropertyChanged(BR.parent);
    }

    public void removeChild(ChildPostContainer child) {
        children.remove(child);
        child.setParent(null);
    }

    public Date getCreationDate() { return creationDate; }

    @Override
    public void createFromData(IDatabaseTable data) {
        ParentPostContainerTable parentPostContainerData = (ParentPostContainerTable)data;
        this.internalID = parentPostContainerData.id;
        this.creationDate = parentPostContainerData.creationDate;
        this.children = new ArrayList<ChildPostContainer>();
        this.post = new Post(); //TODO: Tworzymy pusty obiekt zanim pobierzemy obiekt z bazy - zrobić to samo dla innych encji.

        final ParentPostContainer instance = this;
        final LiveData<Post> postLiveData = PostRepository.getInstance().getDataByID(parentPostContainerData.postID);
        postLiveData.observeForever(new Observer<Post>() {
            @Override
            public void onChanged(@Nullable Post post) {
                instance.post = post;
                postLiveData.removeObserver(this);
            }
        });
    }

    @Override
    public long getInternalID() {
        return internalID;
    }

    @Override
    public void saveInDatabase() {
        post.saveInDatabase();
        ParentPostContainerRepository repository = ParentPostContainerRepository.getInstance();
        internalID = repository.insert(this);
        for (ChildPostContainer child : children)
            child.saveInDatabase();
    }

    @Override
    public void updateInDatabase() {
        post.updateInDatabase();
        ParentPostContainerRepository repository = ParentPostContainerRepository.getInstance();
        repository.update(this);
        for (ChildPostContainer child : children)
            child.saveInDatabase();
    }

    @Override
    public void deleteFromDatabase() {
        for (ChildPostContainer child : children)
            child.deleteFromDatabase();
        ParentPostContainerRepository repository = ParentPostContainerRepository.getInstance();
        repository.delete(this);
        post.deleteFromDatabase();
    }
}
