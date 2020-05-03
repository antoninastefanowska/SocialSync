package com.antonina.socialsynchro.common.model.posts;

import com.antonina.socialsynchro.common.model.attachments.Attachment;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface IPost extends Serializable {
    String getTitle();
    void setTitle(String title);
    String getContent();
    void setContent(String content);
    Date getCreationDate();
    Date getModificationDate();

    List<Attachment> getAttachments();
    void setAttachments(List<Attachment> attachments);
    void addAttachment(Attachment attachment);
    void removeAttachment(Attachment attachment);

    List<Tag> getTags();
    void setTags(List<Tag> tags);
    void addTag(Tag tag);
    void removeTag(Tag tag);
}
