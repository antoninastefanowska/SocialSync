package com.antonina.socialsynchro.content;

import com.antonina.socialsynchro.content.attachments.IAttachment;

import java.util.Date;
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
