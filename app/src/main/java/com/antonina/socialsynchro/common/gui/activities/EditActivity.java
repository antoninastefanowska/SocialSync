package com.antonina.socialsynchro.common.gui.activities;

import android.Manifest;
import android.content.Context;
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
import android.view.View;
import android.widget.Toast;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.content.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.content.posts.ChildPostContainerFactory;
import com.antonina.socialsynchro.common.content.attachments.Attachment;
import com.antonina.socialsynchro.common.content.posts.PostContainer;
import com.antonina.socialsynchro.common.gui.adapters.PostEditAdapter;
import com.antonina.socialsynchro.common.gui.listeners.OnAttachmentUploadedListener;
import com.antonina.socialsynchro.common.gui.listeners.OnPublishedListener;
import com.antonina.socialsynchro.common.content.posts.ParentPostContainer;
import com.antonina.socialsynchro.common.content.attachments.AttachmentType;
import com.antonina.socialsynchro.databinding.ActivityEditBinding;
import com.antonina.socialsynchro.common.gui.dialogs.ChooseAccountDialog;
import com.antonina.socialsynchro.common.gui.listeners.OnAccountsSelectedListener;
import com.antonina.socialsynchro.common.gui.dialogs.ChooseAttachmentTypeDialog;
import com.antonina.socialsynchro.common.gui.listeners.OnAttachmentTypeSelectedListener;
import com.antonina.socialsynchro.common.gui.other.SerializableList;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class EditActivity extends AppCompatActivity {
    private final static int ADD_ATTACHMENTS = 0;
    private final static int REQUEST_STORAGE_ACCESS = 0;

    private List<Account> selectedAccounts;

    private PostEditAdapter postAdapter;
    private PostEditAdapter.PostViewHolder activeViewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        ParentPostContainer parent;
        if (getIntent().hasExtra("parent"))
            parent = (ParentPostContainer)getIntent().getSerializableExtra("parent");
        else
            parent = new ParentPostContainer();

        ActivityEditBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_edit);

        RecyclerView postRecyclerView = findViewById(R.id.recyclerview_post);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostEditAdapter(this, parent, postRecyclerView);

        binding.setPostAdapter(postAdapter);
        binding.executePendingBindings();
    }

    public void addChild() {
        ChooseAccountDialog dialog = new ChooseAccountDialog(this, new OnAccountsSelectedListener() {
            @Override
            public void onAccountsSelected(List<Account> accounts) {
                selectedAccounts = accounts;
                for (Account account : selectedAccounts) {
                    ChildPostContainerFactory factory = ChildPostContainerFactory.getInstance();
                    ChildPostContainer child = factory.createNew(account);
                    postAdapter.addItem(child);
                }
                selectedAccounts = null;
            }
        });

        List<Account> usedAccounts = new ArrayList<>();
        for (ChildPostContainer child : postAdapter.getChildren())
            usedAccounts.add(child.getAccount());

        dialog.setIgnoredData(usedAccounts);
        dialog.show();
    }

    public void publish(final PostContainer postContainer) {
        final Context context = this;
        final View layout = findViewById(R.id.layout_main);
        postContainer.publish(new OnPublishedListener() {
            @Override
            public void onPublished(ChildPostContainer publishedPost) {
                postAdapter.updateItemView(publishedPost);
                Snackbar snackbar = Snackbar.make(layout, "Succesfully published: " + publishedPost.getExternalID(), Snackbar.LENGTH_LONG);
                snackbar.show();
                if (postContainer.isParent())
                    exitAndSave((ParentPostContainer)postContainer);
            }

            @Override
            public void onError(ChildPostContainer post, String error) {
                postAdapter.updateItemView(post);
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
            public void onProgress(Attachment attachment) {
                Toast toast = Toast.makeText(context, "Attachment upload progress: " + attachment.getUploadProgress() + "%", Toast.LENGTH_LONG);
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
        postAdapter.notifyDataSetChanged();
    }

    public void exitAndSave(ParentPostContainer parent) {
        Intent mainActivity = new Intent();
        mainActivity.putExtra("parent", parent);
        setResult(RESULT_OK, mainActivity);
        finish();
    }

    public void addAttachment(PostEditAdapter.PostViewHolder viewHolder) {
        activeViewHolder = viewHolder;
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

                    for (Attachment attachment : attachments)
                        activeViewHolder.attachmentAdapter.addItem(attachment);

                    postAdapter.notifyItemChanged(activeViewHolder.getAdapterPosition());
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
