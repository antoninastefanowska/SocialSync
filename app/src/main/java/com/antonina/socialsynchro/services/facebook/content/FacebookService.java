package com.antonina.socialsynchro.services.facebook.content;

import android.support.v7.app.AppCompatActivity;

import com.antonina.socialsynchro.common.content.services.Service;
import com.antonina.socialsynchro.common.content.services.ServiceID;
import com.antonina.socialsynchro.services.facebook.gui.FacebookLoginActivity;

public class FacebookService extends Service {
    private static FacebookService instance;

    private FacebookService() { }

    public static FacebookService getInstance() {
        if (instance == null)
            instance = new FacebookService();
        return instance;
    }

    @Override
    public ServiceID getID() {
        return ServiceID.Facebook;
    }

    @Override
    public String getName() {
        return "Facebook";
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
        return FacebookLoginActivity.class;
    }
}
