package com.antonina.socialsynchro.common.gui.chart;

import android.content.Context;

import com.antonina.socialsynchro.common.content.statistics.AccountStatistic;
import com.antonina.socialsynchro.common.content.statistics.Statistic;
import com.antonina.socialsynchro.common.content.statistics.StatisticsContainer;
import com.antonina.socialsynchro.common.gui.listeners.OnLoadedListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class AccountsBarChartHolder extends BaseBarChartHolder {
    private transient int loadedIconCount;

    public AccountsBarChartHolder(StatisticsContainer statisticsContainer) {
        super(statisticsContainer);
    }

    @Override
    public void createChart(BarChart chart, Context context) {
        this.context = context;
        this.chart = chart;

        final List<AccountStatistic> statistics = new ArrayList<>();
        for (Statistic statistic : statisticsContainer.getStatistics())
            statistics.add((AccountStatistic)statistic);

        loadedIconCount = 0;
        for (AccountStatistic statistic : statistics) {
            statistic.loadBarBackground(context);
            statistic.loadIcon(context, new OnLoadedListener() {
                @Override
                public void onLoaded() {
                    if (loadedIconCount >= statistics.size() - 1) {
                        renderChart(statistics);
                        loadedIconCount = 0;
                    } else {
                        loadedIconCount++;
                    }
                }
            });
        }
    }

    private void renderChart(final List<AccountStatistic> statistics) {
        List<BarEntry> chartEntries = new ArrayList<>();
        for (int i = 0; i < statistics.size(); i++) {
            AccountStatistic statistic = statistics.get(i);
            chartEntries.add(new BarEntry(i, statistic.getValue()));
        }
        configureChart(chartEntries);
        chartData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                String name = statistics.get((int)barEntry.getX()).getName();
                return (int)barEntry.getY() + " " + name;
            }
        });

        chart.setRenderer(new AccountsBarChartRenderer(chart, chart.getAnimator(), chart.getViewPortHandler(), statistics, context));
        showChart();
    }
}
