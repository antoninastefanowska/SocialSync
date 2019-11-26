package com.antonina.socialsynchro.common.gui.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.content.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.content.posts.ParentPostContainer;
import com.antonina.socialsynchro.common.content.statistics.StatisticsContainer;
import com.antonina.socialsynchro.common.gui.chart.ParentBarChartHolder;
import com.antonina.socialsynchro.common.gui.listeners.OnUnpublishedListener;
import com.antonina.socialsynchro.databinding.ActivityMainBinding;
import com.antonina.socialsynchro.common.gui.adapters.ParentDisplayAdapter;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.rest.IServiceEntity;
import com.antonina.socialsynchro.services.twitter.rest.TwitterClient;
import com.antonina.socialsynchro.services.twitter.rest.requests.TwitterGetRateLimitsRequest;
import com.antonina.socialsynchro.services.twitter.rest.authorization.TwitterApplicationAuthorizationStrategy;
import com.antonina.socialsynchro.services.twitter.rest.responses.TwitterGetRateLimitsResponse;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static int ACCOUNTS = 0, CREATE = 1;

    private ParentDisplayAdapter parentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        RecyclerView parentRecyclerView = findViewById(R.id.recyclerview_parents);
        parentRecyclerView.setHasFixedSize(true);
        parentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        parentAdapter = new ParentDisplayAdapter(this);

        binding.setParentAdapter(parentAdapter);
        binding.executePendingBindings();
    }

    public void buttonAccounts_onClick(View view) {
        Intent accountsActivity = new Intent(MainActivity.this, AccountsActivity.class);
        startActivityForResult(accountsActivity, ACCOUNTS);
    }

    public void buttonCreateContent_onClick(View view) {
        Intent editActivity = new Intent(MainActivity.this, EditActivity.class);
        startActivityForResult(editActivity, CREATE);
    }

    public void buttonRemoveContent_onClick(View view) {
        for (ParentPostContainer parent : parentAdapter.getSelectedItems())
            parent.deleteFromDatabase();
        parentAdapter.removeSelected();
    }

    public void buttonUnpublishContent_onClick(View view) {
        final Context context = this;
        OnUnpublishedListener listener = new OnUnpublishedListener() {
            @Override
            public void onUnpublished(ChildPostContainer unpublishedPost) {
                Toast toast = Toast.makeText(context, getResources().getString(R.string.message_content_unpublish), Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onError(ChildPostContainer post, String error) {
                Toast toast = Toast.makeText(context, getResources().getString(R.string.error_content_unpublish, error), Toast.LENGTH_LONG);
                toast.show();
            }
        };
        for (ParentPostContainer parent : parentAdapter.getSelectedItems())
            parent.unpublish(listener);
    }

    public void buttonSynchronizeContent_onClick(View view) {
        List<ParentPostContainer> selectedParents = parentAdapter.getSelectedItems();
        final Context context = this;
        OnSynchronizedListener listener = new OnSynchronizedListener() {
            @Override
            public void onSynchronized(IServiceEntity entity) {
                Toast toast = Toast.makeText(context, getResources().getString(R.string.message_content_synchronization), Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onError(IServiceEntity entity, String error) {
                Toast toast = Toast.makeText(context, getResources().getString(R.string.error_content_synchronization, error), Toast.LENGTH_LONG);
                toast.show();
            }
        };
        for (ParentPostContainer parent : selectedParents) {
            parent.synchronize(listener);
        }
    }

    public void buttonStatistics_onClick(View view) {
        List<ParentPostContainer> selectedParents = parentAdapter.getSelectedItems();
        ParentPostContainer parent = selectedParents.get(0);
        StatisticsContainer statisticsContainer = new StatisticsContainer();
        statisticsContainer.addStatistic(parent.getStatistic());

        ParentBarChartHolder chartContainer = new ParentBarChartHolder(statisticsContainer);
        Intent statisticsActivity = new Intent(MainActivity.this, StatisticsActivity.class);
        statisticsActivity.putExtra("chart_container", chartContainer);
        startActivity(statisticsActivity);
    }

    public void buttonCheckLimits_onClick(View view) {
        TwitterApplicationAuthorizationStrategy authorization = new TwitterApplicationAuthorizationStrategy();
        TwitterGetRateLimitsRequest request = TwitterGetRateLimitsRequest.builder()
                .addResource("users")
                .addResource("statuses")
                .addResource("application")
                .authorizationStrategy(authorization)
                .build();
        LiveData<TwitterGetRateLimitsResponse> asyncResponse = TwitterClient.getRateLimits(request);
        asyncResponse.observe(this, new Observer<TwitterGetRateLimitsResponse>() {
            @Override
            public void onChanged(@Nullable TwitterGetRateLimitsResponse response) {
                if (response != null) {
                    if (response.getErrorString() == null) {
                        Log.d("limity", "Pobrano limity dla aplikacji.");
                    } else {
                        Log.d("limity", "Błąd pobierania limitów dla aplikacji.");
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACCOUNTS:
                    boolean accountsChanged = data.getBooleanExtra("accountsChanged", false);
                    if (accountsChanged)
                        parentAdapter.loadData();
                    break;
                case CREATE:
                    ParentPostContainer parent = (ParentPostContainer)data.getSerializableExtra("parent");
                    parentAdapter.addItem(parent);
                    parent.saveInDatabase();
                    break;
            }
        }
    }
}
