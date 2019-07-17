package com.antonina.socialsynchro;

import android.app.Application;
import android.content.Context;

public class SocialSynchro extends Application {
    private static Context context;
    private static Application instance;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        instance = this;
    }

    public static Context getAppContext() {
        return context;
    }

    public static Application getInstance() { return instance; }
}
