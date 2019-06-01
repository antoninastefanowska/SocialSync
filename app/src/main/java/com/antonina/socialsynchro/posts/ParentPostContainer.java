package com.antonina.socialsynchro.posts;

import com.antonina.socialsynchro.posts.attachments.IAttachment;

import java.util.ArrayList;
import java.util.List;

public class ParentPostContainer implements IPostContainer, IPost {
    private List<ChildPostContainer> children;
    private IPost post;

    public ParentPostContainer() {
        post = new Post();
        children = new ArrayList<ChildPostContainer>();
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
    public List<IAttachment> getAttachments() {
        return post.getAttachments();
    }

    @Override
    public void addAttachment(IAttachment attachment) {
        post.addAttachment(attachment);
    }

    @Override
    public void removeAttachment(IAttachment attachment) {
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
}
