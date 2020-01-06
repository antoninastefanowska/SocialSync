package com.antonina.socialsynchro.common.gui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.content.posts.IPost;
import com.antonina.socialsynchro.common.content.attachments.Attachment;
import com.antonina.socialsynchro.databinding.AttachmentEditItemBinding;

import java.util.ArrayList;

@SuppressWarnings("WeakerAccess")
public class AttachmentEditAdapter extends BaseAdapter<Attachment, AttachmentEditAdapter.AttachmentViewHolder> {
    private IPost postContainer;
    private boolean locked;

    protected static class AttachmentViewHolder extends BaseAdapter.BaseViewHolder<AttachmentEditItemBinding> {
        public Button removeButton;

        public AttachmentViewHolder(@NonNull View view) {
            super(view);

            removeButton = view.findViewById(R.id.button_remove);
        }

        @Override
        protected AttachmentEditItemBinding getBinding(View view) {
            return AttachmentEditItemBinding.bind(view);
        }
    }

    public AttachmentEditAdapter(AppCompatActivity context) {
        super(context);
        locked = false;
        items = new ArrayList();
    }

    public AttachmentEditAdapter(AppCompatActivity context, IPost postContainer) {
        super(context);
        this.postContainer = postContainer;
        locked = false;
        items = new ArrayList();
        loadData();
    }

    @Override
    protected int getItemLayout() {
        return R.layout.attachment_edit_item;
    }

    @Override
    protected void setItemBinding(AttachmentViewHolder viewHolder, Attachment item) {
        viewHolder.binding.setAttachment(item);
        viewHolder.binding.setLocked(locked);
    }

    @Override
    protected AttachmentViewHolder createViewHolder(View view) {
        return new AttachmentViewHolder(view);
    }

    @Override
    public void loadData() {
        items.clear();
        if (postContainer != null)
            items.addAll(postContainer.getAttachments());
        notifyDataSetChanged();
    }

    @Override
    public void addItem(Attachment item) {
        postContainer.addAttachment(item);
        items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public void removeItem(Attachment item) {
        int position = getItemPosition(item);
        postContainer.removeAttachment(item);
        items.remove(item);
        notifyItemRemoved(position);
    }

    public void setSource(IPost postContainer) {
        this.postContainer = postContainer;
        loadData();
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public AttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final AttachmentViewHolder viewHolder = super.onCreateViewHolder(viewGroup, viewType);
        viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Attachment item = getItem(position);
                removeItem(item);
            }
        });
        return viewHolder;
    }
}
