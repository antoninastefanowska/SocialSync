package com.antonina.socialsynchro.common.model.statistics;

import java.util.ArrayList;
import java.util.List;

public class ParentStatistic extends Statistic {
    private List<ChildGroupStatistic> childGroups;

    public ParentStatistic() {
        childGroups = new ArrayList<>();
    }

    public void addChildGroup(ChildGroupStatistic childGroup) {
        childGroups.add(childGroup);
    }

    public List<ChildGroupStatistic> getChildGroups() {
        return childGroups;
    }
}
