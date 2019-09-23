package com.antonina.socialsynchro.gui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import com.antonina.socialsynchro.content.IPost;
import com.antonina.socialsynchro.content.attachments.Attachment;
import com.antonina.socialsynchro.gui.listeners.OnAttachmentUploadedListener;
import com.antonina.socialsynchro.gui.listeners.OnPublishedListener;
import com.antonina.socialsynchro.content.ParentPostContainer;
import com.antonina.socialsynchro.content.attachments.AttachmentType;
import com.antonina.socialsynchro.databinding.ActivityEditBinding;
import com.antonina.socialsynchro.gui.adapters.AttachmentEditAdapter;
import com.antonina.socialsynchro.gui.adapters.ChildEditAdapter;
import com.antonina.socialsynchro.gui.dialogs.ChooseAccountDialog;
import com.antonina.socialsynchro.gui.listeners.OnAccountsSelectedListener;
import com.antonina.socialsynchro.gui.dialogs.ChooseAttachmentTypeDialog;
import com.antonina.socialsynchro.gui.listeners.OnAttachmentTypeSelectedListener;
import com.antonina.socialsynchro.gui.serialization.SerializableList;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class EditActivity extends AppCompatActivity {
    private final static int ADD_ATTACHMENTS = 0;
    private final static int REQUEST_STORAGE_ACCESS = 0;

    private ParentPostContainer parent;
    private List<Account> selectedAccounts;
    private ChildEditAdapter childAdapter;
    private AttachmentEditAdapter parentAttachmentAdapter;
    private IPost activePostContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("activePostContainer"))
                activePostContainer = (IPost)savedInstanceState.getSerializable("activePostContainer");
        }

        if (getIntent().hasExtra("parent"))
            parent = (ParentPostContainer)getIntent().getSerializableExtra("parent");
        else
            parent = new ParentPostContainer();

        ActivityEditBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_edit);
        binding.setParent(parent);

        RecyclerView childRecyclerView = findViewById(R.id.recyclerview_children);
        childRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        childAdapter = new ChildEditAdapter(this, parent);

        RecyclerView parentAttachmentRecyclerView = findViewById(R.id.recyclerview_parent_attachments);
        parentAttachmentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        parentAttachmentAdapter = new AttachmentEditAdapter(this, parent);

        binding.setChildAdapter(childAdapter);
        binding.setAttachmentAdapter(parentAttachmentAdapter);
        binding.executePendingBindings();

        EditText parentContent = findViewById(R.id.edittext_parent_content);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("activePostContainer", activePostContainer);
        super.onSaveInstanceState(outState);
    }

    public void buttonAddChild_onClick(View view) {
        ChooseAccountDialog dialog = new ChooseAccountDialog(this, new OnAccountsSelectedListener() {
            @Override
            public void onAccountsSelected(List<Account> accounts) {
                selectedAccounts = accounts;
                for (Account account : selectedAccounts) {
                    ChildPostContainerFactory factory = ChildPostContainerFactory.getInstance();
                    ChildPostContainer child = factory.createNew(account);
                    childAdapter.addItem(child);
                }
                selectedAccounts = null;
            }
        });

        List<Account> usedAccounts = new ArrayList<>();
        for (ChildPostContainer child : parent.getChildren())
            usedAccounts.add(child.getAccount());

        dialog.setIgnoredData(usedAccounts);
        dialog.show();
    }

    public void buttonSave_onClick(View view) {
        exitAndSave();
    }

    public void buttonPublish_onClick(View view) {
        final Activity context = this;
        final View layout = findViewById(R.id.layout_main);
        parent.publish(new OnPublishedListener() {
            @Override
            public void onPublished(ChildPostContainer publishedPost) {
                childAdapter.updateItemView(publishedPost);
                Snackbar snackbar = Snackbar.make(layout, "Succesfully published: " + publishedPost.getExternalID(), Snackbar.LENGTH_LONG);
                snackbar.show();
                exitAndSave();
            }

            @Override
            public void onError(ChildPostContainer post, String error) {
                childAdapter.updateItemView(post);
                Snackbar snackbar = Snackbar.make(layout, "Failed to publish. Error: " + error, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }, new OnAttachmentUploadedListener() {
            @Override
            public void onInitialized(Attachment attachment) {
                Toast toast = Toast.makeText(context, "Attachment initialized: " + attachment.getFile().getName(), Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onProgress(Attachment attachment, int percentProgress) {
                Toast toast = Toast.makeText(context, "Attachment upload progress: " + percentProgress + "%", Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onFinished(Attachment attachment) {
                Toast toast = Toast.makeText(context, "Attachment upload finished: " + attachment.getExternalID(), Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onError(Attachment attachment, String error) {
                Toast toast = Toast.makeText(context, "Attachment upload failed: " + attachment.getFile().getName(), Toast.LENGTH_LONG);
                toast.show();
            }
        });
        childAdapter.notifyDataSetChanged();
    }

    private void exitAndSave() {
        Intent mainActivity = new Intent();
        mainActivity.putExtra("parent", parent);
        setResult(RESULT_OK, mainActivity);
        finish();
    }

    public void buttonParentAddAttachment_onClick(View view) {
        activePostContainer = parent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, REQUEST_STORAGE_ACCESS);
            }
            else
                chooseAttachmentType();
        }
        else
            chooseAttachmentType();
    }

    private void chooseAttachmentType() {
        ChooseAttachmentTypeDialog dialog = new ChooseAttachmentTypeDialog(this, new OnAttachmentTypeSelectedListener() {
            @Override
            public void onAttachmentTypeSelected(AttachmentType attachmentType) {
                Intent addAttachments = new Intent(EditActivity.this, attachmentType.getGalleryActivityClass());
                startActivityForResult(addAttachments, ADD_ATTACHMENTS);
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_ATTACHMENTS:
                    SerializableList<Attachment> serializableAttachments = (SerializableList<Attachment>)data.getSerializableExtra("attachments");
                    List<Attachment> attachments = serializableAttachments.getList();

                    for (Attachment attachment : attachments) {
                        if (activePostContainer == parent)
                            parentAttachmentAdapter.addItem(attachment);
                        else {
                            // TODO
                        }
                    }
                    if (activePostContainer == parent)
                        childAdapter.notifyDataSetChanged();
                    else {
                        ChildPostContainer activeChild = (ChildPostContainer)activePostContainer;
                        childAdapter.updateItemView(activeChild);
                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_ACCESS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    chooseAttachmentType();
                else {
                    Toast toast = Toast.makeText(this, getResources().getString(R.string.error_storage_permissions), Toast.LENGTH_LONG);
                    toast.show();
                }
        }
    }
}
