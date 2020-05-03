package com.antonina.socialsynchro.services.facebook.model;

import android.graphics.drawable.Drawable;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.SocialSynchro;
import com.antonina.socialsynchro.common.model.accounts.Account;
import com.antonina.socialsynchro.common.model.accounts.LoginFlow;
import com.antonina.socialsynchro.common.model.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.model.posts.PostOptions;
import com.antonina.socialsynchro.common.model.services.Service;
import com.antonina.socialsynchro.common.model.services.ServiceID;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.gui.activities.LoginActivity;

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
    public int getIconID() {
        return R.drawable.facebook_icon;
    }

    @Override
    public Drawable getBanner() {
        return SocialSynchro.getInstance().getResources().getDrawable(R.drawable.facebook_banner);
    }

    @Override
    public int getColor() {
        return SocialSynchro.getInstance().getResources().getColor(R.color.colorFacebook);
    }

    @Override
    public int getFontColor() {
        return SocialSynchro.getInstance().getResources().getColor(R.color.colorFontFacebook);
    }

    @Override
    public Drawable getPanelBackground() {
        return SocialSynchro.getInstance().getResources().getDrawable(R.drawable.background_facebook_panel);
    }

    @Override
    public int getPanelBackgroundID() {
        return R.drawable.background_facebook_panel;
    }

    @Override
    public Drawable getBackground() {
        return SocialSynchro.getInstance().getResources().getDrawable(R.drawable.background_facebook);
    }

    @Override
    public Account createAccount(IDatabaseRow data) {
        return new FacebookAccount(data);
    }

    @Override
    public ChildPostContainer createPostContainer(IDatabaseRow data) {
        return new FacebookPostContainer(data);
    }

    @Override
    public ChildPostContainer createNewPostContainer(Account account) {
        return new FacebookPostContainer((FacebookAccount)account);
    }

    @Override
    public LoginFlow createLoginFlow(LoginActivity context) {
        return new FacebookLoginFlow(context);
    }

    @Override
    public boolean isOptionsEnabled() {
        return false;
    }

    @Override
    public PostOptions createNewPostOptions(ChildPostContainer post) {
        return null;
    }
}
