package com.antonina.socialsynchro.common.gui.adapters;

import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.content.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.content.posts.ParentPostContainer;
import com.antonina.socialsynchro.common.gui.activities.MainActivity;
import com.antonina.socialsynchro.common.gui.other.GrayscaleTransformation;
import com.antonina.socialsynchro.common.gui.other.MaskTransformation;
import com.antonina.socialsynchro.databinding.ChildDisplayItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

@SuppressWarnings("WeakerAccess")
public class ChildDisplayAdapter extends BaseAdapter<ChildPostContainer, ChildDisplayAdapter.ChildViewHolder> {
    private ParentPostContainer parent;
    private int imageSize;
    private MainActivity activity;

    protected static class ChildViewHolder extends BaseAdapter.BaseViewHolder<ChildDisplayItemBinding> {
        public final AttachmentDisplayAdapter attachmentAdapter;
        public final ImageView profilePictureImageView;
        public final ImageView serviceIconImageView;

        public final Button synchronizeButton;
        public final Button statisticsButton;
        public final Button linkButton;
        public final Button publishButton;
        public final Button unpublishButton;
        public final Button removeButton;

        public ChildViewHolder(@NonNull View view, AppCompatActivity context) {
            super(view);

            profilePictureImageView = view.findViewById(R.id.imageview_profile_picture);
            serviceIconImageView = view.findViewById(R.id.imageview_icon_picture);
            synchronizeButton = view.findViewById(R.id.button_synchronize);
            statisticsButton = view.findViewById(R.id.button_statistics);
            linkButton = view.findViewById(R.id.button_link);
            publishButton = view.findViewById(R.id.button_publish);
            unpublishButton = view.findViewById(R.id.button_unpublish);
            removeButton = view.findViewById(R.id.button_remove);

            RecyclerView attachmentRecyclerView = view.findViewById(R.id.recyclerview_child_attachments);
            attachmentRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

            attachmentAdapter = new AttachmentDisplayAdapter(context);
            binding.setAttachmentAdapter(attachmentAdapter);
            binding.executePendingBindings();
        }

        @Override
        protected ChildDisplayItemBinding getBinding(View view) {
            return ChildDisplayItemBinding.bind(view);
        }
    }

    public ChildDisplayAdapter(AppCompatActivity context) {
        super(context);
        activity = (MainActivity)context;
        imageSize = getPictureSize();
    }

    public ChildDisplayAdapter(AppCompatActivity context, ParentPostContainer parent) {
        super(context);
        activity = (MainActivity)context;
        this.parent = parent;
        imageSize = getPictureSize();
        loadData();
    }

    @Override
    protected int getItemLayout() {
        return R.layout.child_display_item;
    }

    @Override
    protected void setItemBinding(ChildViewHolder viewHolder, ChildPostContainer item) {
        viewHolder.binding.setChild(item);
        viewHolder.attachmentAdapter.setSource(item);

        Transformation<Bitmap> transformation;
        if (!item.isOnline())
            transformation = new MultiTransformation<>(new MaskTransformation(context), new GrayscaleTransformation());
        else
            transformation = new MaskTransformation(context);

        loadPictureByURL(viewHolder.profilePictureImageView, imageSize, item.getAccount().getProfilePictureURL());
        loadPictureByID(transformation, viewHolder.serviceIconImageView, imageSize, item.getAccount().getService().getIconID());
    }

    @Override
    protected ChildViewHolder createViewHolder(View view) {
        return new ChildViewHolder(view, context);
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        final ChildViewHolder viewHolder = super.onCreateViewHolder(parent, position);
        setHideable(viewHolder);
        viewHolder.synchronizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                ChildPostContainer item = getItem(position);
                activity.synchronizePost(item);
            }
        });
        viewHolder.statisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                ChildPostContainer item = getItem(position);
                activity.showChildStatistics(item);
            }
        });
        viewHolder.linkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                ChildPostContainer item = getItem(position);
                activity.openChildLink(item);
            }
        });
        viewHolder.publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                ChildPostContainer item = getItem(position);
                activity.publishPost(item);
            }
        });
        viewHolder.unpublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                ChildPostContainer item = getItem(position);
                activity.unpublishPost(item);
            }
        });
        viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                ChildPostContainer item = getItem(position);
                activity.removePost(item);
                removeItem(position);
            }
        });
        return viewHolder;
    }

    @Override
    public void loadData() {
        items = parent.getChildren();
        for (ChildPostContainer item : items)
            item.hide();
        notifyDataSetChanged();
    }

    public void setSource(ParentPostContainer parent) {
        this.parent = parent;
        loadData();
    }

    @Override
    public void addItem(ChildPostContainer item) {
        parent.addChild(item);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public void removeItem(ChildPostContainer item) {
        int position = getItemPosition(item);
        parent.removeChild(item);
        notifyItemRemoved(position);
    }
}
