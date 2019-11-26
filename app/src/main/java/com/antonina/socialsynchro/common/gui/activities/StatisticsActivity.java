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

        BaseBarChartHolder chartContainer = null;

        if (getIntent().hasExtra("chart_container"))
            chartContainer = (BaseBarChartHolder)getIntent().getSerializableExtra("chart_container");


        BarChart chart = findViewById(R.id.chart);
        if (chartContainer != null)
            chartContainer.createChart(chart, this);
    }
}
