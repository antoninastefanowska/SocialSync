package com.antonina.socialsynchro.content.attachments;

import com.antonina.socialsynchro.database.tables.ITable;

public class VideoAttachment extends Attachment {
    private int height, width, lengthSeconds;

    public VideoAttachment(ITable data) {
        super(data);
        // TODO: Wydobyć pozostałe informacje z pliku - dotyczy też pozostałych załączników
    }

    public int getHeight() { return height; }

    public int getWidth() { return width; }

    public int getLengthSeconds() {
        return lengthSeconds;
    }
}
