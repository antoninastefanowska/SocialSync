package com.antonina.socialsynchro.content;

import com.antonina.socialsynchro.base.IAccount;

public class FacebookPostContainer extends ChildPostContainer {
    private static final int MAX_CONTENT_LENGTH = 63206;

    public FacebookPostContainer(IAccount account) {
        super(account);
    }

    @Override
    public String getTitle() {
        return "";
    }

    @Override
    public void setTitle(String title) {
        return;
    }

    @Override
    public void setContent(String content) {
        if (content.length() > MAX_CONTENT_LENGTH) {
            unlock();
            content = content.substring(0, content.length() - 3) + "...";
        }
        super.setContent(content);
    }

    @Override
    public void publish() {

    }

    @Override
    public void remove() {

    }
}
