package com.antonina.socialsynchro.common.content.services;

import android.databinding.Bindable;
import android.graphics.drawable.Drawable;

import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.content.accounts.LoginFlow;
import com.antonina.socialsynchro.common.content.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.content.posts.PostOptions;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.common.gui.GUIItem;
import com.antonina.socialsynchro.common.gui.activities.LoginActivity;

public abstract class Service extends GUIItem {
    public abstract ServiceID getID();

    @Bindable
    public abstract String getName();

    public abstract int getIconID();

    @Bindable
    public abstract Drawable getBanner();

    @Bindable
    public abstract int getColor();

    @Bindable
    public abstract int getFontColor();

    @Bindable
    public abstract Drawable getPanelBackground();

    public abstract int getPanelBackgroundID();

    @Bindable
    public abstract Drawable getBackground();

    public abstract Account createAccount(IDatabaseRow data);

    public abstract ChildPostContainer createPostContainer(IDatabaseRow data);

    public abstract ChildPostContainer createNewPostContainer(Account account);

    public abstract LoginFlow createLoginFlow(LoginActivity context);

    public abstract boolean isOptionsEnabled();

    public abstract PostOptions createNewPostOptions(ChildPostContainer post);
}
