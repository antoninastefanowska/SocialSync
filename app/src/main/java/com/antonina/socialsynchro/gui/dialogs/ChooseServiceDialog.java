package com.antonina.socialsynchro.gui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.gui.adapters.ServiceDialogAdapter;
import com.antonina.socialsynchro.gui.listeners.OnServiceSelectedListener;
import com.antonina.socialsynchro.services.Service;

public class ChooseServiceDialog extends Dialog {
    private final AppCompatActivity context;
    private final OnServiceSelectedListener listener;

    public ChooseServiceDialog(@NonNull AppCompatActivity context, OnServiceSelectedListener listener) {
        super(context, R.style.DialogStyle);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.dialog_choose_service);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_choose_service);

        RecyclerView recyclerView = findViewById(R.id.recyclerview_services_dialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        ServiceDialogAdapter adapter = new ServiceDialogAdapter(context, new OnServiceSelectedListener() {
            @Override
            public void onServiceSelected(Service service) {
                listener.onServiceSelected(service);
                dismiss();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
