package com.antonina.socialsynchro.common.gui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
        public final ImageView imageView;

        public ChildViewHolder(@NonNull View view, AppCompatActivity context) {
            super(view);

            imageView = view.findViewById(R.id.imageview_profile_picture);

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
        loadPicture(viewHolder.imageView, imageSize, item.getAccount().getProfilePictureURL());
    }

    @Override
    protected ChildViewHolder createViewHolder(View view) {
        return new ChildViewHolder(view, context);
    }

    @Override
    public void loadData() {
        items = parent.getChildren();
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
