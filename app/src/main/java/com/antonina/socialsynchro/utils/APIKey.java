package com.antonina.socialsynchro.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.antonina.socialsynchro.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class APIKey {
    public static String getKey(Context context, String name) {
        Resources resources = context.getResources();
        try {
            InputStream rawResource = resources.openRawResource(R.raw.keys);
            Properties properties = new Properties();
            properties.load(rawResource);
            return properties.getProperty(name);
        } catch (Resources.NotFoundException e) {
            Log.d("error", "API key not found.");
            return null;
        } catch (IOException e) {
            Log.d("error", "Couldn't retrieve API key.");
            return null;
        }
    }
}
