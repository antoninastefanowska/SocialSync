package com.antonina.socialsynchro.services.twitter.content;

import android.graphics.drawable.Drawable;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.SocialSynchro;
import com.antonina.socialsynchro.common.content.accounts.LoginFlow;
import com.antonina.socialsynchro.common.gui.activities.LoginActivity;
import com.antonina.socialsynchro.common.content.services.Service;
import com.antonina.socialsynchro.common.content.services.ServiceID;

public class TwitterService extends Service {
    private static TwitterService instance;

    private TwitterService() { }

    public static TwitterService getInstance() {
        if (instance == null)
            instance = new TwitterService();
        return instance;
    }

    @Override
    public ServiceID getID() {
        return ServiceID.Twitter;
    }

    @Override
    public String getName() {
        return "Twitter";
    }

    @Override
    public int getIconID() {
        return R.drawable.twitter_icon;
    }

    @Override
    public Drawable getBanner() {
        return SocialSynchro.getInstance().getResources().getDrawable(R.drawable.twitter_banner);
    }

    @Override
    public int getColor() {
        return SocialSynchro.getInstance().getResources().getColor(R.color.colorTwitter);
    }

    @Override
    public int getFontColor() {
        return SocialSynchro.getInstance().getResources().getColor(R.color.colorFontTwitter);
    }

    @Override
    public Drawable getPanelBackground() {
        return SocialSynchro.getInstance().getResources().getDrawable(R.drawable.background_twitter_panel);
    }

    @Override
    public LoginFlow createLoginFlow(LoginActivity context) {
        return new TwitterLoginFlow(context);
    }
}
