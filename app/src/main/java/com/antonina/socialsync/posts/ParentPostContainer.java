package com.antonina.socialsync.posts;

import com.antonina.socialsync.posts.attachments.IAttachment;

import java.util.List;

public class ParentPostContainer implements IPostContainer, IPost {
    private List<ChildPostContainer> children;
    private IPost post;

    public ParentPostContainer() {
        post = new Post();
    }

    @Override
    public IPost getPost() {
        return post;
    }

    @Override
    public String getTitle() {
        return post.getTitle();
    }

    @Override
    public void setTitle(String title) {
        post.setTitle(title);
        for (ChildPostContainer child : children) {
            if (child.isLocked())
                child.setTitle(title);
        }
    }

    @Override
    public String getContent() {
        return post.getContent();
    }

    @Override
    public void setContent(String content) {
        post.setContent(content);
        for (ChildPostContainer child : children) {
            if (child.isLocked())
                child.setContent(content);
        }
    }

    @Override
    public List<IAttachment> getAttachments() {
        return post.getAttachments();
    }

    @Override
    public void addAttachment(IAttachment attachment) {
        post.addAttachment(attachment);
        for (ChildPostContainer child : children) {
            if (child.isLocked())
                child.addAttachment(attachment);
        }
    }

    @Override
    public void removeAttachment(IAttachment attachment) {
        post.removeAttachment(attachment);
        for (ChildPostContainer child : children) {
            if (child.isLocked())
                child.removeAttachment(attachment);
        }
    }
}
