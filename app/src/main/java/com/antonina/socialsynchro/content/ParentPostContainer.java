package com.antonina.socialsynchro.content;

import com.antonina.socialsynchro.content.attachments.Attachment;
import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.tables.ITable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParentPostContainer implements IPostContainer, IPost, IDatabaseEntity {
    private long id;
    private List<ChildPostContainer> children;
    private Post post;
    private Date creationDate;

    public ParentPostContainer() {
        post = new Post();
        children = new ArrayList<ChildPostContainer>();
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

    public Date getCreationDate() { return creationDate; }

    @Override
    public void createFromData(ITable data) {

    }

    @Override
    public long getID() {
        return id;
    }
}
