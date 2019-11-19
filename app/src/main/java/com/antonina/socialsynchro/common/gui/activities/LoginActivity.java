package com.antonina.socialsynchro.common.gui.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.content.accounts.LoginFlow;
import com.antonina.socialsynchro.common.content.services.Service;
import com.antonina.socialsynchro.common.gui.other.SerializableList;
import com.antonina.socialsynchro.databinding.ActivityLoginBinding;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private LoginFlow loginFlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Service service = (Service)getIntent().getSerializableExtra("service");
        loginFlow = service.createLoginFlow(this);

        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setService(service);
        binding.executePendingBindings();
    }

    public void buttonConnect_onClick(View view) {
        loginFlow.signIn();
    }

    public void buttonConfirm_onClick(View view) {
        loginFlow.confirm();
    }

    public void exitAndSave(List<Account> accounts) {
        SerializableList<Account> serializableAccounts = new SerializableList<>(accounts);

        Intent accountsActivity = new Intent();
        accountsActivity.putExtra("accounts", serializableAccounts);
        setResult(RESULT_OK, accountsActivity);
        finish();
    }
}
