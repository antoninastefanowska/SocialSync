package com.antonina.socialsynchro.common.gui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.model.posts.PostContainer;
import com.antonina.socialsynchro.common.gui.GUIItem;
import com.antonina.socialsynchro.common.gui.listeners.OnUpdatedListener;
import com.antonina.socialsynchro.common.gui.operations.Operation;
import com.antonina.socialsynchro.common.gui.operations.OperationID;
import com.antonina.socialsynchro.databinding.OperationItemBinding;
import com.gtomato.android.ui.widget.CarouselView;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

public class OperationAdapter extends CarouselView.Adapter<OperationAdapter.OperationViewHolder> {
    public final static int EDIT = 0, DISPLAY = 1;

    private int mode;
    private PostContainer parentPost;
    private SortedMap<OperationID, Operation> items;
    private List<Operation> itemList;
    private final OnUpdatedListener listener;

    protected static class OperationViewHolder extends BaseAdapter.BaseViewHolder<OperationItemBinding> {

        public OperationViewHolder(@NonNull View view) {
            super(view);
        }

        @Override
        protected OperationItemBinding getBinding(View view) {
            return OperationItemBinding.bind(view);
        }
    }

    public OperationAdapter(int mode) {
        listener =  new OnUpdatedListener() {
            @Override
            public void onUpdated(GUIItem item) {
                updateItemView(item);
            }
        };
        this.mode = mode;
    }

    public void setSource(PostContainer parentPost) {
        this.parentPost = parentPost;
        loadData();
    }

    private void loadData() {
        if (mode == EDIT)
            items = parentPost.getEditOperations();
        else
            items = parentPost.getDisplayOperations();
        itemList = new ArrayList<>(items.values());
        notifyDataSetChanged();
    }

    private int getItemLayout() {
        return R.layout.operation_item;
    }

    private void updateItemView(GUIItem item) {
        int position = getItemPosition(item);
        notifyItemChanged(position);
    }

    private int getItemPosition(GUIItem item) {
        return itemList.indexOf(item);
    }

    private Operation getItem(int position) {
        return itemList.get(position);
    }

    public Operation getItem(OperationID id) {
        return items.get(id);
    }

    @NonNull
    @Override
    public OperationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(getItemLayout(), viewGroup, false);
        return new OperationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OperationViewHolder viewHolder, int position) {
        Operation item = getItem(position);
        item.setListener(listener);
        viewHolder.binding.setOperation(item);
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
