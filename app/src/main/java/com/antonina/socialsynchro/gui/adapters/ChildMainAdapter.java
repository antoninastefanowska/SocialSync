package com.antonina.socialsynchro.gui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.content.ChildPostContainer;
import com.antonina.socialsynchro.content.ParentPostContainer;
import com.antonina.socialsynchro.databinding.ChildMainItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ChildMainAdapter extends RecyclerView.Adapter<ChildMainAdapter.ChildViewHolder> {
    //private List<ChildPostContainer> children;
    private ParentPostContainer parent;

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        ChildMainItemBinding binding;

        public ChildViewHolder(@NonNull View view) {
            super(view);
            binding = ChildMainItemBinding.bind(view);
        }
    }

    public ChildMainAdapter() { }

    public ChildMainAdapter(ParentPostContainer parent) {
        this.parent = parent;
        notifyDataSetChanged();
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
        ChildPostContainer child = getItem(position);
        viewHolder.binding.setChild(child);
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (parent == null)
            return 0;
        else
            return parent.getChildren().size();
    }

    public ChildPostContainer getItem(int position) {
        return parent.getChildren().get(position);
    }

    public void setSource(ParentPostContainer parent) {
        this.parent = parent;
        notifyDataSetChanged();
    }
}
