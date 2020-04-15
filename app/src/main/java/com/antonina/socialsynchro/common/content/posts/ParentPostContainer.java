package com.antonina.socialsynchro.common.content.posts;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.databinding.Bindable;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.common.content.statistics.ParentStatistic;
import com.antonina.socialsynchro.common.content.attachments.Attachment;
import com.antonina.socialsynchro.common.database.repositories.ChildPostContainerRepository;
import com.antonina.socialsynchro.common.database.repositories.ParentPostContainerRepository;
import com.antonina.socialsynchro.common.database.repositories.PostRepository;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.database.rows.ParentPostContainerRow;
import com.antonina.socialsynchro.common.gui.listeners.OnAttachmentUploadedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnPublishedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnUnpublishedListener;
import com.antonina.socialsynchro.common.rest.IServiceEntity;

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
        setPost(new Post());
        children = new ArrayList<>();
        deletedChildren = new ArrayList<>();
    }

    public ParentPostContainer(IDatabaseRow data) {
        createFromDatabaseRow(data);
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
        notifyGUI();
        notifyGUIChildren();
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
        notifyGUI();
        notifyGUIChildren();
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
        notifyGUI();
        notifyGUIChildren();
    }

    @Override
    public void addAttachment(Attachment attachment) {
        post.addAttachment(attachment);
        for (ChildPostContainer child : getChildren()) {
            //TODO: Usunąć automatyczne odblokowywanie
            if (!child.validateAttachment(attachment))
                child.unlock(false);
        }
        notifyGUI();
        notifyGUIChildren();
    }

    @Override
    public void removeAttachment(Attachment attachment) {
        post.removeAttachment(attachment);
        notifyGUI();
        notifyGUIChildren();
    }

    @Override
    public List<Tag> getTags() {
        return post.getTags();
    }

    @Override
    public void setTags(List<Tag> tags) {
        post.setTags(tags);
        notifyGUI();
        notifyGUIChildren();
    }

    @Override
    public void addTag(Tag tag) {
        post.addTag(tag);
        notifyGUI();
        notifyGUIChildren();
    }

    @Override
    public void removeTag(Tag tag) {
        post.removeTag(tag);
        notifyGUI();
        notifyGUIChildren();
    }

    @Override
    public void publish(final OnPublishedListener publishListener, OnAttachmentUploadedListener attachmentListener) {
        publishedChildren = 0;
        setLoading(true);
        OnPublishedListener parentListener = new OnPublishedListener() {
            @Override
            public void onPublished(ChildPostContainer publishedPost) {
                publishedChildren++;
                if (finishedPublishing()) {
                    setLoading(false);
                    saveInDatabase();
                }
                publishListener.onPublished(publishedPost);
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

    public boolean finishedPublishing() {
        return publishedChildren >= children.size();
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
                if (synchronizedChildren >= children.size()) {
                    setLoading(false);
                    saveInDatabase();
                }
            }

            @Override
            public void onError(IServiceEntity entity, String error) {
                setLoading(false);
                listener.onError(entity, error);
            }
        };
        for (ChildPostContainer child : children)
            child.synchronize(parentListener);
    }

    @Override
    public void unpublish(final OnUnpublishedListener listener) {
        unpublishedChildren = 0;
        setLoading(true);
        notifyGUI();
        OnUnpublishedListener parentListener = new OnUnpublishedListener() {
            @Override
            public void onUnpublished(ChildPostContainer unpublishedPost) {
                unpublishedChildren++;
                listener.onUnpublished(unpublishedPost);
                if (unpublishedChildren >= children.size()) {
                    setLoading(false);
                    notifyGUI();
                }
            }

            @Override
            public void onError(ChildPostContainer post, String error) {
                setLoading(false);
                listener.onError(post, error);
                notifyGUI();
            }
        };
        for (ChildPostContainer child : children)
            child.unpublish(parentListener);
    }

    public void addChild(ChildPostContainer child) {
        child.setParent(this);
        children.add(child);
        notifyGUI();
    }

    public void removeChild(ChildPostContainer child) {
        children.remove(child);
        child.setParent(null);
        if (child.getInternalID() != null)
            deletedChildren.add(child);
        notifyGUI();
    }

    @Override
    public void createFromDatabaseRow(IDatabaseRow data) {
        ParentPostContainerRow parentPostContainerData = (ParentPostContainerRow) data;
        this.internalID = parentPostContainerData.getID();

        this.children = new ArrayList<>();
        this.deletedChildren = new ArrayList<>();
        setPost(new Post());

        final ParentPostContainer instance = this;
        final LiveData<Post> postLiveData = PostRepository.getInstance().getDataByID(parentPostContainerData.postID);
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
            updateInDatabase();
        else {
            post.saveInDatabase();
            ParentPostContainerRepository repository = ParentPostContainerRepository.getInstance();
            internalID = repository.insert(this);
            for (ChildPostContainer child : children)
                child.saveInDatabase();
        }
    }

    @Override
    public void updateInDatabase() {
        post.saveInDatabase();
        ParentPostContainerRepository repository = ParentPostContainerRepository.getInstance();
        repository.update(this);
        for (ChildPostContainer deletedChild : deletedChildren)
            deletedChild.deleteFromDatabase();

        deletedChildren.clear();

        for (ChildPostContainer child : children)
            child.saveInDatabase();
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

    private void notifyGUIChildren() {
        for (ChildPostContainer child : children) {
            if (child.isLocked())
                child.notifyGUI();
        }
    }

    @Override
    public boolean isParent() {
        return true;
    }

    @Override
    public ParentStatistic getStatistic() {
        ParentStatistic parentStatistic = new ParentStatistic();
        for (ChildPostContainer child : children)
            parentStatistic.addChildGroup(child.getStatistic());
        return parentStatistic;
    }
}
