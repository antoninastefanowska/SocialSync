package com.antonina.socialsynchro.content;

import com.antonina.socialsynchro.content.attachments.Attachment;

import java.util.List;

public interface IPost {
    String getTitle();
    void setTitle(String title);
    String getContent();
    void setContent(String content);
    List<Attachment> getAttachments();
    void addAttachment(Attachment attachment);
    void removeAttachment(Attachment attachment);
}
