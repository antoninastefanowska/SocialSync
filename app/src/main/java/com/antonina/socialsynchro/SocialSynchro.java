package com.antonina.socialsynchro;

import android.app.Application;
import android.content.Context;
import android.util.Log;

//import com.instacart.library.truetime.TrueTime;

import java.io.IOException;

public class SocialSynchro extends Application {
    private static Context context;

    public void onCreate() {
        /*
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    TrueTime.build().initialize();
                }
                catch(IOException e) {
                    Log.d("initialization", "Couldn't initialize clock.");
                }
            }
        });
        t.start(); */

        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
