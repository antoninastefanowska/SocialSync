package com.antonina.socialsynchro.common.content.accounts;

import android.content.Context;

import com.antonina.socialsynchro.common.gui.activities.LoginActivity;

public abstract class LoginFlow {
    protected LoginActivity context;

    public LoginFlow(LoginActivity context) {
        this.context = context;
    }

    public abstract void signIn();

    public abstract void confirm();

    protected abstract void complete();
}
