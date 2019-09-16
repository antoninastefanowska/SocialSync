package com.antonina.socialsynchro.gui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.content.attachments.AttachmentType;
import com.antonina.socialsynchro.content.attachments.AttachmentTypes;
import com.antonina.socialsynchro.databinding.AttachmentTypeDialogItemBinding;
import com.antonina.socialsynchro.gui.dialogs.ChooseAttachmentTypeDialogListener;

public class AttachmentTypeDialogAdapter extends RecyclerView.Adapter<AttachmentTypeDialogAdapter.AttachmentTypeViewHolder> {
    private AttachmentType[] attachmentTypes;
    private ChooseAttachmentTypeDialogListener listener;

    public static class AttachmentTypeViewHolder extends RecyclerView.ViewHolder {
        public AttachmentTypeDialogItemBinding binding;

        public AttachmentTypeViewHolder(@NonNull View view) {
            super(view);
            binding = AttachmentTypeDialogItemBinding.bind(view);
        }
    }

    public AttachmentTypeDialogAdapter(ChooseAttachmentTypeDialogListener listener) {
        attachmentTypes = AttachmentTypes.getAttachmentTypes();
        this.listener = listener;
    }

    @NonNull
    @Override
    public AttachmentTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View attachmentTypeView = inflater.inflate(R.layout.attachment_type_dialog_item, parent, false);
        final AttachmentTypeViewHolder viewHolder = new AttachmentTypeViewHolder(attachmentTypeView);

        attachmentTypeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                AttachmentType item = getItem(position);
                listener.onAttachmentTypeSelected(item);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentTypeViewHolder viewHolder, int position) {
        AttachmentType attachmentType = attachmentTypes[position];
        viewHolder.binding.setAttachmentType(attachmentType);
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return attachmentTypes.length;
    }

    public AttachmentType getItem(int position) {
        return attachmentTypes[position];
    }
}
