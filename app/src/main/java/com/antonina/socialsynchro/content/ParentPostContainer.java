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
import com.antonina.socialsynchro.gui.GUIItem;
import com.antonina.socialsynchro.gui.listeners.OnPublishedListener;
import com.antonina.socialsynchro.gui.listeners.OnUnpublishedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParentPostContainer extends PostContainer {
    private List<ChildPostContainer> children;
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
        if (listener != null)
            listener.onUpdated();
    }

    @Override
    public void removeAttachment(Attachment attachment) {
        post.removeAttachment(attachment);
        if (listener != null)
            listener.onUpdated();
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
        if (listener != null)
            listener.onUpdated();
    }

    public void removeChild(ChildPostContainer child) {
        children.remove(child);
        child.setParent(null);
        if (listener != null)
            listener.onUpdated();
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
        LiveData<Post> postLiveData = PostRepository.getInstance().getDataByID(parentPostContainerData.postID);
        postLiveData.observeForever(new Observer<Post>() {
            @Override
            public void onChanged(@Nullable Post post) {
                if (post != null) {
                    instance.post = post;
                    if (listener != null)
                        listener.onUpdated();
                }
            }
        });
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
            child.updateInDatabase();
        //TODO: Sprawdzić czy dziecko istnieje w bazie i dodać/usunąć
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
