package com.antonina.socialsynchro.common.gui.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.content.statistics.AccountStatistic;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

public class AccountsBarChartRenderer extends BaseBarChartRenderer {
    private List<AccountStatistic> data;
    private Drawable iconBackground;

    private float iconCaptionOffset;
    private float iconBackgroundSize;
    private int iconSize;

    public AccountsBarChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler, List<AccountStatistic> data, Context context) {
        super(chart, animator, viewPortHandler);
        this.data = data;

        iconBackground = context.getResources().getDrawable(R.drawable.background_avatar);
        iconBackgroundSize = context.getResources().getDimensionPixelSize(R.dimen.profile_picture_background_size);
        iconSize = context.getResources().getDimensionPixelSize(R.dimen.profile_picture_size);
        iconCaptionOffset = context.getResources().getDimensionPixelSize(R.dimen.icon_caption_offset);
    }

    @Override
    public void drawBar(Canvas canvas, IBarDataSet dataSet, int index, float left, float top, float right, float bottom) {
        AccountStatistic statistic = data.get(index);

        Drawable bar = statistic.getBarBackground();
        Drawable icon = statistic.getIcon();
        String iconCaption = statistic.getIconCaption();

        float middle = (right + left) / 2f;

        bar.setBounds((int)left, (int)top, (int)right, (int)bottom);
        bar.draw(canvas);

        iconBackground.setBounds((int)(middle - iconBackgroundSize / 2), (int)(top - iconBackgroundSize / 2), (int)(middle + iconBackgroundSize / 2), (int)(top + iconBackgroundSize / 2));
        iconBackground.draw(canvas);

        icon.setBounds((int)(middle - iconSize / 2), (int)(top - iconSize / 2), (int)(middle + iconSize / 2), (int)(top + iconSize / 2));
        icon.draw(canvas);

        super.drawValue(canvas, iconCaption, middle, (int)(top + iconBackgroundSize / 2 +  iconCaptionOffset), dataSet.getValueTextColor());
    }

    @Override
    public void drawValue(Canvas canvas, String valueText, float x, float y, int color) {
        super.drawValue(canvas, valueText, x, y - iconBackgroundSize / 2, color);
    }
}