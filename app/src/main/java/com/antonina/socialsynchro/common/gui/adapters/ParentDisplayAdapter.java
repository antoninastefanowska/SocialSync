package com.antonina.socialsynchro.common.gui.adapters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.model.posts.ParentPostContainer;
import com.antonina.socialsynchro.common.database.viewmodels.ParentPostContainerViewModel;
import com.antonina.socialsynchro.common.gui.activities.MainActivity;
import com.antonina.socialsynchro.common.gui.operations.Operation;
import com.antonina.socialsynchro.common.gui.operations.OperationID;
import com.antonina.socialsynchro.common.gui.other.CustomViewTransformer;
import com.antonina.socialsynchro.databinding.ParentDisplayItemBinding;
import com.gtomato.android.ui.widget.CarouselView;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ParentDisplayAdapter extends BaseAdapter<ParentPostContainer, ParentDisplayAdapter.BaseParentViewHolder> {
    private final static int PARENT = 0, NEW = 1;
    private MainActivity activity;
    private ParentPostContainerViewModel parentViewModel;

    protected abstract static class BaseParentViewHolder<ItemBindingType extends ViewDataBinding> extends BaseAdapter.BaseViewHolder<ItemBindingType> {
        public int viewHolderType;

        public BaseParentViewHolder(@NonNull View view) {
            super(view);
        }
    }

    protected static class ParentViewHolder extends BaseParentViewHolder<ParentDisplayItemBinding> {
        public final AttachmentDisplayAdapter attachmentAdapter;
        public final ChildDisplayAdapter childAdapter;
        public final OperationAdapter operationAdapter;
        public final ImageView imageView;
        public final CarouselView operationMenu;

        public ParentViewHolder(@NonNull View view, AppCompatActivity context) {
            super(view);
            viewHolderType = PARENT;

            RecyclerView childRecyclerView = view.findViewById(R.id.recyclerview_main_children);
            childRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

            RecyclerView attachmentRecyclerView = view.findViewById(R.id.recyclerview_main_attachments);
            attachmentRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

            imageView = view.findViewById(R.id.imageview_profile_picture);
            operationMenu = view.findViewById(R.id.operation_menu);

            childAdapter = new ChildDisplayAdapter(context);
            binding.setChildAdapter(childAdapter);

            attachmentAdapter = new AttachmentDisplayAdapter(context);
            binding.setAttachmentAdapter(attachmentAdapter);

            operationAdapter = new OperationAdapter(OperationAdapter.DISPLAY);
            operationMenu.setTransformer(new CustomViewTransformer());
            operationMenu.setInfinite(true);
            operationMenu.setAdapter(operationAdapter);

            binding.executePendingBindings();
        }

        @Override
        protected ParentDisplayItemBinding getBinding(View view) {
            return ParentDisplayItemBinding.bind(view);
        }
    }

    protected static class NewParentViewHolder extends BaseParentViewHolder<ViewDataBinding> {
        public final Button addNewButton;

        public NewParentViewHolder(@NonNull View view) {
            super(view);
            viewHolderType = NEW;

            addNewButton = view.findViewById(R.id.button_add_new);
        }

        @Override
        protected ViewDataBinding getBinding(View view) {
            return null;
        }
    }

    public ParentDisplayAdapter(AppCompatActivity context) {
        super(context);
        activity = (MainActivity)context;
        parentViewModel = ViewModelProviders.of(activity).get(ParentPostContainerViewModel.class);
        loadData();
    }

    @Override
    protected int getItemLayout() {
        return 0;
    }

    @Override
    protected void setItemBinding(BaseParentViewHolder viewHolder, ParentPostContainer item) {
        if (viewHolder.viewHolderType == PARENT) {
            final ParentViewHolder parentViewHolder = (ParentViewHolder)viewHolder;
            parentViewHolder.binding.setParent(item);
            parentViewHolder.childAdapter.setSource(item);
            parentViewHolder.attachmentAdapter.setSource(item);
            parentViewHolder.operationAdapter.setSource(item);

            Operation operation = parentViewHolder.operationAdapter.getItem(OperationID.EDIT);
            operation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = parentViewHolder.getAdapterPosition();
                    ParentPostContainer item = getItem(position);
                    activity.editParent(item);
                }
            });
            operation = parentViewHolder.operationAdapter.getItem(OperationID.SYNCHRONIZE);
            operation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = parentViewHolder.getAdapterPosition();
                    ParentPostContainer item = getItem(position);
                    activity.synchronizePost(item);
                }
            });
            operation = parentViewHolder.operationAdapter.getItem(OperationID.STATISTICS);
            operation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = parentViewHolder.getAdapterPosition();
                    ParentPostContainer item = getItem(position);
                    activity.showParentStatistics(item);
                }
            });
            operation = parentViewHolder.operationAdapter.getItem(OperationID.PUBLISH);
            operation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = parentViewHolder.getAdapterPosition();
                    ParentPostContainer item = getItem(position);
                    activity.publishPost(item);
                }
            });
            operation = parentViewHolder.operationAdapter.getItem(OperationID.UNPUBLISH);
            operation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = parentViewHolder.getAdapterPosition();
                    ParentPostContainer item = getItem(position);
                    activity.unpublishPost(item);
                }
            });
            operation = parentViewHolder.operationAdapter.getItem(OperationID.DELETE);
            operation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = parentViewHolder.getAdapterPosition();
                    ParentPostContainer item = getItem(position);
                    activity.removePost(item);
                    removeItem(item);
                }
            });
        }
    }

    @Override
    protected BaseParentViewHolder createViewHolder(View view) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return NEW;
        else
            return PARENT;
    }

    @NonNull
    @Override
    public BaseParentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        final BaseParentViewHolder viewHolder;

        if (viewType == PARENT) {
            view = inflater.inflate(R.layout.parent_display_item, viewGroup, false);
            final ParentViewHolder parentViewHolder = new ParentViewHolder(view, this.context);
            setHideable(parentViewHolder);
            viewHolder = parentViewHolder;
        } else {
            view = inflater.inflate(R.layout.parent_new_item, viewGroup, false);
            NewParentViewHolder newParentViewHolder = new NewParentViewHolder(view);
            newParentViewHolder.addNewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.createNew();
                }
            });
            viewHolder = newParentViewHolder;
        }
        return viewHolder;
    }

    @Override
    public void loadData() {
        final LiveData<List<ParentPostContainer>> parentLiveData = parentViewModel.getCurrentData();
        parentLiveData.observe(context, new Observer<List<ParentPostContainer>>() {
            @Override
            public void onChanged(@Nullable List<ParentPostContainer> data) {
                if (data != null) {
                    items = data;
                    for (ParentPostContainer item : items)
                        item.hide();
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull BaseParentViewHolder viewHolder, int position) {
        if (viewHolder.viewHolderType != NEW)
            super.onBindViewHolder(viewHolder, position);
    }

    @Override
    public ParentPostContainer getItem(int position) {
        return super.getItem(position - 1);
    }

    @Override
    public void updateItemView(ParentPostContainer item) {
        int position = getItemPosition(item);
        notifyItemChanged(position + 1);
    }

    public void editItem(ParentPostContainer oldItem, ParentPostContainer newItem) {
        int position = getItemPosition(oldItem);
        items.set(position, newItem);
        notifyItemChanged(position + 1);
    }

    @Override
    public void addItem(ParentPostContainer item) {
        items.add(0, item);
        notifyItemInserted(1);
    }

    @Override
    public void removeItem(ParentPostContainer item) {
        int position = getItemPosition(item);
        if (position != -1) {
            items.remove(position);
            notifyItemRemoved(position + 1);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    public MainActivity getActivity() {
        return activity;
    }
}
