package com.antonina.socialsynchro.services;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.SocialSynchro;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class APIKey {
    public static String getKey(String name) {
        Context context = SocialSynchro.getInstance();
        Resources resources = context.getResources();
        try {
            InputStream rawResource = resources.openRawResource(R.raw.keys);
            Properties properties = new Properties();
            properties.load(rawResource);
            return properties.getProperty(name);
        } catch (Resources.NotFoundException e) {
            Toast toast = Toast.makeText(context, resources.getString(R.string.error_key_not_found), Toast.LENGTH_LONG);
            toast.show();
            return null;
        } catch (IOException e) {
            Toast toast = Toast.makeText(context, resources.getString(R.string.error_key_exception, e.getMessage()), Toast.LENGTH_LONG);
            toast.show();
            return null;
        }
    }
}
