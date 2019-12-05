package com.antonina.socialsynchro.common.gui.chart;

import android.content.Context;

import com.antonina.socialsynchro.common.content.statistics.ChildGroupStatistic;
import com.antonina.socialsynchro.common.content.statistics.ChildStatistic;
import com.antonina.socialsynchro.common.content.statistics.StatisticsContainer;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class ChildBarChartHolder extends BaseBarChartHolder {
    public ChildBarChartHolder(StatisticsContainer statisticsContainer) {
        super(statisticsContainer);
    }

    @Override
    public void createChart(BarChart chart, Context context) {
        this.context = context;
        this.chart = chart;

        ChildGroupStatistic groupStatistic = (ChildGroupStatistic)statisticsContainer.getStatistics().get(0);
        final List<ChildStatistic> statistics = groupStatistic.getChildStatistics();
        for (ChildStatistic childStatistic : statistics)
            childStatistic.loadBarBackground(context);
        renderChart(statistics);
    }

    private void renderChart(final List<ChildStatistic> statistics) {
        List<BarEntry> chartEntries = new ArrayList<>();
        for (int i = 0; i < statistics.size(); i++) {
            ChildStatistic childStatistic = statistics.get(i);
            chartEntries.add(new BarEntry(i, childStatistic.getValue()));
        }
        configureChart(chartEntries);

        chartData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                String name = statistics.get((int)barEntry.getX()).getName();
                return (int)barEntry.getY() + " " + name;
            }
        });
        chart.setRenderer(new ChildBarChartRenderer(chart, chart.getAnimator(), chart.getViewPortHandler(), statistics));
        showChart();
    }
}
