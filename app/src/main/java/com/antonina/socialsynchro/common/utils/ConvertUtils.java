package com.antonina.socialsynchro.common.utils;

import android.content.res.Resources;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.SocialSynchro;

public class ConvertUtils {

    private ConvertUtils() { }

    public static String durationToString(long durationInSeconds) {
        int hours = (int)(durationInSeconds / 3600);
        int minutes = (int)((durationInSeconds % 3600) / 60);
        int seconds = (int)(durationInSeconds % 60);
        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }

    public static String requestLimitWaitMessage(long durationInSeconds) {
        Resources resources = SocialSynchro.getInstance().getResources();
        return resources.getString(R.string.error_request_limit_exceeded, durationToString(durationInSeconds));
    }
}
