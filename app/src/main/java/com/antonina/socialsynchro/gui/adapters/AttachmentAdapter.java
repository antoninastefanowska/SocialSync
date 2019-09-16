package com.antonina.socialsynchro.gui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.content.IPost;
import com.antonina.socialsynchro.content.attachments.Attachment;
import com.antonina.socialsynchro.databinding.AttachmentItemBinding;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentViewHolder> {
    private IPost postContainer;

    public static class AttachmentViewHolder extends RecyclerView.ViewHolder {
        public AttachmentItemBinding binding;

        public AttachmentViewHolder(@NonNull View view) {
            super(view);
            binding = AttachmentItemBinding.bind(view);
        }
    }

    public AttachmentAdapter(IPost postContainer) {
        this.postContainer = postContainer;
        notifyDataSetChanged();
    }

    public AttachmentAdapter() { }

    @NonNull
    @Override
    public AttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View attachmentView = inflater.inflate(R.layout.attachment_item, parent, false);
        AttachmentViewHolder viewHolder = new AttachmentViewHolder(attachmentView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentViewHolder viewHolder, int position) {
        Attachment attachment = getItem(position);
        viewHolder.binding.setAttachment(attachment);
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (postContainer == null)
            return 0;
        else
            return postContainer.getAttachments().size();
    }

    public Attachment getItem(int position) {
        return postContainer.getAttachments().get(position);
    }

    public void addItem(Attachment item) {
        postContainer.addAttachment(item);
        notifyItemInserted(getItemCount() - 1);
    }

    public void setSource(IPost postContainer) {
        this.postContainer = postContainer;
        notifyDataSetChanged();
    }
}
