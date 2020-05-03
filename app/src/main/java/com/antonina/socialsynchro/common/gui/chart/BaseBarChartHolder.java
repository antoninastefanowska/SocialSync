package com.antonina.socialsynchro.common.gui.chart;

import android.content.Context;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.model.statistics.StatisticsContainer;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.Serializable;
import java.util.List;

public abstract class BaseBarChartHolder implements Serializable {
    protected transient BarChart chart;
    protected transient BarData chartData;
    protected transient Context context;
    protected StatisticsContainer statisticsContainer;

    public BaseBarChartHolder(StatisticsContainer statisticsContainer) {
        this.statisticsContainer = statisticsContainer;
    }

    public abstract void createChart(BarChart chart, Context context);

    protected void configureChart(List<BarEntry> chartEntries) {
        BarDataSet chartDataSet = new BarDataSet(chartEntries, "Data");
        chartData = new BarData(chartDataSet);
        chartData.setHighlightEnabled(false);
        chartData.setBarWidth(0.5f);
        chartData.setValueTextSize(context.getResources().getDimensionPixelSize(R.dimen.text_size_chart));
        chartData.setValueTextColor(context.getResources().getColor(R.color.colorLight));

        XAxis xAxis = chart.getXAxis();
        xAxis.setEnabled(false);

        YAxis yAxisLeft = chart.getAxisLeft();
        YAxis yAxisRight = chart.getAxisRight();

        yAxisLeft.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.text_size_chart));
        yAxisLeft.setTextColor(context.getResources().getColor(R.color.colorLight));
        yAxisLeft.setGridColor(context.getResources().getColor(R.color.colorLight));
        yAxisLeft.setAxisLineColor(context.getResources().getColor(R.color.colorLight));

        yAxisRight.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.text_size_chart));
        yAxisRight.setTextColor(context.getResources().getColor(R.color.colorLight));
        yAxisRight.setGridColor(context.getResources().getColor(R.color.colorLight));
        yAxisRight.setAxisLineColor(context.getResources().getColor(R.color.colorLight));

        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setNoDataTextColor(context.getResources().getColor(R.color.colorLight));
    }

    protected void showChart() {
        chart.setData(chartData);
        chart.animateY(1000, Easing.EaseInQuad);
    }
}
