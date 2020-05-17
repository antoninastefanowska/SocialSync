package com.antonina.socialsynchro.common.gui.charts;

import android.graphics.Canvas;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public abstract class BaseBarChartRenderer extends BarChartRenderer {
    protected BarBuffer buffer;

    public BaseBarChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    @Override
    protected void drawDataSet(Canvas canvas, IBarDataSet dataSet, int index) {
        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        buffer = mBarBuffers[index];
        buffer.setPhases(phaseX, phaseY);
        buffer.setDataSet(index);
        buffer.setInverted(mChart.isInverted(dataSet.getAxisDependency()));
        buffer.setBarWidth(mChart.getBarData().getBarWidth());
        buffer.feed(dataSet);
        trans.pointValuesToPixel(buffer.buffer);

        float left, top, right, bottom;

        for (int j = 0; j < buffer.size(); j += 4) {
            left = buffer.buffer[j];
            top = buffer.buffer[j + 1];
            right = buffer.buffer[j + 2];
            bottom = buffer.buffer[j + 3];

            if (!mViewPortHandler.isInBoundsLeft(right))
                continue;

            if (!mViewPortHandler.isInBoundsRight(left))
                break;

            drawBar(canvas, dataSet,j / 4, left, top, right, bottom);
        }
    }

    public abstract void drawBar(Canvas canvas, IBarDataSet dataSet, int index, float left, float top, float right, float bottom);
}
