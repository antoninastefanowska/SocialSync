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
import android.widget.ImageView;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.gui.activities.EditActivity;
import com.antonina.socialsynchro.common.gui.adapters.AttachmentEditAdapter;
import com.antonina.socialsynchro.common.gui.adapters.OperationAdapter;
import com.antonina.socialsynchro.common.gui.adapters.TagEditAdapter;
import com.antonina.socialsynchro.common.gui.operations.Operation;
import com.antonina.socialsynchro.common.gui.operations.OperationID;
import com.antonina.socialsynchro.common.gui.other.CustomViewTransformer;
import com.antonina.socialsynchro.common.gui.other.MaskTransformation;
import com.antonina.socialsynchro.common.model.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.model.posts.Tag;
import com.antonina.socialsynchro.databinding.FragmentChildPostBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.gtomato.android.ui.widget.CarouselView;

public class ChildPostFragment extends Fragment {
    private ChildPostContainer child;
    private FragmentChildPostBinding binding;
    private AttachmentEditAdapter attachmentAdapter;
    private TagEditAdapter tagAdapter;
    private OperationAdapter operationAdapter;

    public ChildPostFragment() { }

    public static ChildPostFragment newInstance(ChildPostContainer child) {
        ChildPostFragment fragment = new ChildPostFragment();
        Bundle args = new Bundle();
        args.putSerializable("child", child);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            child = (ChildPostContainer)getArguments().getSerializable("child");
            child.show();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_child_post, container, false);
        View view = binding.getRoot();
        AppCompatActivity context = (AppCompatActivity)getActivity();
        final EditActivity activity = (EditActivity)context;
        binding.setLifecycleOwner(context);

        operationAdapter = new OperationAdapter(OperationAdapter.EDIT);
        attachmentAdapter = new AttachmentEditAdapter(context);
        tagAdapter = new TagEditAdapter(context);

        operationAdapter.setSource(child);
        attachmentAdapter.setSource(child);
        tagAdapter.setSource(child);

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

        Operation operation = operationAdapter.getItem(OperationID.LOCK);
        operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                child.lock();
            }
        });
        operation = operationAdapter.getItem(OperationID.UNLOCK);
        operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                child.unlock(true);
            }
        });
        operation = operationAdapter.getItem(OperationID.DELETE);
        operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.removeChild(child);
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
                activity.publishPost(child);
            }
        });
        operation = operationAdapter.getItem(OperationID.UNPUBLISH);
        operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.unpublishPost(child);
            }
        });

        ImageView profilePictureView = view.findViewById(R.id.imageview_profile_picture);
        ImageView serviceIconView = view.findViewById(R.id.imageview_icon_picture);
        int imageSize = getResources().getDimensionPixelSize(R.dimen.profile_picture_size);
        RequestOptions options = new RequestOptions()
                .override(imageSize)
                .fitCenter()
                .transform(new MaskTransformation(context));
        Glide.with(context)
                .load(child.getAccount().getProfilePictureURL())
                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(profilePictureView);
        Glide.with(context)
                .load(child.getAccount().getService().getIconID())
                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(serviceIconView);

        binding.setChild(child);
        binding.executePendingBindings();
        return view;
    }

    private void refresh() {
        binding.setChild(child);
        binding.executePendingBindings();
        attachmentAdapter.setSource(child);
        tagAdapter.setSource(child);
        operationAdapter.setSource(child);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (child.isLocked())
            refresh();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && child != null && child.isLocked())
            refresh();
    }
}
