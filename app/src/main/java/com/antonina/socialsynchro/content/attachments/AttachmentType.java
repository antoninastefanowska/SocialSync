package com.antonina.socialsynchro.content.attachments;

import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.tables.AttachmentTypeTable;
import com.antonina.socialsynchro.database.tables.ITable;

public class AttachmentType implements IDatabaseEntity {
    private long id;
    private String name;
    private String iconUrl;

    public AttachmentType(ITable data) { createFromData(data); }

    @Override
    public long getID() {
        return 0;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getIconUrl() { return iconUrl; }

    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    @Override
    public void createFromData(ITable data) {
        AttachmentTypeTable attachmentTypeData = (AttachmentTypeTable)data;
        this.id = attachmentTypeData.id;
        this.iconUrl = attachmentTypeData.iconUrl;
    }
}
