package com.antonina.socialsynchro.common.gui.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.gui.activities.EditActivity;
import com.antonina.socialsynchro.common.gui.adapters.AttachmentEditAdapter;
import com.antonina.socialsynchro.common.gui.adapters.OperationAdapter;
import com.antonina.socialsynchro.common.gui.adapters.TagEditAdapter;
import com.antonina.socialsynchro.common.gui.operations.Operation;
import com.antonina.socialsynchro.common.gui.operations.OperationID;
import com.antonina.socialsynchro.common.gui.other.CustomViewTransformer;
import com.antonina.socialsynchro.common.model.posts.ParentPostContainer;
import com.antonina.socialsynchro.common.model.posts.Tag;
import com.antonina.socialsynchro.databinding.FragmentParentPostBinding;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.gtomato.android.ui.widget.CarouselView;

public class ParentPostFragment extends Fragment {
    private ParentPostContainer parent;
    private AttachmentEditAdapter attachmentAdapter;
    private TagEditAdapter tagAdapter;

    public ParentPostFragment() { }

    public static ParentPostFragment newInstance(ParentPostContainer post) {
        ParentPostFragment fragment = new ParentPostFragment();
        Bundle args = new Bundle();
        args.putSerializable("parent", post);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parent = (ParentPostContainer)getArguments().getSerializable("parent");
            parent.show();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentParentPostBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_parent_post, container, false);
        View view = binding.getRoot();
        AppCompatActivity context = (AppCompatActivity)getActivity();
        final EditActivity activity = (EditActivity)context;

        OperationAdapter operationAdapter = new OperationAdapter(OperationAdapter.EDIT);
        attachmentAdapter = new AttachmentEditAdapter(context);
        tagAdapter = new TagEditAdapter(context);

        operationAdapter.setSource(parent);
        attachmentAdapter.setSource(parent);
        tagAdapter.setSource(parent);

        CarouselView operationMenu = view.findViewById(R.id.operation_menu);
        operationMenu.setTransformer(new CustomViewTransformer());
        operationMenu.setInfinite(true);
        operationMenu.setAdapter(operationAdapter);

        RecyclerView attachmentRecyclerView = view.findViewById(R.id.recyclerview_attachments);
        attachmentRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        attachmentRecyclerView.setAdapter(attachmentAdapter);

        RecyclerView tagRecyclerView = view.findViewById(R.id.recyclerview_tags);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        tagRecyclerView.setLayoutManager(layoutManager);
        tagRecyclerView.setAdapter(tagAdapter);

        final EditText tagsEditText = view.findViewById(R.id.edittext_tags);
        Button addTagsButton = view.findViewById(R.id.button_add_tags);
        addTagsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = tagsEditText.getText().toString();
                String[] tagStrings = text.split(" ");
                for (String tagString : tagStrings) {
                    if (!tagString.isEmpty()) {
                        Tag tag = new Tag(tagString);
                        tagAdapter.addItem(tag);
                    }
                }
                tagsEditText.getText().clear();
            }
        });

        Operation operation = operationAdapter.getItem(OperationID.SAVE);
        operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.exitAndSave(parent);
            }
        });
        operation = operationAdapter.getItem(OperationID.ADD_ATTACHMENT);
        operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.addAttachment(attachmentAdapter);
            }
        });
        operation = operationAdapter.getItem(OperationID.PUBLISH);
        operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.publishPost(parent);
            }
        });
        operation = operationAdapter.getItem(OperationID.UNPUBLISH);
        operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.unpublishPost(parent);
            }
        });

        binding.setParent(parent);
        binding.executePendingBindings();
        return view;
    }
}
