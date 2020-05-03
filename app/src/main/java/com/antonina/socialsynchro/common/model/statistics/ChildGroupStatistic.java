package com.antonina.socialsynchro.common.model.statistics;

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

import java.util.ArrayList;
import java.util.List;

public class ChildGroupStatistic extends Statistic {
    private String iconURL;
    private String iconCaption;
    private List<ChildStatistic> childStatistics;

    private transient Drawable icon;

    public ChildGroupStatistic(String iconURL, String iconCaption) {
        this.iconURL = iconURL;
        this.iconCaption = iconCaption;
        childStatistics = new ArrayList<>();
    }

    public void addChildStatistic(ChildStatistic childStatistic) {
        childStatistics.add(childStatistic);
        childStatistic.setGroup(this);
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getIconCaption() {
        return iconCaption;
    }

    public List<ChildStatistic> getChildStatistics() {
        return childStatistics;
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
