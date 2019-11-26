package com.antonina.socialsynchro.common.content.statistics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StatisticsContainer implements Serializable {
    private List<Statistic> statistics;

    public StatisticsContainer() {
        statistics = new ArrayList<>();
    }

    public void addStatistic(Statistic statistic) {
        statistics.add(statistic);
    }

    public List<Statistic> getStatistics() {
        return statistics;
    }
}
