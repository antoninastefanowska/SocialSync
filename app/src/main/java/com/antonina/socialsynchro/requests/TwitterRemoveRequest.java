package com.antonina.socialsynchro.requests;

import android.util.Log;

import com.antonina.socialsynchro.posts.ChildPostContainer;
import com.antonina.socialsynchro.posts.TwitterPostContainer;

public class TwitterRemoveRequest extends TwitterRequest {
    private final String id;

    public TwitterRemoveRequest(ChildPostContainer post) {
        super();
        id = post.getServiceID();
        Log.d("wysylanie", id);
        buildUserAuthorizationHeader();
        Log.d("wysylanie", getAuthorization());
    }

    public String getID() { return id; }

    @Override
    public void collectParameters() {
        collectBaseParameters();
    }

    @Override
    protected String getUrl() {
        return "https://api.twitter.com/1.1/statuses/destroy/" + id + ".json";
    }
}
