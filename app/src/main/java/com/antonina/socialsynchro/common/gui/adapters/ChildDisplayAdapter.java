package com.antonina.socialsynchro.common.gui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.content.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.content.posts.ParentPostContainer;
import com.antonina.socialsynchro.databinding.ChildDisplayItemBinding;

@SuppressWarnings("WeakerAccess")
public class ChildDisplayAdapter extends BaseAdapter<ChildPostContainer, ChildDisplayAdapter.ChildViewHolder> {
    private ParentPostContainer parent;
    private int imageSize;

    protected static class ChildViewHolder extends BaseAdapter.BaseViewHolder<ChildDisplayItemBinding> {
        public final AttachmentDisplayAdapter attachmentAdapter;
        public final ImageView profilePictureImageView;
        public final ImageView serviceIconImageView;

        public ChildViewHolder(@NonNull View view, AppCompatActivity context) {
            super(view);

            profilePictureImageView = view.findViewById(R.id.imageview_profile_picture);
            serviceIconImageView = view.findViewById(R.id.imageview_icon_picture);

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
        imageSize = getPictureSize();
    }

    public ChildDisplayAdapter(AppCompatActivity context, ParentPostContainer parent) {
        super(context);
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
        loadPictureByURL(viewHolder.profilePictureImageView, imageSize, item.getAccount().getProfilePictureURL());
        loadPictureByID(viewHolder.serviceIconImageView, imageSize, item.getAccount().getService().getIconID());
    }

    @Override
    protected ChildViewHolder createViewHolder(View view) {
        return new ChildViewHolder(view, context);
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        final ChildViewHolder viewHolder = super.onCreateViewHolder(parent, position);
        viewHolder.profilePictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                ChildPostContainer item = getItem(position);
                item.switchVisibility();
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
