package com.antonina.socialsynchro.content;

import com.antonina.socialsynchro.base.IAccount;
import com.antonina.socialsynchro.content.attachments.IAttachment;

import java.util.List;

public abstract class ChildPostContainer implements IPostContainer, IPost {
    private String serviceID;
    private IPost post;
    private boolean locked;
    private IAccount account;

    protected ParentPostContainer parent;

    // TODO: dla każdej funkcji modyfikującej sprawdzić ograniczenia

    public ChildPostContainer(IAccount account) {
        this.account = account;
    }

    public ChildPostContainer(ParentPostContainer parent) {
        this.parent = parent;
        parent.addChild(this);
        lock();
    }

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
    public List<IAttachment> getAttachments() {
        if (locked)
            return parent.getPost().getAttachments();
        else
            return post.getAttachments();
    }

    @Override
    public void addAttachment(IAttachment attachment) {
        if (!locked)
            post.addAttachment(attachment);
    }

    @Override
    public void removeAttachment(IAttachment attachment) {
        if (!locked)
            post.removeAttachment(attachment);
    }

    @Override
    public IPost getPost() {
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
        for (IAttachment attachment : parentPost.getAttachments()) {
            addAttachment(attachment);
        }
        // kopia głęboka postu z parenta
    }

    public IAccount getAccount() {
        return account;
    }

    public void setAccount(IAccount account) { this.account = account; }

    public String getServiceID() { return serviceID; }

    public void setServiceID(String serviceID) { this.serviceID = serviceID; }
}
