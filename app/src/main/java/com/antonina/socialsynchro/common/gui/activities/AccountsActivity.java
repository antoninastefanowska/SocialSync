package com.antonina.socialsynchro.common.gui.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.model.statistics.StatisticsContainer;
import com.antonina.socialsynchro.common.gui.chart.AccountsBarChartHolder;
import com.antonina.socialsynchro.common.gui.other.SerializableList;
import com.antonina.socialsynchro.databinding.ActivityAccountsBinding;
import com.antonina.socialsynchro.common.gui.adapters.AccountDisplayAdapter;
import com.antonina.socialsynchro.common.model.accounts.Account;
import com.antonina.socialsynchro.common.gui.dialogs.ChooseServiceDialog;
import com.antonina.socialsynchro.common.gui.listeners.OnServiceSelectedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.rest.IServiceEntity;
import com.antonina.socialsynchro.common.model.services.Service;

import java.util.List;

public class AccountsActivity extends AppCompatActivity {
    private final static int ADD_ACCOUNT = 0;

    private AccountDisplayAdapter adapter;
    private boolean accountsChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        accountsChanged = false;

        ActivityAccountsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_accounts);
        RecyclerView recyclerView = findViewById(R.id.recyclerview_accounts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AccountDisplayAdapter(this);

        binding.setAccountAdapter(adapter);
    }

    public void showPosts(View view) {
        finish();
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("accountsChanged", accountsChanged);
        setResult(RESULT_OK, data);
        super.finish();
    }

    public void addAccount() {
        ChooseServiceDialog dialog = new ChooseServiceDialog(this, new OnServiceSelectedListener() {
            @Override
            public void onServiceSelected(Service service) {
                Intent loginActivity = new Intent(AccountsActivity.this, LoginActivity.class);
                loginActivity.putExtra("service", service);
                startActivityForResult(loginActivity, ADD_ACCOUNT);
            }
        });
        dialog.show();
    }

    public void removeAccount(Account account) {
        account.deleteFromDatabase();
        adapter.removeItem(account);
    }

    public void synchronizeAccount(Account account) {
        final Context context = this;
        account.synchronize(new OnSynchronizedListener() {
            @Override
            public void onSynchronized(IServiceEntity entity) {
                Toast toast = Toast.makeText(context, getResources().getString(R.string.message_account_synchronization), Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onError(IServiceEntity entity, String error) {
                Toast toast = Toast.makeText(context, getResources().getString(R.string.error_account_synchronization, error), Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    public void showStatistics(View view) {
        List<Account> accounts = adapter.getItems();
        StatisticsContainer statisticsContainer = new StatisticsContainer();
        for (Account account : accounts)
            statisticsContainer.addStatistic(account.getStatistic());
        AccountsBarChartHolder chartHolder = new AccountsBarChartHolder(statisticsContainer);

        Intent statisticsActivity = new Intent(AccountsActivity.this, StatisticsActivity.class);
        statisticsActivity.putExtra("chart_holder", chartHolder);
        startActivity(statisticsActivity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_ACCOUNT:
                    SerializableList<Account> serializableAccounts = (SerializableList<Account>)data.getSerializableExtra("accounts");
                    List<Account> accounts = serializableAccounts.getList();
                    for (Account account : accounts) {
                        adapter.addItem(account);
                        account.saveInDatabase();
                        if (account.hasBeenUpdated()) {
                            adapter.loadData();
                            accountsChanged = true;
                            account.setUpdated(false);
                        }
                    }
                    break;
            }
        }
    }
}
