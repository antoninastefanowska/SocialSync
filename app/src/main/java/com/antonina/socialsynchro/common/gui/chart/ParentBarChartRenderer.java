package com.antonina.socialsynchro.common.gui.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.model.statistics.ChildStatistic;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

public class ParentBarChartRenderer extends BaseBarChartRenderer {
    private List<ChildStatistic> data;

    private Drawable iconBackground;
    private float iconCaptionOffset;
    private float iconBackgroundSize;
    private int iconSize;

    public ParentBarChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler, List<ChildStatistic> statistics, Context context) {
        super(chart, animator, viewPortHandler);
        data = statistics;

        iconBackground = context.getResources().getDrawable(R.drawable.background_avatar);
        iconBackgroundSize = context.getResources().getDimensionPixelSize(R.dimen.profile_picture_background_size);
        iconSize = context.getResources().getDimensionPixelSize(R.dimen.profile_picture_size);
        iconCaptionOffset = context.getResources().getDimensionPixelSize(R.dimen.icon_caption_offset);
    }

    @Override
    public void drawBar(Canvas canvas, IBarDataSet dataSet, int index, float left, float top, float right, float bottom) {
        ChildStatistic statistic = data.get(index);

        if (!statistic.getName().equals("")) {
            Drawable bar = statistic.getBarBackground();
            bar.setBounds((int)left, (int)top, (int)right, (int)bottom);
            bar.draw(canvas);

            if (statistic.isLast())  {
                Drawable icon = statistic.getIcon();
                String iconCaption = statistic.getIconCaption();
                float groupStart = buffer.buffer[(index - statistic.getGroupIndex()) * 4];
                float middle = (right + groupStart) / 2f;

                iconBackground.setBounds((int) (middle - iconBackgroundSize / 2), (int) (bottom - iconBackgroundSize / 2), (int) (middle + iconBackgroundSize / 2), (int) (bottom + iconBackgroundSize / 2));
                iconBackground.draw(canvas);

                icon.setBounds((int) (middle - iconSize / 2), (int) (bottom - iconSize / 2), (int) (middle + iconSize / 2), (int) (bottom + iconSize / 2));
                icon.draw(canvas);
                super.drawValue(canvas, iconCaption, middle, (int) (bottom + iconBackgroundSize / 2 + iconCaptionOffset), dataSet.getValueTextColor());
            }
        }
    }
}
