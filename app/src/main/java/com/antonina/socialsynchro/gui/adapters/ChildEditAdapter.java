package com.antonina.socialsynchro.gui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.content.ChildPostContainer;
import com.antonina.socialsynchro.databinding.ChildEditItemBinding;

import java.util.List;

public class ChildEditAdapter extends RecyclerView.Adapter<ChildEditAdapter.ChildViewHolder> {
    private List<ChildPostContainer> children;

    public static class ChildViewHolder extends RecyclerView.ViewHolder {
        public ChildEditItemBinding binding;

        public ChildViewHolder(@NonNull View view) {
            super(view);
            binding = ChildEditItemBinding.bind(view);
        }
    }

    public ChildEditAdapter(List<ChildPostContainer> children) {
        this.children = children;
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
        ChildPostContainer child = children.get(position);
        viewHolder.binding.setChild(child);
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return children.size();
    }

    public ChildPostContainer getItem(int position) {
        return children.get(position);
    }

    public void setData(List<ChildPostContainer> data) {
        children = data;
        notifyDataSetChanged();
    }

    public int getItemPosition(ChildPostContainer item) {
        return children.indexOf(item);
    }
}
