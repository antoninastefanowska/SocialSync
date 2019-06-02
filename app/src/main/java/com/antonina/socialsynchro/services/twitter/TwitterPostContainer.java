package com.antonina.socialsynchro.services.twitter;

import com.antonina.socialsynchro.base.IAccount;
import com.antonina.socialsynchro.content.ChildPostContainer;
import com.antonina.socialsynchro.content.ParentPostContainer;

public class TwitterPostContainer extends ChildPostContainer {
    private static final int MAX_CONTENT_LENGTH = 140;

    public TwitterPostContainer(IAccount account) {
        super(account);
    }

    public TwitterPostContainer(ParentPostContainer parent) {
        super(parent);
        lock();
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
    public void lock() {
        String content = parent.getContent();
        super.lock();
        if (content.length() > MAX_CONTENT_LENGTH) {
            unlock();
            content = content.substring(0, content.length() - 3) + "...";
            super.setContent(content);
        }
    }

    @Override
    public void publish() {
        TwitterController twitterController = TwitterController.getInstance();
        twitterController.requestPost(this, getAccount());
    }

    @Override
    public void remove() {
        TwitterController twitterController = TwitterController.getInstance();
        twitterController.requestRemove(this, getAccount());
    }
}
