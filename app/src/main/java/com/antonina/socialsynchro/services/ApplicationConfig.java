package com.antonina.socialsynchro.services;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.SocialSynchro;
import com.antonina.socialsynchro.services.twitter.TwitterClient;
import com.antonina.socialsynchro.services.twitter.TwitterConfig;
import com.antonina.socialsynchro.services.twitter.requests.TwitterGetBearerTokenRequest;
import com.antonina.socialsynchro.services.twitter.responses.TwitterGetBearerTokenResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ApplicationConfig {
    private static ApplicationConfig instance;
    private Application application;

    private TwitterConfig twitterConfig;

    private ApplicationConfig() { }

    public static void createInstance(Application application) {
        instance = new ApplicationConfig();
        instance.application = application;
        instance.twitterConfig = new TwitterConfig(application);
    }

    public static ApplicationConfig getInstance() {
        return instance;
    }

    public TwitterConfig getTwitterConfig() {
        return twitterConfig;
    }

    public String getKey(String name) {
        Resources resources = application.getResources();
        try {
            InputStream rawResource = resources.openRawResource(R.raw.keys);
            Properties properties = new Properties();
            properties.load(rawResource);
            return properties.getProperty(name);
        } catch (Resources.NotFoundException e) {
            Toast toast = Toast.makeText(application, resources.getString(R.string.error_key_not_found), Toast.LENGTH_LONG);
            toast.show();
            return null;
        } catch (IOException e) {
            Toast toast = Toast.makeText(application, resources.getString(R.string.error_key_exception, e.getMessage()), Toast.LENGTH_LONG);
            toast.show();
            return null;
        }
    }
}
