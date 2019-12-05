package com.antonina.socialsynchro.common.gui.adapters;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.gui.GUIItem;
import com.antonina.socialsynchro.common.gui.listeners.OnUpdatedListener;
import com.antonina.socialsynchro.common.gui.other.MaskTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public abstract class BaseAdapter<ItemType extends GUIItem, ViewHolderType extends BaseAdapter.BaseViewHolder> extends RecyclerView.Adapter<ViewHolderType> {
    private List<ItemType> selectedItems;
    protected List<ItemType> items;
    protected final AppCompatActivity context;
    private final OnUpdatedListener listener;

    protected abstract static class BaseViewHolder<ItemBindingType extends ViewDataBinding> extends RecyclerView.ViewHolder {
        public final ItemBindingType binding;

        public BaseViewHolder(@NonNull View view) {
            super(view);
            binding = getBinding(view);
        }

        protected abstract ItemBindingType getBinding(View view);
    }

    public BaseAdapter(AppCompatActivity context) {
        this.context = context;
        items = new ArrayList<>();
        selectedItems = new ArrayList<>();
        listener =  new OnUpdatedListener() {
            @Override
            public void onUpdated(GUIItem item) {
                updateItemView((ItemType)item);
            }
        };
    }

    protected abstract int getItemLayout();

    protected abstract void setItemBinding(ViewHolderType viewHolder, ItemType item);

    protected abstract ViewHolderType createViewHolder(View view);

    public abstract void loadData();

    @NonNull
    @Override
    public ViewHolderType onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(getItemLayout(), parent, false);
        return createViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderType viewHolder, int position) {
        ItemType item = getItem(position);
        item.setListener(listener);
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
                ItemType item = getItem(position);
                if (item.isSelected())
                    unselectItem(position);
                else
                    selectItem(position);
            }
        });
    }

    public ItemType getItem(int position) {
        return items.get(position);
    }

    public void addItem(ItemType item) {
        items.add(0, item);
        notifyItemInserted(0);
    }

    public void removeItem(ItemType item) {
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

    public void updateItemView(ItemType item) {
        int position = getItemPosition(item);
        notifyItemChanged(position);
    }

    public void setData(List<ItemType> data) {
        items = data;
        notifyDataSetChanged();
    }

    private void selectItem(int position) {
        ItemType item = getItem(position);
        if (item.isSelected())
            return;
        item.select();
        selectedItems.add(item);
        notifyItemChanged(position);
    }

    private void unselectItem(int position) {
        ItemType item = getItem(position);
        if (!item.isSelected())
            return;
        item.unselect();
        selectedItems.remove(item);
        notifyItemChanged(position);
    }

    public int getItemPosition(ItemType item) {
        return items.indexOf(item);
    }

    public ItemType getSelectedItem() {
        if (selectedItems.size() > 0)
            return selectedItems.get(0);
        else
            return null;
    }

    public List<ItemType> getSelectedItems() {
        return selectedItems;
    }

    public List<ItemType> getItems() {
        return items;
    }

    public void removeSelected() {
        for (ItemType item : selectedItems)
            removeItem(item);
        selectedItems.clear();
    }

    protected void loadPictureByURL(ImageView imageView, int imageSize, String url) {
        RequestOptions options = new RequestOptions()
                .override(imageSize)
                .fitCenter()
                .transform(new MaskTransformation(context));
        Glide.with(context)
                .load(url)
                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    protected void loadPictureByID(ImageView imageView, int imageSize, int id) {
        RequestOptions options = new RequestOptions()
                .override(imageSize)
                .fitCenter()
                .transform(new MaskTransformation(context));
        Glide.with(context)
                .load(id)
                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    protected void loadPictureByID(Transformation<Bitmap> transformation, ImageView imageView, int imageSize, int id) {
        RequestOptions options = new RequestOptions()
                .override(imageSize)
                .fitCenter()
                .transform(transformation);
        Glide.with(context)
                .load(id)
                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    protected int getPictureSize() {
        return context.getResources().getDimensionPixelSize(R.dimen.profile_picture_size);
    }
}
