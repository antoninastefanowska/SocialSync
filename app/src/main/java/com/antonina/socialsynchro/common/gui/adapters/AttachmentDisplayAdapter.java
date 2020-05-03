package com.antonina.socialsynchro.common.gui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.model.attachments.Attachment;
import com.antonina.socialsynchro.common.model.posts.IPost;
import com.antonina.socialsynchro.databinding.AttachmentDisplayItemBinding;

public class AttachmentDisplayAdapter extends BaseAdapter<Attachment, AttachmentDisplayAdapter.AttachmentViewHolder> {
    private IPost postContainer;

    protected class AttachmentViewHolder extends BaseAdapter.BaseViewHolder<AttachmentDisplayItemBinding> {

        public AttachmentViewHolder(@NonNull View view) {
            super(view);
        }

        @Override
        protected AttachmentDisplayItemBinding getBinding(View view) {
            return AttachmentDisplayItemBinding.bind(view);
        }
    }

    public AttachmentDisplayAdapter(AppCompatActivity context) {
        super(context);
    }

    public AttachmentDisplayAdapter(AppCompatActivity context, IPost postContainer) {
        super(context);
        this.postContainer = postContainer;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.attachment_display_item;
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

    public void setSource(IPost postContainer) {
        this.postContainer = postContainer;
        loadData();
    }
}
