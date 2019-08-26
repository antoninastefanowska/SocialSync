package com.antonina.socialsynchro.gui.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.content.ChildPostContainer;
import com.antonina.socialsynchro.content.ParentPostContainer;
import com.antonina.socialsynchro.database.repositories.ChildPostContainerRepository;
import com.antonina.socialsynchro.databinding.ActivityMainBinding;
import com.antonina.socialsynchro.gui.adapters.ParentMainAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static int CREATE = 0;

    private ActivityMainBinding binding;
    private RecyclerView parentRecyclerView;
    private ParentMainAdapter parentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        parentRecyclerView = (RecyclerView)findViewById(R.id.rvMainParents);
        parentRecyclerView.setHasFixedSize(true);
        parentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        parentAdapter = new ParentMainAdapter();

        binding.setParentMainAdapter(parentAdapter);
    }

    public void btAccounts_onClick(View view) {
        Intent accountsActivity = new Intent(MainActivity.this, AccountsActivity.class);
        startActivity(accountsActivity);
    }

    public void btCreateContent_onClick(View view) {
        Intent editActivity = new Intent(MainActivity.this, EditActivity.class);
        startActivityForResult(editActivity, CREATE);
    }

    public void btRefresh_onClick(View view) {
        parentAdapter.refreshData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CREATE:
                    ParentPostContainer parent = (ParentPostContainer)data.getSerializableExtra("parent");
                    parentAdapter.addItem(parent);
                    break;
            }
        }
    }
}
