package com.antonina.socialsynchro.gui.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.databinding.ActivityAccountsBinding;
import com.antonina.socialsynchro.gui.adapters.AccountAdapter;
import com.antonina.socialsynchro.base.Account;

public class AccountsActivity extends AppCompatActivity {
    private final static int ADD = 0;

    private ActivityAccountsBinding binding;
    private RecyclerView recyclerView;
    private AccountAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_accounts);
        recyclerView = (RecyclerView)findViewById(R.id.rvAccounts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AccountAdapter();

        binding.setAccountAdapter(adapter);
    }

    public void btAddAccount_onClick(View view) {
        Intent connectActivity = new Intent(AccountsActivity.this, ConnectActivity.class);
        startActivityForResult(connectActivity, ADD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD:
                    Account account = (Account)data.getSerializableExtra("account");
                    adapter.addItem(account);
                    break;
            }
        }
    }
}
