package com.antonina.socialsync.posts;

import com.antonina.socialsync.accounts.IAccount;
import com.antonina.socialsync.posts.attachments.IAttachment;

import java.util.List;

public abstract class ChildPostContainer implements IPostContainer, IPost {
    private IPost post;
    private boolean locked;
    private ParentPostContainer parent;
    private IAccount account;

    // TODO: dla każdej funkcji modyfikującej sprawdzić ograniczenia

    public ChildPostContainer(IAccount account) {
        this.account = account;
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
            return parent.getPost().getTitle();
        else
            return post.getTitle();
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
}
