package com.antonina.socialsynchro.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.controllers.TwitterController;
import com.antonina.socialsynchro.posts.ChildPostContainer;
import com.antonina.socialsynchro.posts.IPost;
import com.antonina.socialsynchro.posts.ParentPostContainer;
import com.antonina.socialsynchro.posts.Post;
import com.antonina.socialsynchro.posts.TwitterPostContainer;

public class MainActivity extends AppCompatActivity {
    ParentPostContainer parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void btSend_onClick(View view) {
        EditText etContent = (EditText)findViewById(R.id.etContent);

        parent = new ParentPostContainer();
        parent.setContent(etContent.getText().toString());
        TwitterPostContainer child = new TwitterPostContainer(parent);
        parent.publish();

        etContent.getText().clear();
    }

    protected void btRemove_onClick(View view) {
        if (parent != null) {
            parent.remove();
        }
    }
}
