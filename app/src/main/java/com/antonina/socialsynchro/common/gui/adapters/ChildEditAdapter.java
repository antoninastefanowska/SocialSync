package com.antonina.socialsynchro.common.gui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.content.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.content.posts.ParentPostContainer;
import com.antonina.socialsynchro.databinding.ChildEditItemBinding;

@SuppressWarnings("WeakerAccess")
public class ChildEditAdapter extends BaseAdapter<ChildPostContainer, ChildEditAdapter.ChildViewHolder> {
    private ParentPostContainer parent;

    protected static class ChildViewHolder extends BaseAdapter.BaseViewHolder<ChildEditItemBinding> {
        public final AttachmentEditAdapter attachmentAdapter;

        public ChildViewHolder(@NonNull View view, AppCompatActivity context) {
            super(view);

            RecyclerView attachmentRecyclerView = view.findViewById(R.id.recyclerview_child_attachments);
            attachmentRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

            attachmentAdapter = new AttachmentEditAdapter(context);
            binding.setAttachmentAdapter(attachmentAdapter);
            binding.executePendingBindings();
        }

        @Override
        protected ChildEditItemBinding getBinding(View view) {
            return ChildEditItemBinding.bind(view);
        }
    }

    public ChildEditAdapter(AppCompatActivity context) {
        super(context);
    }

    public ChildEditAdapter(AppCompatActivity context, ParentPostContainer parent) {
        super(context);
        this.parent = parent;
        loadData();
    }

    @Override
    protected int getItemLayout() {
        return R.layout.child_edit_item;
    }

    @Override
    protected void setItemBinding(ChildViewHolder viewHolder, ChildPostContainer item) {
        viewHolder.binding.setChild(item);
        viewHolder.attachmentAdapter.setSource(item);
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
