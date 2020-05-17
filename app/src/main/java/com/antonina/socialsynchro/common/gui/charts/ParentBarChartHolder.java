package com.antonina.socialsynchro.common.gui.charts;

import android.content.Context;

import com.antonina.socialsynchro.common.model.statistics.ChildGroupStatistic;
import com.antonina.socialsynchro.common.model.statistics.ChildStatistic;
import com.antonina.socialsynchro.common.model.statistics.ParentStatistic;
import com.antonina.socialsynchro.common.model.statistics.StatisticsContainer;
import com.antonina.socialsynchro.common.gui.listeners.OnLoadedListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class ParentBarChartHolder extends BaseBarChartHolder {
    private transient int loadedIconCount;

    public ParentBarChartHolder(StatisticsContainer statisticsContainer) {
        super(statisticsContainer);
    }

    @Override
    public void createChart(BarChart chart, Context context) {
        this.context = context;
        this.chart = chart;

        ParentStatistic parentStatistic = (ParentStatistic)statisticsContainer.getStatistics().get(0);
        final List<ChildGroupStatistic> groupStatistics = parentStatistic.getChildGroups();

        loadedIconCount = 0;
        for (int i = 0; i < groupStatistics.size(); i++) {
            final ChildGroupStatistic groupStatistic = groupStatistics.get(i);
            for (ChildStatistic childStatistic : groupStatistic.getChildStatistics())
                childStatistic.loadBarBackground(context);

            groupStatistic.loadIcon(context, new OnLoadedListener() {
                @Override
                public void onLoaded() {
                    if (loadedIconCount >= groupStatistics.size() - 1) {
                        renderChart(groupStatistics);
                        loadedIconCount = 0;
                    } else {
                        loadedIconCount++;
                    }
                }
            });
        }
    }

    private void renderChart(List<ChildGroupStatistic> groupStatistics) {
        List<BarEntry> chartEntries = new ArrayList<>();
        final List<ChildStatistic> childStatistics = new ArrayList<>();
        ChildStatistic last = null;
        for (ChildGroupStatistic groupStatistic : groupStatistics) {
            if (last != null) {
                ChildStatistic emptyChild = new ChildStatistic("", 0, 0);
                emptyChild.setGroup(groupStatistic);
                childStatistics.add(emptyChild);
            }
            childStatistics.addAll(groupStatistic.getChildStatistics());
            last = childStatistics.get(childStatistics.size() - 1);
            last.setLast(true);
        }

        for (int i = 0; i < childStatistics.size(); i++) {
            ChildStatistic childStatistic = childStatistics.get(i);
            chartEntries.add(new BarEntry(i, childStatistic.getValue()));
        }
        configureChart(chartEntries);

        chartData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                String name = childStatistics.get((int)barEntry.getX()).getName();
                return name.isEmpty() ? "" : (int)barEntry.getY() + " " + name;
            }
        });
        chart.setRenderer(new ParentBarChartRenderer(chart, chart.getAnimator(), chart.getViewPortHandler(), childStatistics, context));
        showChart();
    }
}
