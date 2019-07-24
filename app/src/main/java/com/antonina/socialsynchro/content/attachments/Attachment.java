package com.antonina.socialsynchro.content.attachments;

import com.antonina.socialsynchro.content.Post;
import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.tables.AttachmentTable;
import com.antonina.socialsynchro.database.tables.ITable;

public abstract class Attachment implements IDatabaseEntity {
    private long id;
    private String filename;
    private int sizeKb;
    private AttachmentType attachmentType;
    private Post parentPost;

    @Override
    public long getID() { return id; }

    public String getFilename() { return filename; }

    public void setFilename(String filename) { this.filename = filename; }

    public int getSizeKb() { return sizeKb; }

    public void setSizeKb(int sizeKb) { this.sizeKb = sizeKb; }

    public AttachmentType getAttachmentType() { return attachmentType; }

    public void setAttachmentType(AttachmentType attachmentType) { this.attachmentType = attachmentType; }

    public Post getParentPost() { return parentPost; }

    public void setParentPost(Post parentPost) { this.parentPost = parentPost; }

    public Attachment(ITable data) {
        createFromData(data);
    }

    @Override
    public void createFromData(ITable data) {
        AttachmentTable attachmentData = (AttachmentTable)data;
        this.id = attachmentData.id;
        this.filename = attachmentData.filename;
        this.sizeKb = attachmentData.sizeKb;

        // TODO: Wydobyć attachmentType z bazy.
    }
}
