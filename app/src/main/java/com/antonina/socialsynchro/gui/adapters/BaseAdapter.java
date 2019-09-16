package com.antonina.socialsynchro.gui.adapters;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class BaseAdapter<ItemClass, ItemBindingClass extends ViewDataBinding> extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder<ItemBindingClass>> {
    private List<ItemClass> items;
    private AppCompatActivity context;

    public abstract static class BaseViewHolder<ItemBindingClass extends ViewDataBinding> extends RecyclerView.ViewHolder {
        public ItemBindingClass binding;

        protected abstract ItemBindingClass getBinding();

        public BaseViewHolder(@NonNull View view) {
            super(view);
            binding = getBinding();
        }
    }

    public BaseAdapter(AppCompatActivity context) {
        this.context = context;
    }

    protected abstract int getLayout();

    protected abstract void setBinding();

    protected abstract BaseViewHolder<ItemBindingClass> createViewHolder(View view);

    public abstract void loadData();

    @NonNull
    @Override
    public BaseViewHolder<ItemBindingClass> onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(getLayout(), parent, false);
        BaseViewHolder<ItemBindingClass> viewHolder = createViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<ItemBindingClass> viewHolder, int position) {
        ItemClass item = getItem(position);
        setBinding();
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ItemClass getItem(int position) {
        return items.get(position);
    }

    public void addItem(ItemClass item) {
        items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeItem(ItemClass item) {
        int position = items.indexOf(item);
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void setData(List<ItemClass> data) {
        items = data;
        notifyDataSetChanged();
    }
}
