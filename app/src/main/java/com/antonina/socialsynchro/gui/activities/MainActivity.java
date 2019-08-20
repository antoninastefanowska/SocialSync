package com.antonina.socialsynchro.gui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.antonina.socialsynchro.R;

public class MainActivity extends AppCompatActivity {
    //private TwitterController twitterController;
    //private ParentPostContainer parent;
    //private Account account;
    //private TwitterAccessTokenResponse twitterAccessTokenResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TwitterExampleRequest request = TwitterExampleRequest.builder().build();
        //Log.d("konto", request.getAuthorizationHeader());
    }

    protected void btSend_onClick(View view) {
        /*
        EditText etContent = (EditText)findViewById(R.id.etContent);

        account.setAccessToken(twitterController.getTwitterAccessTokenResponse().getAccessToken());
        account.setSecretToken(twitterController.getTwitterAccessTokenResponse().getSecretToken());

        parent = new ParentPostContainer();
        parent.setContent(etContent.getText().toString());
        TwitterPostContainer child = new TwitterPostContainer(parent);
        child.setAccount(account);
        parent.publish();

        etContent.getText().clear(); */
    }

    protected void btRemove_onClick(View view) {
        /*
        if (parent != null) {
            parent.remove();
        } */
    }

    protected void btLogin_onClick(View view) {
        /*
        account = new TwitterAccount();

        twitterController = TwitterController.getInstance();
        twitterController.requestGetLoginToken(); */
    }

    protected void btConfirmLogin_onClick(View view) {
        /*
        TwitterLoginTokenResponse twitterLoginTokenResponse = twitterController.getTwitterLoginTokenResponse();
        CallbackController callbackController = new CallbackController();
        callbackController.requestGetVerifier(twitterLoginTokenResponse.getLoginToken(), twitterLoginTokenResponse.getLoginSecretToken());
        twitterAccessTokenResponse = twitterController.getTwitterAccessTokenResponse(); */
    }

    public void btAccounts_onClick(View view) {
        Intent accountsActivity = new Intent(MainActivity.this, AccountsActivity.class);
        startActivity(accountsActivity);
    }
}
