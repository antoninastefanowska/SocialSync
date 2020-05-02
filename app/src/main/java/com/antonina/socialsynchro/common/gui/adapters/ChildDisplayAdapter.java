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
import com.antonina.socialsynchro.common.gui.operations.Operation;
import com.antonina.socialsynchro.common.gui.operations.OperationID;
import com.antonina.socialsynchro.common.gui.other.CustomViewTransformer;
import com.antonina.socialsynchro.common.gui.other.GrayscaleTransformation;
import com.antonina.socialsynchro.common.gui.other.MaskTransformation;
import com.antonina.socialsynchro.databinding.ChildDisplayItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.gtomato.android.ui.widget.CarouselView;

@SuppressWarnings("WeakerAccess")
public class ChildDisplayAdapter extends BaseAdapter<ChildPostContainer, ChildDisplayAdapter.ChildViewHolder> {
    private ParentPostContainer parent;
    private int imageSize;
    private MainActivity activity;

    protected static class ChildViewHolder extends BaseAdapter.BaseViewHolder<ChildDisplayItemBinding> {
        public final AttachmentDisplayAdapter attachmentAdapter;
        public final OperationAdapter operationAdapter;
        public final ImageView profilePictureImageView;
        public final ImageView serviceIconImageView;
        public final CarouselView operationMenu;

        public ChildViewHolder(@NonNull View view, AppCompatActivity context) {
            super(view);

            profilePictureImageView = view.findViewById(R.id.imageview_profile_picture);
            serviceIconImageView = view.findViewById(R.id.imageview_icon_picture);
            operationMenu = view.findViewById(R.id.operation_menu);

            RecyclerView attachmentRecyclerView = view.findViewById(R.id.recyclerview_child_attachments);
            attachmentRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

            attachmentAdapter = new AttachmentDisplayAdapter(context);
            binding.setAttachmentAdapter(attachmentAdapter);
            binding.executePendingBindings();

            operationAdapter = new OperationAdapter(OperationAdapter.DISPLAY);
            operationMenu.setTransformer(new CustomViewTransformer());
            operationMenu.setInfinite(true);
            operationMenu.setAdapter(operationAdapter);
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
    protected void setItemBinding(final ChildViewHolder viewHolder, ChildPostContainer item) {
        viewHolder.binding.setChild(item);
        viewHolder.attachmentAdapter.setSource(item);
        viewHolder.operationAdapter.setSource(item);

        Operation operation = viewHolder.operationAdapter.getItem(OperationID.SYNCHRONIZE);
        operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                ChildPostContainer item = getItem(position);
                activity.synchronizePost(item);
            }
        });
        operation = viewHolder.operationAdapter.getItem(OperationID.STATISTICS);
        operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                ChildPostContainer item = getItem(position);
                activity.showChildStatistics(item);
            }
        });
        operation = viewHolder.operationAdapter.getItem(OperationID.LINK);
        operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                ChildPostContainer item = getItem(position);
                activity.openChildLink(item);
            }
        });
        operation = viewHolder.operationAdapter.getItem(OperationID.PUBLISH);
        operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                ChildPostContainer item = getItem(position);
                activity.publishPost(item);
            }
        });
        operation = viewHolder.operationAdapter.getItem(OperationID.UNPUBLISH);
        operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                ChildPostContainer item = getItem(position);
                activity.unpublishPost(item);
            }
        });
        operation = viewHolder.operationAdapter.getItem(OperationID.DELETE);
        operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                ChildPostContainer item = getItem(position);
                activity.removePost(item);
                removeItem(position);
            }
        });

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
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ChildViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
        setHideable(viewHolder);
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
