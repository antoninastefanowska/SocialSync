package com.antonina.socialsynchro.posts;

import android.util.Log;

import com.antonina.socialsynchro.accounts.IAccount;
import com.antonina.socialsynchro.controllers.TwitterController;

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
        TwitterController twitterController = new TwitterController();
        twitterController.requestPost(this);
    }

    @Override
    public void remove() {
        TwitterController twitterController = new TwitterController();
        twitterController.requestRemove(this);
    }
}
