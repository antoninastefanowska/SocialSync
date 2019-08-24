package com.antonina.socialsynchro.gui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.antonina.socialsynchro.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btAccounts_onClick(View view) {
        Intent accountsActivity = new Intent(MainActivity.this, AccountsActivity.class);
        startActivity(accountsActivity);
    }

    public void btCreateContent_onClick(View view) {
        Intent editActivity = new Intent(MainActivity.this, EditActivity.class);
        startActivity(editActivity);
    }
}
