package com.antonina.socialsynchro.common.content.statistics;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.gui.listeners.OnLoadedListener;
import com.antonina.socialsynchro.common.gui.other.MaskTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

public class AccountStatistic extends Statistic {
    private String name;
    private int value;
    private String iconURL;
    private String iconCaption;
    private int barBackgroundID;

    private transient Drawable barBackground;
    private transient Drawable icon;

    public AccountStatistic(String name, int value, String iconURL, String iconCaption, int barBackgroundID) {
        this.name = name;
        this.value = value;
        this.iconURL = iconURL;
        this.iconCaption = iconCaption;
        this.barBackgroundID = barBackgroundID;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getIconCaption() {
        return iconCaption;
    }

    public Drawable getBarBackground() {
        return barBackground;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void loadBarBackground(Context context) {
        barBackground = context.getResources().getDrawable(barBackgroundID);
    }

    public void loadIcon(Context context, final OnLoadedListener listener) {
        int iconSize = context.getResources().getDimensionPixelSize(R.dimen.profile_picture_size);
        RequestOptions options = new RequestOptions()
                .override(iconSize)
                .transform(new MaskTransformation(context));
        Glide.with(context)
                .asDrawable()
                .load(iconURL)
                .apply(options)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        icon = resource;
                        listener.onLoaded();
                    }
                });
    }
}
