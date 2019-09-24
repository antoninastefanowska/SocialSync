package com.antonina.socialsynchro.gui.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.content.ParentPostContainer;
import com.antonina.socialsynchro.databinding.ActivityMainBinding;
import com.antonina.socialsynchro.gui.adapters.ParentDisplayAdapter;
import com.antonina.socialsynchro.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.services.IServiceEntity;

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
}
