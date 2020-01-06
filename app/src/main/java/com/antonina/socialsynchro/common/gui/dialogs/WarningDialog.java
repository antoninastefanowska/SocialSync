package com.antonina.socialsynchro.common.gui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.antonina.socialsynchro.R;

public class WarningDialog extends Dialog {
    private String message;

    public WarningDialog(@NonNull AppCompatActivity context, String message) {
        super(context, R.style.DialogStyle);
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.dialog_warning);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_warning);

        Button buttonOk = findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        TextView textViewMessage = findViewById(R.id.textview_message);
        textViewMessage.setText(message);
    }
}
