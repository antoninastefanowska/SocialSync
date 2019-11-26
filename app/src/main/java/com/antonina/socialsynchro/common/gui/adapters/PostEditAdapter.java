package com.antonina.socialsynchro.common.gui.adapters;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.content.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.content.posts.ParentPostContainer;
import com.antonina.socialsynchro.common.content.posts.PostContainer;
import com.antonina.socialsynchro.common.gui.activities.EditActivity;
import com.antonina.socialsynchro.databinding.ChildEditItemBinding;
import com.antonina.socialsynchro.databinding.ParentEditItemBinding;

import java.util.List;

public class PostEditAdapter extends BaseAdapter<PostContainer, PostEditAdapter.PostViewHolder> {
    private final int imageSize;
    private final static int PARENT = 0, CHILD = 1;
    private ParentPostContainer parent;
    private EditActivity activity;
    private RecyclerView postRecyclerView;

    public abstract static class PostViewHolder<ItemBindingType extends ViewDataBinding> extends BaseAdapter.BaseViewHolder<ItemBindingType> {
        public int viewHolderType;

        public final AttachmentEditAdapter attachmentAdapter;
        public final ImageView profilePictureImageView;

        public final Button addAttachmentButton;
        public final Button publishButton;
        public final Button unpublishButton;

        public PostViewHolder(@NonNull View view, AppCompatActivity context) {
            super(view);

            profilePictureImageView = view.findViewById(R.id.imageview_profile_picture);

            addAttachmentButton = view.findViewById(R.id.button_add_attachment);
            publishButton = view.findViewById(R.id.button_publish);
            unpublishButton = view.findViewById(R.id.button_unpublish);

            attachmentAdapter = new AttachmentEditAdapter(context);

            RecyclerView attachmentRecyclerView = view.findViewById(R.id.recyclerview_attachments);
            attachmentRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        }
    }

    public static class ChildViewHolder extends PostViewHolder<ChildEditItemBinding> {
        public final Button lockButton;
        public final ImageView serviceIconImageView;

        public ChildViewHolder(@NonNull View view, AppCompatActivity context) {
            super(view, context);
            viewHolderType = CHILD;

            serviceIconImageView = view.findViewById(R.id.imageview_icon_picture);
            lockButton = view.findViewById(R.id.button_lock);

            binding.setAttachmentAdapter(attachmentAdapter);
            binding.executePendingBindings();
        }

        @Override
        protected ChildEditItemBinding getBinding(View view) {
            return ChildEditItemBinding.bind(view);
        }
    }

    public static class ParentViewHolder extends PostViewHolder<ParentEditItemBinding> {
        public final Button addChildButton;
        public final Button saveButton;

        public ParentViewHolder(@NonNull View view, AppCompatActivity context) {
            super(view, context);
            viewHolderType = PARENT;

            addChildButton = view.findViewById(R.id.button_add_child);
            saveButton = view.findViewById(R.id.button_save);

            binding.setAttachmentAdapter(attachmentAdapter);
            binding.executePendingBindings();
        }

        @Override
        protected ParentEditItemBinding getBinding(View view) {
            return ParentEditItemBinding.bind(view);
        }
    }

    public PostEditAdapter(AppCompatActivity context, RecyclerView postRecyclerView) {
        super(context);
        imageSize = getPictureSize();
        activity = (EditActivity)context;
        this.postRecyclerView = postRecyclerView;
    }

    public PostEditAdapter(AppCompatActivity context, ParentPostContainer parent, RecyclerView postRecyclerView) {
        super(context);
        this.parent = parent;
        activity = (EditActivity)context;
        imageSize = getPictureSize();
        this.postRecyclerView = postRecyclerView;
        loadData();
    }

    @Override
    protected int getItemLayout() {
        return 0;
    }

    @Override
    protected void setItemBinding(PostViewHolder viewHolder, PostContainer item) {
        if (viewHolder.viewHolderType == PARENT) {
            ParentViewHolder parentViewHolder = (ParentViewHolder)viewHolder;
            parentViewHolder.binding.setParent((ParentPostContainer)item);
        } else {
            ChildViewHolder childViewHolder = (ChildViewHolder)viewHolder;
            ChildPostContainer child = (ChildPostContainer)item;
            childViewHolder.binding.setChild(child);
            loadPictureByURL(viewHolder.profilePictureImageView, imageSize, child.getAccount().getProfilePictureURL());
            loadPictureByID(((ChildViewHolder)viewHolder).serviceIconImageView, imageSize, child.getAccount().getService().getIconID());
        }
        viewHolder.attachmentAdapter.setSource(item);
    }

    @Override
    protected PostViewHolder createViewHolder(View view) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return PARENT;
        else
            return CHILD;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        final PostViewHolder viewHolder;

        if (viewType == PARENT) {
            view = inflater.inflate(R.layout.parent_edit_item, viewGroup, false);
            ParentViewHolder parentViewHolder = new ParentViewHolder(view, this.context);
            parentViewHolder.addChildButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.addChild();
                }
            });
            parentViewHolder.saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.exitAndSave(parent);
                }
            });

            viewHolder = parentViewHolder;
        } else {
            view = inflater.inflate(R.layout.child_edit_item, viewGroup, false);
            final ChildViewHolder childViewHolder = new ChildViewHolder(view, this.context);
            childViewHolder.lockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = childViewHolder.getAdapterPosition();
                    ChildPostContainer item = (ChildPostContainer)getItem(position);
                    if (item.isLocked())
                        item.unlock();
                    else
                        item.lock();
                }
            });

            viewHolder = childViewHolder;
        }
        viewHolder.profilePictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                PostContainer item = getItem(position);
                item.switchVisibility();
                if (item.isParent())
                    notifyItemChanged(position);
            }
        });
        viewHolder.addAttachmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.addAttachment(viewHolder);
            }
        });
        viewHolder.publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                PostContainer item = getItem(position);
                activity.publish(item);
            }
        });
        return viewHolder;
    }

    public void setSource(ParentPostContainer parent) {
        this.parent = parent;
        loadData();
    }

    @Override
    public void loadData() {
        items.clear();
        items.add(0, parent);
        items.addAll(parent.getChildren());
        notifyDataSetChanged();
    }

    public void addItem(ChildPostContainer item) {
        parent.addChild(item);
        items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    public void removeItem(ChildPostContainer item) {
        int position = getItemPosition(item);
        parent.removeChild(item);
        items.remove(position);
        notifyItemRemoved(position);
    }

    public List<ChildPostContainer> getChildren() {
        return parent.getChildren();
    }

    @Override
    public void updateItemView(final PostContainer item) {
        if (!item.isParent()) {
            postRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    int position = getItemPosition(item);
                    notifyItemChanged(position);
                }
            });

        }
    }
}