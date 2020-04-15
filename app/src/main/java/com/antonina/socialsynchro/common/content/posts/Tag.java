package com.antonina.socialsynchro.common.content.posts;

import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;

public class Tag implements IDatabaseEntity {
    private Long internalID;
    private String content;

    public Tag(String content) {
        this.content = correctContent(content);
    }

    private String correctContent(String content) {
        return content.replaceAll("[^a-zA-Z0-9]", "");
    }

    public String getProperContent() {
        return '#' + content;
    }

    public String getContent() {
        return content;
    }

    public int getLength() {
        return content.length() + 1;
    }

    @Override
    public void createFromDatabaseRow(IDatabaseRow data) {

    }

    @Override
    public Long getInternalID() {
        return internalID;
    }

    @Override
    public void saveInDatabase() {

    }

    @Override
    public void updateInDatabase() {

    }

    @Override
    public void deleteFromDatabase() {

    }
}
