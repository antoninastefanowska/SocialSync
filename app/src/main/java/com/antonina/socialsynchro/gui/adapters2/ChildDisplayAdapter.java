package com.antonina.socialsynchro.gui.adapters2;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.content.ChildPostContainer;
import com.antonina.socialsynchro.content.ParentPostContainer;
import com.antonina.socialsynchro.databinding.ChildMainItemBinding;

public class ChildDisplayAdapter extends BaseAdapter<ChildPostContainer, ChildDisplayAdapter.ChildViewHolder> {
    private ParentPostContainer parent;

    public static class ChildViewHolder extends BaseAdapter.BaseViewHolder<ChildMainItemBinding> {

        public ChildViewHolder(@NonNull View view) {
            super(view);
        }

        @Override
        protected ChildMainItemBinding getBinding(View view) {
            return ChildMainItemBinding.bind(view);
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
        return R.layout.child_main_item;
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
