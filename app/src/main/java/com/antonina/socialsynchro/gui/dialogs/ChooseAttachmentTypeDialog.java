package com.antonina.socialsynchro.gui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.content.attachments.AttachmentType;
import com.antonina.socialsynchro.gui.adapters.AttachmentTypeDialogAdapter;
import com.antonina.socialsynchro.gui.listeners.OnAttachmentTypeSelectedListener;

public class ChooseAttachmentTypeDialog extends Dialog {
    private final AppCompatActivity context;
    private final OnAttachmentTypeSelectedListener listener;

    public ChooseAttachmentTypeDialog(@NonNull AppCompatActivity context, OnAttachmentTypeSelectedListener listener) {
        super(context, R.style.DialogStyle);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.dialog_choose_attachment_type);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_choose_attachment_type);

        RecyclerView recyclerView = findViewById(R.id.recyclerview_attachment_types_dialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        AttachmentTypeDialogAdapter adapter = new AttachmentTypeDialogAdapter(context, new OnAttachmentTypeSelectedListener() {
            @Override
            public void onAttachmentTypeSelected(AttachmentType attachmentType) {
                listener.onAttachmentTypeSelected(attachmentType);
                dismiss();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
