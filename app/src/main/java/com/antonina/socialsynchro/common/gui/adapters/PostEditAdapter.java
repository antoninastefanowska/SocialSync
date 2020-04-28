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
import android.widget.EditText;
import android.widget.ImageView;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.content.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.content.posts.ParentPostContainer;
import com.antonina.socialsynchro.common.content.posts.PostContainer;
import com.antonina.socialsynchro.common.content.posts.Tag;
import com.antonina.socialsynchro.common.gui.activities.EditActivity;
import com.antonina.socialsynchro.databinding.ChildEditItemBinding;
import com.antonina.socialsynchro.databinding.ParentEditItemBinding;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;

public class PostEditAdapter extends BaseAdapter<PostContainer, PostEditAdapter.PostViewHolder> {
    private final int imageSize;
    private final static int PARENT = 0, CHILD = 1, NEW = 2;
    private ParentPostContainer parent;
    private EditActivity activity;
    private RecyclerView postRecyclerView;

    public abstract static class PostViewHolder<ItemBindingType extends ViewDataBinding> extends BaseAdapter.BaseViewHolder<ItemBindingType> {
        public int viewHolderType;

        public final AttachmentEditAdapter attachmentAdapter;
        public final TagEditAdapter tagAdapter;
        public final ImageView profilePictureImageView;

        public final Button addAttachmentButton;
        public final Button addTagsButton;
        public final Button publishButton;
        public final Button unpublishButton;

        public final EditText tagsEditText;

        public PostViewHolder(@NonNull View view, AppCompatActivity context) {
            super(view);

            profilePictureImageView = view.findViewById(R.id.imageview_profile_picture);

            addAttachmentButton = view.findViewById(R.id.button_add_attachment);
            addTagsButton = view.findViewById(R.id.button_add_tags);
            publishButton = view.findViewById(R.id.button_publish);
            unpublishButton = view.findViewById(R.id.button_unpublish);
            tagsEditText = view.findViewById(R.id.edittext_tags);

            attachmentAdapter = new AttachmentEditAdapter(context);
            tagAdapter = new TagEditAdapter(context);

            RecyclerView attachmentRecyclerView = view.findViewById(R.id.recyclerview_attachments);
            attachmentRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

            RecyclerView tagRecyclerView = view.findViewById(R.id.recyclerview_tags);
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(view.getContext());
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
            tagRecyclerView.setLayoutManager(layoutManager);
        }

        public PostViewHolder(@NonNull View view) {
            super(view);

            attachmentAdapter = null;
            tagAdapter = null;
            profilePictureImageView = null;
            addAttachmentButton = null;
            addTagsButton = null;
            publishButton = null;
            unpublishButton = null;
            tagsEditText = null;
        }
    }

    public static class ChildViewHolder extends PostViewHolder<ChildEditItemBinding> {
        public final Button lockButton;
        public final Button unlockButton;
        public final Button removeButton;
        public final ImageView serviceIconImageView;

        public ChildViewHolder(@NonNull View view, AppCompatActivity context) {
            super(view, context);
            viewHolderType = CHILD;

            serviceIconImageView = view.findViewById(R.id.imageview_icon_picture);
            lockButton = view.findViewById(R.id.button_lock);
            unlockButton = view.findViewById(R.id.button_unlock);
            removeButton = view.findViewById(R.id.button_remove);

            binding.setAttachmentAdapter(attachmentAdapter);
            binding.setTagAdapter(tagAdapter);
            binding.executePendingBindings();
        }

        @Override
        protected ChildEditItemBinding getBinding(View view) {
            return ChildEditItemBinding.bind(view);
        }
    }

    public static class ParentViewHolder extends PostViewHolder<ParentEditItemBinding> {
        public final Button saveButton;

        public ParentViewHolder(@NonNull View view, AppCompatActivity context) {
            super(view, context);
            viewHolderType = PARENT;

            saveButton = view.findViewById(R.id.button_save);

            binding.setAttachmentAdapter(attachmentAdapter);
            binding.setTagAdapter(tagAdapter);
            binding.executePendingBindings();
        }

        @Override
        protected ParentEditItemBinding getBinding(View view) {
            return ParentEditItemBinding.bind(view);
        }
    }

    public static class NewChildViewHolder extends PostViewHolder<ViewDataBinding> {
        public final Button addNewButton;

        public NewChildViewHolder(@NonNull View view) {
            super(view);
            viewHolderType = NEW;

            addNewButton = view.findViewById(R.id.button_add_new);
        }

        @Override
        protected ViewDataBinding getBinding(View view) {
            return null;
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
            viewHolder.attachmentAdapter.setSource(item);
            viewHolder.tagAdapter.setSource(item);
        } else if (viewHolder.viewHolderType == CHILD) {
            ChildViewHolder childViewHolder = (ChildViewHolder)viewHolder;
            ChildPostContainer child = (ChildPostContainer)item;
            childViewHolder.binding.setChild(child);
            loadPictureByURL(viewHolder.profilePictureImageView, imageSize, child.getAccount().getProfilePictureURL());
            loadPictureByID(((ChildViewHolder)viewHolder).serviceIconImageView, imageSize, child.getAccount().getService().getIconID());
            viewHolder.attachmentAdapter.setSource(item);
            viewHolder.tagAdapter.setSource(item);
            viewHolder.attachmentAdapter.setLocked(child.isLocked());
            viewHolder.tagAdapter.setLocked(child.isLocked());
        }
    }

    @Override
    protected PostViewHolder createViewHolder(View view) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return PARENT;
        else if (position == getItemCount() - 1)
            return NEW;
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

        if (viewType == PARENT || viewType == CHILD) {
            if (viewType == PARENT) {
                view = inflater.inflate(R.layout.parent_edit_item, viewGroup, false);
                ParentViewHolder parentViewHolder = new ParentViewHolder(view, this.context);
                setHideableWithNotification(parentViewHolder);
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
                setHideable(childViewHolder);
                childViewHolder.lockButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = childViewHolder.getAdapterPosition();
                        ChildPostContainer item = (ChildPostContainer) getItem(position);
                        item.lock();
                    }
                });
                childViewHolder.unlockButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = childViewHolder.getAdapterPosition();
                        ChildPostContainer item = (ChildPostContainer) getItem(position);
                        item.unlock(true);
                    }
                });
                childViewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = childViewHolder.getAdapterPosition();
                        ChildPostContainer item = (ChildPostContainer) getItem(position);
                        removeItem(item);
                    }
                });
                viewHolder = childViewHolder;
            }

            viewHolder.addAttachmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.addAttachment(viewHolder);
                }
            });
            viewHolder.addTagsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = viewHolder.tagsEditText.getText().toString();
                    String[] tagStrings = text.split(" ");
                    for (String tagString : tagStrings) {
                        if (!tagString.isEmpty()) {
                            Tag tag = new Tag(tagString);
                            viewHolder.tagAdapter.addItem(tag);
                        }
                    }
                    viewHolder.tagsEditText.getText().clear();
                }
            });
            viewHolder.publishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getAdapterPosition();
                    PostContainer item = getItem(position);
                    activity.publishPost(item);
                }
            });
            viewHolder.unpublishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getAdapterPosition();
                    PostContainer item = getItem(position);
                    activity.unpublishPost(item);
                }
            });
        } else {
            view = inflater.inflate(R.layout.child_new_item, viewGroup, false);
            NewChildViewHolder newChildViewHolder = new NewChildViewHolder(view);
            newChildViewHolder.addNewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.addChild();
                }
            });
            viewHolder = newChildViewHolder;
        }
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

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder viewHolder, int position) {
        if (viewHolder.viewHolderType != NEW)
            super.onBindViewHolder(viewHolder, position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
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