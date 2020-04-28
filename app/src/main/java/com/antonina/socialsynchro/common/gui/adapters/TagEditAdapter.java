package com.antonina.socialsynchro.common.gui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.content.posts.IPost;
import com.antonina.socialsynchro.common.content.posts.Tag;
import com.antonina.socialsynchro.databinding.TagEditItemBinding;

import java.util.ArrayList;

public class TagEditAdapter extends BaseAdapter<Tag, TagEditAdapter.TagViewHolder> {
    private IPost postContainer;
    private boolean locked;

    protected static class TagViewHolder extends BaseAdapter.BaseViewHolder<TagEditItemBinding> {
        public Button removeButton;

        public TagViewHolder(@NonNull View view) {
            super(view);

            removeButton = view.findViewById(R.id.button_remove);
        }

        @Override
        protected TagEditItemBinding getBinding(View view) {
            return TagEditItemBinding.bind(view);
        }
    }

    public TagEditAdapter(AppCompatActivity context) {
        super(context);
        locked = false;
        items = new ArrayList<>();
    }

    public TagEditAdapter(AppCompatActivity context, IPost postContainer) {
        super(context);
        this.postContainer = postContainer;
        locked = false;
        items = new ArrayList<>();
        loadData();
    }

    @Override
    protected int getItemLayout() {
        return R.layout.tag_edit_item;
    }

    @Override
    protected void setItemBinding(TagViewHolder viewHolder, Tag item) {
        viewHolder.binding.setTag(item);
        viewHolder.binding.setLocked(locked);
    }

    @Override
    protected TagViewHolder createViewHolder(View view) {
        return new TagViewHolder(view);
    }

    @Override
    public void loadData() {
        items.clear();
        if (postContainer != null)
            items.addAll(postContainer.getTags());
        notifyDataSetChanged();
    }

    @Override
    public void addItem(Tag item) {
        postContainer.addTag(item);
        items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public void removeItem(Tag item) {
        int position = getItemPosition(item);
        postContainer.removeTag(item);
        items.remove(item);
        notifyItemRemoved(position);
    }

    public void setSource(IPost postContainer) {
        this.postContainer = postContainer;
        loadData();
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        final TagViewHolder viewHolder = super.onCreateViewHolder(viewGroup, viewType);
        viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Tag item = getItem(position);
                removeItem(item);
            }
        });
        return viewHolder;
    }
}
