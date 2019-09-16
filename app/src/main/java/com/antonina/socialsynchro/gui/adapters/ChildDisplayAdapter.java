package com.antonina.socialsynchro.gui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.content.ChildPostContainer;
import com.antonina.socialsynchro.content.ParentPostContainer;
import com.antonina.socialsynchro.databinding.ChildDisplayItemBinding;

public class ChildDisplayAdapter extends BaseAdapter<ChildPostContainer, ChildDisplayAdapter.ChildViewHolder> {
    private ParentPostContainer parent;

    public static class ChildViewHolder extends BaseAdapter.BaseViewHolder<ChildDisplayItemBinding> {

        public ChildViewHolder(@NonNull View view) {
            super(view);
        }

        @Override
        protected ChildDisplayItemBinding getBinding(View view) {
            return ChildDisplayItemBinding.bind(view);
        }
    }

    public ChildDisplayAdapter(AppCompatActivity context) {
        super(context);
    }

    public ChildDisplayAdapter(AppCompatActivity context, ParentPostContainer parent) {
        super(context);
        this.parent = parent;
        loadData();
    }

    @Override
    protected int getItemLayout() {
        return R.layout.child_display_item;
    }

    @Override
    protected void setItemBinding(ChildViewHolder viewHolder, ChildPostContainer item) {
        viewHolder.binding.setChild(item);
    }

    @Override
    protected ChildViewHolder createViewHolder(View view) {
        return new ChildViewHolder(view);
    }

    @Override
    public void loadData() {
        items = parent.getChildren();
        notifyDataSetChanged();
    }

    public void setSource(ParentPostContainer parent) {
        this.parent = parent;
        loadData();
    }

    @Override
    public void addItem(ChildPostContainer item) {
        parent.addChild(item);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public void removeItem(ChildPostContainer item) {
        int position = getItemPosition(item);
        parent.removeChild(item);
        notifyItemRemoved(position);
    }
}
