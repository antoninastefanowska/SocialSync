package com.antonina.socialsynchro.gui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.gui.adapters.AccountDialogAdapter;

public class ChooseAccountDialog extends Dialog {
    private Activity context;
    private AccountDialogAdapter adapter;
    private RecyclerView recyclerView;
    private ChooseAccountDialogListener listener;

    public ChooseAccountDialog(@NonNull Activity context) {
        super(context, R.style.DialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.dialog_choose_account);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_choose_account);

        recyclerView = (RecyclerView)findViewById(R.id.rvAccountsDialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new AccountDialogAdapter();
        recyclerView.setAdapter(adapter);

        Button btOk = (Button)findViewById(R.id.btOk);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btOk_onClick(v);
            }
        });

        Button btCancel = (Button)findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btCancel_onClick(v);
            }
        });
    }

    public void setListener(ChooseAccountDialogListener listener) {
        this.listener = listener;
    }

    public void btCancel_onClick(View view) {
        cancel();
        dismiss();
    }

    public void btOk_onClick(View view) {
        listener.onAccountsSelected(adapter.getCheckedItems());
        dismiss();
    }

    public void itemAccount_onClick(View view) {
        LinearLayout accountContainer = (LinearLayout)view;
        AccountDialogAdapter.AccountViewHolder viewHolder = (AccountDialogAdapter.AccountViewHolder)recyclerView.getChildViewHolder(accountContainer);
        int position = viewHolder.getAdapterPosition();
        adapter.selectItem(position);
    }
}
