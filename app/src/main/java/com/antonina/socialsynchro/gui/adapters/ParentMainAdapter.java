package com.antonina.socialsynchro.gui.adapters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.Observable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antonina.socialsynchro.BR;
import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.content.ChildPostContainer;
import com.antonina.socialsynchro.content.ParentPostContainer;
import com.antonina.socialsynchro.database.repositories.ParentPostContainerRepository;
import com.antonina.socialsynchro.databinding.ParentMainItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ParentMainAdapter extends RecyclerView.Adapter<ParentMainAdapter.ParentViewHolder> {
    private List<ParentPostContainer> parents;

    public static class ParentViewHolder extends RecyclerView.ViewHolder {
        public ParentMainItemBinding binding;
        public ChildMainAdapter childAdapter;

        public ParentViewHolder(@NonNull View view) {
            super(view);
            binding = ParentMainItemBinding.bind(view);

            RecyclerView childRecyclerView = view.findViewById(R.id.rvMainChildren);
            childRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

            childAdapter = new ChildMainAdapter();
            binding.setChildMainAdapter(childAdapter);
            binding.executePendingBindings();
        }
    }

    public ParentMainAdapter() {
        parents = new ArrayList<ParentPostContainer>();
        refreshData();
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View parentView = inflater.inflate(R.layout.parent_main_item, parent, false);
        ParentMainAdapter.ParentViewHolder viewHolder = new ParentMainAdapter.ParentViewHolder(parentView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder viewHolder, int position) {
        ParentPostContainer parent = parents.get(position);
        viewHolder.binding.setParent(parent);
        viewHolder.childAdapter.setData(parent.getChildren());
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return parents.size();
    }

    public ParentPostContainer getItem(int position) {
        return parents.get(position);
    }

    public void refreshData() {
        ParentPostContainerRepository repository = ParentPostContainerRepository.getInstance();
        final LiveData<List<ParentPostContainer>> parentLiveData = repository.getAllDataList();
        parentLiveData.observeForever(new Observer<List<ParentPostContainer>>() {
            @Override
            public void onChanged(@Nullable List<ParentPostContainer> data) {
                parents = data;
                notifyDataSetChanged();
                parentLiveData.removeObserver(this);
            }
        });
    }

    public void addItem(ParentPostContainer item) {
        parents.add(item);
        item.saveInDatabase();
        notifyItemInserted(parents.size() - 1);
    }
}
