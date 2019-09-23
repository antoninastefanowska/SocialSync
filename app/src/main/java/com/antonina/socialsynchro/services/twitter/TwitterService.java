package com.antonina.socialsynchro.services.twitter;

import android.support.v7.app.AppCompatActivity;

import com.antonina.socialsynchro.gui.activities.TwitterLoginActivity;
import com.antonina.socialsynchro.services.Service;
import com.antonina.socialsynchro.services.ServiceID;

public class TwitterService extends Service {
    private static TwitterService instance;

    public static TwitterService getInstance() {
        if (instance == null)
            instance = new TwitterService();
        return instance;
    }

    private TwitterService() { }

    @Override
    public ServiceID getID() {
        return ServiceID.Twitter;
    }

    @Override
    public String getName() {
        return "Twitter";
    }

    @Override
    public String getLogoUrl() {
        return null;
    }

    @Override
    public String getColorName() {
        return null;
    }

    @Override
    public Class<? extends AppCompatActivity> getLoginActivityClass() {
        return TwitterLoginActivity.class;
    }
}
