package com.antonina.socialsynchro.content;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.databinding.Bindable;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.content.attachments.Attachment;
import com.antonina.socialsynchro.database.repositories.ChildPostContainerRepository;
import com.antonina.socialsynchro.database.repositories.ParentPostContainerRepository;
import com.antonina.socialsynchro.database.repositories.PostRepository;
import com.antonina.socialsynchro.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.database.tables.ParentPostContainerTable;
import com.antonina.socialsynchro.gui.listeners.OnAttachmentUploadedListener;
import com.antonina.socialsynchro.gui.listeners.OnPublishedListener;
import com.antonina.socialsynchro.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.gui.listeners.OnUnpublishedListener;
import com.antonina.socialsynchro.services.IServiceEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParentPostContainer extends PostContainer {
    private List<ChildPostContainer> children;
    private List<ChildPostContainer> deletedChildren;

    private int publishedChildren;
    private int unpublishedChildren;
    private int synchronizedChildren;

    public ParentPostContainer() {
        post = new Post();
        children = new ArrayList<>();
        deletedChildren = new ArrayList<>();
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
        notifyListener();
        notifyChildrenListeners();
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
        notifyListener();
        notifyChildrenListeners();
    }

    @Bindable
    @Override
    public Date getCreationDate() {
        return post.getCreationDate();
    }

    @Bindable
    @Override
    public Date getModificationDate() {
        return post.getModificationDate();
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
        notifyListener();
        notifyChildrenListeners();
    }

    @Override
    public void addAttachment(Attachment attachment) {
        post.addAttachment(attachment);
        notifyListener();
        notifyChildrenListeners();
    }

    @Override
    public void removeAttachment(Attachment attachment) {
        post.removeAttachment(attachment);
        notifyListener();
        notifyChildrenListeners();
    }

    @Override
    public void publish(final OnPublishedListener publishListener, OnAttachmentUploadedListener attachmentListener) {
        publishedChildren = 0;
        setLoading(true);
        OnPublishedListener parentListener = new OnPublishedListener() {
            @Override
            public void onPublished(ChildPostContainer publishedPost) {
                publishedChildren++;
                publishListener.onPublished(publishedPost);
                if (publishedChildren >= children.size())
                    setLoading(false);
            }

            @Override
            public void onError(ChildPostContainer post, String error) {
                setLoading(false);
                publishListener.onError(post, error);
            }
        };
        for (ChildPostContainer child : children) {
            child.publish(parentListener, attachmentListener);
        }
    }

    @Override
    public boolean isPublished() {
        return false;
    }

    @Override
    public void synchronize(final OnSynchronizedListener listener) {
        synchronizedChildren = 0;
        setLoading(true);
        OnSynchronizedListener parentListener = new OnSynchronizedListener() {
            @Override
            public void onSynchronized(IServiceEntity entity) {
                synchronizedChildren++;
                listener.onSynchronized(entity);
                if (synchronizedChildren >= children.size())
                    setLoading(false);
            }

            @Override
            public void onError(IServiceEntity entity, String error) {
                setLoading(false);
                listener.onError(entity, error);
            }
        };
        for (ChildPostContainer child : children) {
            child.synchronize(parentListener);
        }
    }

    @Override
    public void unpublish(final OnUnpublishedListener listener) {
        unpublishedChildren = 0;
        setLoading(true);
        OnUnpublishedListener parentListener = new OnUnpublishedListener() {
            @Override
            public void onUnpublished(ChildPostContainer unpublishedPost) {
                unpublishedChildren++;
                listener.onUnpublished(unpublishedPost);
                if (unpublishedChildren >= children.size())
                    setLoading(false);
            }

            @Override
            public void onError(ChildPostContainer post, String error) {
                setLoading(false);
                listener.onError(post, error);
            }
        };
        for (ChildPostContainer child : children) {
            child.unpublish(parentListener);
        }
    }

    public void addChild(ChildPostContainer child) {
        child.setParent(this);
        child.lock();
        children.add(child);
        notifyListener();
    }

    public void removeChild(ChildPostContainer child) {
        children.remove(child);
        child.setParent(null);
        if (child.getInternalID() != null)
            deletedChildren.add(child);
        notifyListener();
    }

    @Override
    public void createFromData(IDatabaseTable data) {
        ParentPostContainerTable parentPostContainerData = (ParentPostContainerTable) data;
        this.internalID = parentPostContainerData.id;

        this.children = new ArrayList<>();
        this.deletedChildren = new ArrayList<>();
        this.post = new Post(); //TODO: Tworzymy pusty obiekt zanim pobierzemy obiekt z bazy - zrobić to samo dla innych encji.

        final ParentPostContainer instance = this;
        final LiveData<Post> postLiveData = PostRepository.getInstance().getDataByID(parentPostContainerData.postID);
        postLiveData.observeForever(new Observer<Post>() {
            @Override
            public void onChanged(@Nullable Post post) {
                if (post != null) {
                    instance.post = post;
                    notifyListener();
                    postLiveData.removeObserver(this);
                }
            }
        });
        final LiveData<List<ChildPostContainer>> childrenLiveData = ChildPostContainerRepository.getInstance().getDataByParent(this);
        childrenLiveData.observeForever(new Observer<List<ChildPostContainer>>() {
            @Override
            public void onChanged(@Nullable List<ChildPostContainer> children) {
                if (children != null) {
                    instance.children.clear();
                    for (ChildPostContainer child : children)
                        if (child != null)
                            instance.addChild(child);
                }
            }
        });
    }

    @Override
    public void saveInDatabase() {
        if (internalID != null)
            return;
        post.saveInDatabase();
        ParentPostContainerRepository repository = ParentPostContainerRepository.getInstance();
        internalID = repository.insert(this);
        for (ChildPostContainer child : children)
            child.saveInDatabase();
    }

    @Override
    public void updateInDatabase() {
        if (internalID == null)
            return;
        post.updateInDatabase();
        ParentPostContainerRepository repository = ParentPostContainerRepository.getInstance();
        repository.update(this);

        for (ChildPostContainer deletedChild : deletedChildren)
            deletedChild.deleteFromDatabase();

        deletedChildren.clear();

        for (ChildPostContainer child : children) {
            if (child.getInternalID() == null)
                child.saveInDatabase();
            else
                child.updateInDatabase();
        }
    }

    @Override
    public void deleteFromDatabase() {
        if (internalID == null)
            return;
        for (ChildPostContainer child : children)
            child.deleteFromDatabase();
        ParentPostContainerRepository repository = ParentPostContainerRepository.getInstance();
        repository.delete(this);
        post.deleteFromDatabase();
        internalID = null;
    }

    private void notifyChildrenListeners() {
        for (ChildPostContainer child : children) {
            if (child.isLocked())
                child.notifyListener();
        }
    }
}
