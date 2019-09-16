package com.antonina.socialsynchro.gui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.content.attachments.AttachmentType;
import com.antonina.socialsynchro.gui.adapters.AttachmentTypeDialogAdapter;

public class ChooseAttachmentTypeDialog extends Dialog {
    private Activity context;
    private ChooseAttachmentTypeDialogListener listener;

    public ChooseAttachmentTypeDialog(@NonNull Activity context, ChooseAttachmentTypeDialogListener listener) {
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

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview_attachment_types_dialog);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        AttachmentTypeDialogAdapter adapter = new AttachmentTypeDialogAdapter(new ChooseAttachmentTypeDialogListener() {
            @Override
            public void onAttachmentTypeSelected(AttachmentType attachmentType) {
                listener.onAttachmentTypeSelected(attachmentType);
                dismiss();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
