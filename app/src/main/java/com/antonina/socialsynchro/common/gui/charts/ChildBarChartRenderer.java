package com.antonina.socialsynchro.common.gui.charts;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.antonina.socialsynchro.common.model.statistics.ChildStatistic;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

public class ChildBarChartRenderer extends BaseBarChartRenderer {
    List<ChildStatistic> data;

    public ChildBarChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler, List<ChildStatistic> statistics) {
        super(chart, animator, viewPortHandler);
        data = statistics;
    }

    @Override
    public void drawBar(Canvas canvas, IBarDataSet dataSet, int index, float left, float top, float right, float bottom) {
        ChildStatistic statistic = data.get(index);
        Drawable bar = statistic.getBarBackground();
        bar.setBounds((int)left, (int)top, (int)right, (int)bottom);
        bar.draw(canvas);
    }
}
