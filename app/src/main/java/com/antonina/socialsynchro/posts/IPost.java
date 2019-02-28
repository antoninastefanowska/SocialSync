package com.antonina.socialsynchro.posts;

import com.antonina.socialsynchro.posts.attachments.IAttachment;

import java.util.List;

public interface IPost {
    String getTitle();
    void setTitle(String title);
    String getContent();
    void setContent(String content);
    List<IAttachment> getAttachments();
    void addAttachment(IAttachment attachment);
    void removeAttachment(IAttachment attachment);
}
