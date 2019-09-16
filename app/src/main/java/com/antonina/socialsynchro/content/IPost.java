package com.antonina.socialsynchro.content;

import com.antonina.socialsynchro.content.attachments.Attachment;

import java.io.Serializable;
import java.util.List;

public interface IPost extends Serializable {
    String getTitle();
    void setTitle(String title);
    String getContent();
    void setContent(String content);
    List<Attachment> getAttachments();
    void setAttachments(List<Attachment> attachments);
    void addAttachment(Attachment attachment);
    void removeAttachment(Attachment attachment);
}
