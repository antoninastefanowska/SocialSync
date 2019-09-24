package com.antonina.socialsynchro.gui.activities;

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
import com.antonina.socialsynchro.databinding.ActivityAccountsBinding;
import com.antonina.socialsynchro.gui.adapters.AccountDisplayAdapter;
import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.gui.dialogs.ChooseServiceDialog;
import com.antonina.socialsynchro.gui.listeners.OnServiceSelectedListener;
import com.antonina.socialsynchro.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.services.IServiceEntity;
import com.antonina.socialsynchro.services.Service;

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

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("accountsChanged", accountsChanged);
        setResult(RESULT_OK, data);
        super.finish();
    }

    public void buttonAddAccount_onClick(View view) {
        ChooseServiceDialog dialog = new ChooseServiceDialog(this, new OnServiceSelectedListener() {
            @Override
            public void onServiceSelected(Service service) {
                Intent loginActivity = new Intent(AccountsActivity.this, service.getLoginActivityClass());
                startActivityForResult(loginActivity, ADD_ACCOUNT);
            }
        });
        dialog.show();
    }

    public void buttonRemoveAccount_onClick(View view) {
        for (Account account : adapter.getSelectedItems())
            account.deleteFromDatabase();
        adapter.removeSelected();
        accountsChanged = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_ACCOUNT:
                    Account account = (Account)data.getSerializableExtra("account");
                    adapter.addItem(account);
                    account.saveInDatabase();
                    if (account.hasBeenUpdated()) {
                        adapter.loadData();
                        accountsChanged = true;
                        account.setUpdated(false);
                    }
                    break;
            }
        }
    }

    public void buttonSynchronizeAccount_onClick(View view) {
        List<Account> selectedAccounts = adapter.getSelectedItems();
        final Context context = this;
        OnSynchronizedListener listener = new OnSynchronizedListener() {
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
        };
        for (Account account : selectedAccounts) {
            account.synchronize(listener);
        }
    }
}
