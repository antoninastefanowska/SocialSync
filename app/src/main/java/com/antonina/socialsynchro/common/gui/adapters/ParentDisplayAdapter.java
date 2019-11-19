package com.antonina.socialsynchro.common.gui.adapters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.content.posts.ParentPostContainer;
import com.antonina.socialsynchro.common.database.repositories.ParentPostContainerRepository;
import com.antonina.socialsynchro.databinding.ParentDisplayItemBinding;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ParentDisplayAdapter extends BaseAdapter<ParentPostContainer, ParentDisplayAdapter.ParentViewHolder> {

    protected static class ParentViewHolder extends BaseAdapter.BaseViewHolder<ParentDisplayItemBinding> {
        public final AttachmentDisplayAdapter attachmentAdapter;
        public final ChildDisplayAdapter childAdapter;

        public ParentViewHolder(@NonNull View view, AppCompatActivity context) {
            super(view);

            RecyclerView childRecyclerView = view.findViewById(R.id.recyclerview_main_children);
            childRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

            RecyclerView attachmentRecyclerView = view.findViewById(R.id.recyclerview_main_attachments);
            attachmentRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

            childAdapter = new ChildDisplayAdapter(context);
            binding.setChildAdapter(childAdapter);

            attachmentAdapter = new AttachmentDisplayAdapter(context);
            binding.setAttachmentAdapter(attachmentAdapter);

            binding.executePendingBindings();
        }

        @Override
        protected ParentDisplayItemBinding getBinding(View view) {
            return ParentDisplayItemBinding.bind(view);
        }
    }

    public ParentDisplayAdapter(AppCompatActivity context) {
        super(context);
        loadData();
    }

    @Override
    protected int getItemLayout() {
        return R.layout.parent_display_item;
    }

    @Override
    protected void setItemBinding(ParentViewHolder viewHolder, ParentPostContainer item) {
        viewHolder.binding.setParent(item);
        viewHolder.childAdapter.setSource(item);
        viewHolder.attachmentAdapter.setSource(item);
    }

    @Override
    protected ParentViewHolder createViewHolder(View view) {
        return new ParentViewHolder(view, context);
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        ParentViewHolder viewHolder = super.onCreateViewHolder(parent, position);
        setSelectable(viewHolder);
        return viewHolder;
    }

    @Override
    public void loadData() {
        ParentPostContainerRepository repository = ParentPostContainerRepository.getInstance();
        final LiveData<List<ParentPostContainer>> parentLiveData = repository.getAllDataList();
        parentLiveData.observe(context, new Observer<List<ParentPostContainer>>() {
            @Override
            public void onChanged(@Nullable List<ParentPostContainer> data) {
                if (data != null) {
                    items = data;
                    notifyDataSetChanged();
                    parentLiveData.removeObserver(this);
                }
            }
        });
    }
}
