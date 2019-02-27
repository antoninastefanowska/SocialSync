package com.antonina.socialsync.posts;

import com.antonina.socialsync.accounts.IAccount;

import java.util.List;

public class FacebookPostContainer extends ChildPostContainer {
    private static final int MAX_CONTENT_SIZE = 63206;

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
        if (content.length() > MAX_CONTENT_SIZE) {
            unlock();
            content = content.substring(0, content.length() - 3) + "...";
        }
        super.setContent(content);
    }
}
