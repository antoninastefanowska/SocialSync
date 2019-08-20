package com.antonina.socialsynchro.content;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.SocialSynchro;
import com.antonina.socialsynchro.content.attachments.Attachment;
import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.tables.ITable;
import com.antonina.socialsynchro.database.tables.ParentPostContainerTable;
import com.antonina.socialsynchro.database.viewmodels.PostViewModel;
import com.antonina.socialsynchro.gui.SelectableItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParentPostContainer extends SelectableItem implements IPostContainer, IPost, IDatabaseEntity {
    private long id;
    private List<ChildPostContainer> children;
    private Post post;
    private Date creationDate;

    public ParentPostContainer() {
        post = new Post();
        children = new ArrayList<ChildPostContainer>();
    }

    public ParentPostContainer(ITable data) {
        createFromData(data);
    }

    @Override
    public Post getPost() {
        return post;
    }

    @Override
    public String getTitle() {
        return post.getTitle();
    }

    @Override
    public void setTitle(String title) {
        post.setTitle(title);
    }

    @Override
    public String getContent() {
        return post.getContent();
    }

    @Override
    public void setContent(String content) {
        post.setContent(content);
    }

    @Override
    public List<Attachment> getAttachments() {
        return post.getAttachments();
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
    public void publish() {
        for (ChildPostContainer child : children) {
            child.publish();
        }
    }

    @Override
    public void remove() {
        for (ChildPostContainer child : children) {
            child.remove();
        }
        // TODO: Usuniecie z aplikacji
    }

    public void addChild(ChildPostContainer child) {
        children.add(child);
    }

    public void removeChild(ChildPostContainer child) {
        children.remove(child);
    }

    public Date getCreationDate() { return creationDate; }

    @Override
    public void createFromData(ITable data) {
        ParentPostContainerTable parentPostContainerData = (ParentPostContainerTable)data;
        this.id = parentPostContainerData.id;
        this.creationDate = parentPostContainerData.creationDate;

        final ParentPostContainer instance = this;
        final LiveData<Post> postLiveData = PostViewModel.getInstance(SocialSynchro.getInstance()).getEntityByID(parentPostContainerData.postID);
        postLiveData.observeForever(new Observer<Post>() {
            @Override
            public void onChanged(@Nullable Post post) {
                instance.post = post;
                postLiveData.removeObserver(this);
            }
        });
    }

    @Override
    public long getID() {
        return id;
    }
}
