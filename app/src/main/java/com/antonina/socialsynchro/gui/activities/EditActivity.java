package com.antonina.socialsynchro.gui.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.content.ChildPostContainer;
import com.antonina.socialsynchro.content.ChildPostContainerFactory;
import com.antonina.socialsynchro.content.OnPublishedListener;
import com.antonina.socialsynchro.content.ParentPostContainer;
import com.antonina.socialsynchro.databinding.ActivityEditBinding;
import com.antonina.socialsynchro.gui.adapters.ChildEditAdapter;
import com.antonina.socialsynchro.gui.dialogs.ChooseAccountDialog;
import com.antonina.socialsynchro.gui.dialogs.ChooseAccountDialogListener;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {
    private ParentPostContainer parent;
    private List<Account> selectedAccounts;
    private ActivityEditBinding binding;
    private RecyclerView childRecyclerView;
    private ChildEditAdapter childAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        if (getIntent().hasExtra("parent"))
            parent = (ParentPostContainer)getIntent().getSerializableExtra("parent");
        else
            parent = new ParentPostContainer();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit);
        binding.setParent(parent);

        childRecyclerView = (RecyclerView)findViewById(R.id.rvChildren);
        childRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        childAdapter = new ChildEditAdapter(parent.getChildren());

        binding.setChildAdapter(childAdapter);
        binding.executePendingBindings();

        EditText parentContent = (EditText)findViewById(R.id.etParentContent);
        parentContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                childAdapter.notifyDataSetChanged();
            }
        });
    }

    public void btAddChild_onClick(View view) {
        ChooseAccountDialog dialog = new ChooseAccountDialog(this);
        dialog.setListener(new ChooseAccountDialogListener() {
            @Override
            public void onAccountsSelected(List<Account> accounts) {
                selectedAccounts = accounts;
                for (Account account : selectedAccounts) {
                    ChildPostContainerFactory factory = ChildPostContainerFactory.getInstance();
                    ChildPostContainer child = factory.createNew(parent, account);
                }
                childAdapter.setData(parent.getChildren());
                binding.setChildAdapter(childAdapter); // ?
                binding.executePendingBindings(); // ?
                selectedAccounts = null;
            }
        });

        List<Account> usedAccounts = new ArrayList<Account>();
        for (ChildPostContainer child : parent.getChildren())
            usedAccounts.add(child.getAccount());

        dialog.setIgnoredData(usedAccounts);
        dialog.show();
    }

    public void btSave_onClick(View view) {
        exitActivity();
    }

    public void btPublish_onClick(View view) {
        final Activity context = this;
        parent.publish(new OnPublishedListener() {
            @Override
            public void onPublished(ChildPostContainer publishedPost, String error) {
                int position = childAdapter.getItemPosition(publishedPost);
                childAdapter.notifyItemChanged(position);
                Toast toast = Toast.makeText(context, "Successfily published: " + publishedPost.getExternalID(), Toast.LENGTH_SHORT);
                toast.show();
                exitActivity();
            }
        });
        childAdapter.notifyDataSetChanged();
    }

    private void exitActivity() {
        Intent mainActivity = new Intent();
        mainActivity.putExtra("parent", parent);
        setResult(RESULT_OK, mainActivity);
        finish();
    }

    public void etParentContent_onTextChanged(View view) {
        childAdapter.notifyDataSetChanged();
    }
}
