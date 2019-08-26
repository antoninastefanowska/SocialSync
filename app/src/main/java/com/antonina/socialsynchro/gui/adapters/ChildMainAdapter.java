package com.antonina.socialsynchro.gui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.content.ChildPostContainer;
import com.antonina.socialsynchro.databinding.ChildMainItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ChildMainAdapter extends RecyclerView.Adapter<ChildMainAdapter.ChildViewHolder> {
    private List<ChildPostContainer> children;

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        ChildMainItemBinding binding;

        public ChildViewHolder(@NonNull View view) {
            super(view);
            binding = ChildMainItemBinding.bind(view);
        }
    }

    public ChildMainAdapter() {
        children = new ArrayList<ChildPostContainer>();
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View parentView = inflater.inflate(R.layout.child_main_item, parent, false);
        ChildMainAdapter.ChildViewHolder viewHolder = new ChildMainAdapter.ChildViewHolder(parentView);

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
        this.children = data;
        notifyDataSetChanged();
    }
}
