package com.antonina.socialsynchro.gui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.content.IPost;
import com.antonina.socialsynchro.content.attachments.Attachment;
import com.antonina.socialsynchro.databinding.AttachmentEditItemBinding;

public class AttachmentEditAdapter extends BaseAdapter<Attachment, AttachmentEditAdapter.AttachmentViewHolder> {
    private IPost postContainer;

    public static class AttachmentViewHolder extends BaseAdapter.BaseViewHolder<AttachmentEditItemBinding> {

        public AttachmentViewHolder(@NonNull View view) {
            super(view);
        }

        @Override
        protected AttachmentEditItemBinding getBinding(View view) {
            return AttachmentEditItemBinding.bind(view);
        }
    }

    public AttachmentEditAdapter(AppCompatActivity context) {
        super(context);
    }

    public AttachmentEditAdapter(AppCompatActivity context, IPost postContainer) {
        super(context);
        this.postContainer = postContainer;
        loadData();
    }

    @Override
    protected int getItemLayout() {
        return R.layout.attachment_edit_item;
    }

    @Override
    protected void setItemBinding(AttachmentViewHolder viewHolder, Attachment item) {
        viewHolder.binding.setAttachment(item);
    }

    @Override
    protected AttachmentViewHolder createViewHolder(View view) {
        return new AttachmentViewHolder(view);
    }

    @Override
    public void loadData() {
        if (postContainer != null)
            items = postContainer.getAttachments();
        notifyDataSetChanged();
    }

    @Override
    public void addItem(Attachment item) {
        postContainer.addAttachment(item);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public void removeItem(Attachment item) {
        int position = getItemPosition(item);
        postContainer.removeAttachment(item);
        notifyItemRemoved(position);
    }

    public void setSource(IPost postContainer) {
        this.postContainer = postContainer;
        loadData();
    }
}
