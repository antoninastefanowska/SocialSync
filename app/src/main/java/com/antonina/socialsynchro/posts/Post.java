package com.antonina.socialsynchro.posts;

import com.antonina.socialsynchro.posts.attachments.IAttachment;

import java.util.ArrayList;
import java.util.List;

public class Post implements IPost {
    private String title;
    private String content;
    private List<IAttachment> attachments;

    public Post() {
        title = "";
        content = "";
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public List<IAttachment> getAttachments() {
        return attachments;
    }

    @Override
    public void addAttachment(IAttachment attachment) {
        if (attachments == null)
            attachments = new ArrayList<IAttachment>();
        attachments.add(attachment);
    }

    @Override
    public void removeAttachment(IAttachment attachment) {
        if (attachments == null || attachments.isEmpty())
            return;
        attachments.remove(attachment);
    }
}
