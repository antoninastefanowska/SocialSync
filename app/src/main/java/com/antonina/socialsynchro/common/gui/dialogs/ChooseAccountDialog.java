package com.antonina.socialsynchro.common.gui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.gui.adapters.AccountDialogAdapter;
import com.antonina.socialsynchro.common.gui.listeners.OnAccountsSelectedListener;

import java.util.List;

public class ChooseAccountDialog extends Dialog {
    private final AppCompatActivity context;
    private final OnAccountsSelectedListener listener;
    private AccountDialogAdapter adapter;
    private List<Account> ignoredData;

    public ChooseAccountDialog(@NonNull AppCompatActivity context, OnAccountsSelectedListener listener) {
        super(context, R.style.DialogStyle);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.dialog_choose_account);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_choose_accounts);
        RecyclerView recyclerView = findViewById(R.id.recyclerview_accounts_dialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new AccountDialogAdapter(context);
        adapter.setIgnoredData(ignoredData);
        recyclerView.setAdapter(adapter);

        Button buttonOk = findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAccountsSelected(adapter.getSelectedItems());
                dismiss();
            }
        });

        Button buttonCancel = findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                dismiss();
            }
        });
    }

    public void setIgnoredData(List<Account> ignoredData) {
        this.ignoredData = ignoredData;
    }
}
