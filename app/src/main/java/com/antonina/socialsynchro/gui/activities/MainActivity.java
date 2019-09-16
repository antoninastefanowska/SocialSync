package com.antonina.socialsynchro.gui.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.content.ParentPostContainer;
import com.antonina.socialsynchro.databinding.ActivityMainBinding;
import com.antonina.socialsynchro.gui.adapters.ParentMainAdapter;

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
        parentRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_parents);
        parentRecyclerView.setHasFixedSize(true);
        parentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        parentAdapter = new ParentMainAdapter();

        binding.setParentMainAdapter(parentAdapter);
    }

    //TODO: Dane zależą od działalności w innych aktywnościach (np. AccountsActivity) - odświeżyć dane przy powrocie do aktywności MainActivity

    public void buttonAccounts_onClick(View view) {
        Intent accountsActivity = new Intent(MainActivity.this, AccountsActivity.class);
        startActivity(accountsActivity);
    }

    public void buttonCreateContent_onClick(View view) {
        Intent editActivity = new Intent(MainActivity.this, EditActivity.class);
        startActivityForResult(editActivity, CREATE);
    }

    public void buttonRefresh_onClick(View view) {
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
                    parent.saveInDatabase();
                    break;
            }
        }
    }
}
