package com.antonina.socialsynchro.gui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.content.ChildPostContainer;
import com.antonina.socialsynchro.content.ParentPostContainer;
import com.antonina.socialsynchro.databinding.ChildEditItemBinding;

public class ChildEditAdapter extends RecyclerView.Adapter<ChildEditAdapter.ChildViewHolder> {
    private ParentPostContainer parent;
    private Activity context;

    public static class ChildViewHolder extends RecyclerView.ViewHolder {
        public ChildEditItemBinding binding;
        public AttachmentAdapter attachmentAdapter;

        public ChildViewHolder(@NonNull View view) {
            super(view);
            binding = ChildEditItemBinding.bind(view);

            RecyclerView attachmentRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerview_child_attachments);
            attachmentRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

            attachmentAdapter = new AttachmentAdapter();
            binding.setAttachmentAdapter(attachmentAdapter);
            binding.executePendingBindings();
        }
    }

    public ChildEditAdapter(ParentPostContainer parent, Activity context) {
        this.parent = parent;
        this.context = context;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View childView = inflater.inflate(R.layout.child_edit_item, parent, false);
        ChildViewHolder viewHolder = new ChildViewHolder(childView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder viewHolder, int position) {
        ChildPostContainer child = getItem(position);
        viewHolder.binding.setChild(child);
        viewHolder.attachmentAdapter.setSource(child);
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return parent.getChildren().size();
    }

    private ChildPostContainer getItem(int position) {
        return parent.getChildren().get(position);
    }

    public int getItemPosition(ChildPostContainer item) {
        return parent.getChildren().indexOf(item);
    }

    public void addItem(ChildPostContainer item) {
        parent.addChild(item);
        notifyItemInserted(getItemCount() - 1);
    }
}
