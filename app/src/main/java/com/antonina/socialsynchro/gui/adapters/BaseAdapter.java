package com.antonina.socialsynchro.gui.adapters;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antonina.socialsynchro.gui.GUIItem;
import com.antonina.socialsynchro.gui.listeners.OnUpdatedListener;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<ItemClass extends GUIItem, ViewHolderClass extends BaseAdapter.BaseViewHolder> extends RecyclerView.Adapter<ViewHolderClass> {
    protected List<ItemClass> items;
    protected List<ItemClass> selectedItems;
    protected AppCompatActivity context;

    public abstract static class BaseViewHolder<ItemBindingClass extends ViewDataBinding> extends RecyclerView.ViewHolder {
        public ItemBindingClass binding;

        public BaseViewHolder(@NonNull View view) {
            super(view);
            binding = getBinding(view);
        }

        protected abstract ItemBindingClass getBinding(View view);
    }

    public BaseAdapter(AppCompatActivity context) {
        this.context = context;
        items = new ArrayList<ItemClass>();
        selectedItems = new ArrayList<ItemClass>();
    }

    protected abstract int getItemLayout();

    protected abstract void setItemBinding(ViewHolderClass viewHolder, ItemClass item);

    protected abstract ViewHolderClass createViewHolder(View view);

    public abstract void loadData();

    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(getItemLayout(), parent, false);
        final ViewHolderClass viewHolder = createViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass viewHolder, int position) {
        ItemClass item = getItem(position);
        final int index = position;
        item.setListener(new OnUpdatedListener() {
            @Override
            public void onUpdated() {
                notifyItemChanged(index);
            }
        });
        setItemBinding(viewHolder, item);
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected void setSelectable(final BaseViewHolder viewHolder) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                ItemClass item = getItem(position);
                if (item.isSelected())
                    unselectItem(position);
                else
                    selectItem(position);
            }
        });
    }

    public ItemClass getItem(int position) {
        return items.get(position);
    }

    public void addItem(ItemClass item) {
        items.add(0, item);
        notifyItemInserted(0);
    }

    public void removeItem(ItemClass item) {
        int position = getItemPosition(item);
        if (position != -1) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void updateItemView(ItemClass item) {
        int position = getItemPosition(item);
        notifyItemChanged(position);
    }

    public void setData(List<ItemClass> data) {
        items = data;
        notifyDataSetChanged();
    }

    public void selectItem(int position) {
        ItemClass item = getItem(position);
        if (item.isSelected())
            return;
        item.select();
        selectedItems.add(item);
        notifyItemChanged(position);
    }

    public void unselectItem(int position) {
        ItemClass item = getItem(position);
        if (!item.isSelected())
            return;
        item.unselect();
        selectedItems.remove(item);
        notifyItemChanged(position);
    }

    public int getItemPosition(ItemClass item) {
        return items.indexOf(item);
    }

    public List<ItemClass> getSelectedItems() {
        return selectedItems;
    }

    public List<ItemClass> getItems() {
        return items;
    }

    public void removeSelected() {
        for (ItemClass item : selectedItems)
            removeItem(item);
        selectedItems.clear();
    }
}
