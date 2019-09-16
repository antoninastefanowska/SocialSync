package com.antonina.socialsynchro.gui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.content.attachments.AttachmentType;
import com.antonina.socialsynchro.content.attachments.AttachmentTypes;
import com.antonina.socialsynchro.databinding.AttachmentTypeDialogItemBinding;
import com.antonina.socialsynchro.gui.listeners.OnAttachmentTypeSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;

public class AttachmentTypeDialogAdapter extends BaseAdapter<AttachmentType, AttachmentTypeDialogAdapter.AttachmentTypeViewHolder> {
    private OnAttachmentTypeSelectedListener listener;

    public static class AttachmentTypeViewHolder extends BaseAdapter.BaseViewHolder<AttachmentTypeDialogItemBinding> {

        public AttachmentTypeViewHolder(@NonNull View view) {
            super(view);
        }

        @Override
        protected AttachmentTypeDialogItemBinding getBinding(View view) {
            return AttachmentTypeDialogItemBinding.bind(view);
        }
    }

    public AttachmentTypeDialogAdapter(AppCompatActivity context, OnAttachmentTypeSelectedListener listener) {
        super(context);
        this.listener = listener;
        loadData();
    }

    @Override
    protected int getItemLayout() {
        return R.layout.attachment_type_dialog_item;
    }

    @Override
    protected void setItemBinding(AttachmentTypeViewHolder viewHolder, AttachmentType item) {
        viewHolder.binding.setAttachmentType(item);
    }

    @Override
    protected AttachmentTypeViewHolder createViewHolder(View view) {
        return new AttachmentTypeViewHolder(view);
    }

    @NonNull
    @Override
    public AttachmentTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        final AttachmentTypeViewHolder viewHolder = super.onCreateViewHolder(parent, position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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
    public void loadData() {
        AttachmentType[] array = AttachmentTypes.getAttachmentTypes();
        items = new ArrayList<AttachmentType>(Arrays.asList(array));
        notifyDataSetChanged();
    }
}