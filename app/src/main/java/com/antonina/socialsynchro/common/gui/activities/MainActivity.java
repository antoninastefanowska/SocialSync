package com.antonina.socialsynchro.common.gui.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.model.attachments.Attachment;
import com.antonina.socialsynchro.common.model.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.model.posts.ParentPostContainer;
import com.antonina.socialsynchro.common.model.posts.PostContainer;
import com.antonina.socialsynchro.common.model.statistics.StatisticsContainer;
import com.antonina.socialsynchro.common.gui.charts.ChildBarChartHolder;
import com.antonina.socialsynchro.common.gui.charts.ParentBarChartHolder;
import com.antonina.socialsynchro.common.gui.listeners.OnAttachmentUploadedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnPublishedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnUnpublishedListener;
import com.antonina.socialsynchro.common.gui.other.DynamicResource;
import com.antonina.socialsynchro.databinding.ActivityMainBinding;
import com.antonina.socialsynchro.common.gui.adapters.ParentDisplayAdapter;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.rest.IServiceEntity;

public class MainActivity extends AppCompatActivity {
    private final static int ACCOUNTS = 0, CREATE = 1, EDIT = 2;

    private ParentDisplayAdapter parentAdapter;
    private ParentPostContainer editParent;

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        DynamicResource.free();
    }

    public void showAccounts(View view) {
        Intent accountsActivity = new Intent(MainActivity.this, AccountsActivity.class);
        startActivityForResult(accountsActivity, ACCOUNTS);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public void createNew() {
        Intent editActivity = new Intent(MainActivity.this, EditActivity.class);
        startActivityForResult(editActivity, CREATE);
    }

    public void editParent(ParentPostContainer parent) {
        Intent editActivity = new Intent(MainActivity.this, EditActivity.class);
        editActivity.putExtra("parent", parent);
        editParent = parent;
        startActivityForResult(editActivity, EDIT);
    }

    public void synchronizePost(PostContainer postContainer) {
        final Context context = this;
        postContainer.synchronize(new OnSynchronizedListener() {
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
        });
    }

    public void showParentStatistics(ParentPostContainer parent) {
        StatisticsContainer statisticsContainer = new StatisticsContainer();
        statisticsContainer.addStatistic(parent.getStatistic());

        ParentBarChartHolder chartHolder = new ParentBarChartHolder(statisticsContainer);
        Intent statisticsActivity = new Intent(MainActivity.this, StatisticsActivity.class);
        statisticsActivity.putExtra("chart_holder", chartHolder);
        startActivity(statisticsActivity);
    }

    public void showChildStatistics(ChildPostContainer child) {
        StatisticsContainer statisticsContainer = new StatisticsContainer();
        statisticsContainer.addStatistic(child.getStatistic());

        ChildBarChartHolder chartHolder = new ChildBarChartHolder(statisticsContainer);
        Intent statisticsActivity = new Intent(MainActivity.this, StatisticsActivity.class);
        statisticsActivity.putExtra("chart_holder", chartHolder);
        startActivity(statisticsActivity);
    }

    public void openChildLink(ChildPostContainer child) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(child.getURL()));
        startActivity(browserIntent);
    }

    public void publishPost(PostContainer post) {
        final Context context = this;
        final View layout = findViewById(R.id.layout_main);
        post.publish(new OnPublishedListener() {
            @Override
            public void onPublished(ChildPostContainer publishedPost) {
                Snackbar snackbar = Snackbar.make(layout, "Succesfully published: " + publishedPost.getExternalID(), Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            @Override
            public void onError(ChildPostContainer post, String error) {
                Snackbar snackbar = Snackbar.make(layout, "Failed to publish. Error: " + error, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }, new OnAttachmentUploadedListener() {
            @Override
            public void onInitialized(Attachment attachment) {
                Toast toast = Toast.makeText(context, "Attachment initialized: " + attachment.getFile().getName(), Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onProgress(Attachment attachment) {
                Toast toast = Toast.makeText(context, "Attachment upload progress: " + attachment.getUploadProgress() + "%", Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onFinished(Attachment attachment) {
                Toast toast = Toast.makeText(context, "Attachment upload finished: " + attachment.getExternalID(), Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onError(Attachment attachment, String error) {
                Toast toast = Toast.makeText(context, "Attachment upload failed: " + attachment.getFile().getName(), Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    public void unpublishPost(PostContainer postContainer) {
        final Context context = this;
        postContainer.unpublish(new OnUnpublishedListener() {
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
        });
    }

    public void removePost(PostContainer postContainer) {
        postContainer.deleteFromDatabase();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ACCOUNTS: {
                    boolean accountsChanged = data.getBooleanExtra("accountsChanged", false);
                    if (accountsChanged)
                        parentAdapter.loadData();
                    break;
                }
                case CREATE: {
                    ParentPostContainer parent = (ParentPostContainer) data.getSerializableExtra("parent");
                    parentAdapter.addItem(parent);
                    parent.saveInDatabase();
                    break;
                }
                case EDIT: {
                    ParentPostContainer parent = (ParentPostContainer) data.getSerializableExtra("parent");
                    parentAdapter.editItem(editParent, parent);
                    parent.updateInDatabase();
                    editParent = null;
                    break;
                }
            }
        }
    }
}
