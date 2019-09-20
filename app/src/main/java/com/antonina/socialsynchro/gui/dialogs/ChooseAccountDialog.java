package com.antonina.socialsynchro.gui.dialogs;

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
import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.gui.adapters.AccountDialogAdapter;
import com.antonina.socialsynchro.gui.listeners.OnAccountsSelectedListener;

import java.util.List;

public class ChooseAccountDialog extends Dialog {
    private AppCompatActivity context;
    private AccountDialogAdapter adapter;
    private OnAccountsSelectedListener listener;
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

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview_accounts_dialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new AccountDialogAdapter(context);
        adapter.setIgnoredData(ignoredData);
        recyclerView.setAdapter(adapter);

        Button buttonOk = (Button)findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAccountsSelected(adapter.getSelectedItems());
                dismiss();
            }
        });

        Button buttonCancel = (Button)findViewById(R.id.button_cancel);
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
