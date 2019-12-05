package com.antonina.socialsynchro.common.gui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.gui.chart.BaseBarChartHolder;
import com.github.mikephil.charting.charts.BarChart;

public class StatisticsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        BaseBarChartHolder chartHolder = null;

        if (getIntent().hasExtra("chart_holder"))
            chartHolder = (BaseBarChartHolder)getIntent().getSerializableExtra("chart_holder");


        BarChart chart = findViewById(R.id.chart);
        if (chartHolder != null)
            chartHolder.createChart(chart, this);
    }
}
