package com.antonina.socialsynchro.common.gui.adapters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.database.repositories.AccountRepository;
import com.antonina.socialsynchro.common.gui.activities.AccountsActivity;
import com.antonina.socialsynchro.databinding.AccountDisplayItemBinding;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class AccountDisplayAdapter extends BaseAdapter<Account, AccountDisplayAdapter.BaseAccountViewHolder> {
    private static final int ACCOUNT = 0, NEW = 1;
    private int imageSize;
    private AccountsActivity activity;

    protected abstract static class BaseAccountViewHolder<ItemBindingType extends ViewDataBinding> extends BaseAdapter.BaseViewHolder<ItemBindingType> {
        public int viewHolderType;

        public BaseAccountViewHolder(@NonNull View view) {
            super(view);
        }
    }

    protected static class AccountViewHolder extends BaseAccountViewHolder<AccountDisplayItemBinding> {
        public final ImageView profilePictureImageView;
        public final ImageView serviceIconImageView;

        public final Button synchronizeButton;
        public final Button removeButton;

        public AccountViewHolder(@NonNull View view) {
            super(view);
            viewHolderType = ACCOUNT;

            profilePictureImageView = view.findViewById(R.id.imageview_profile_picture);
            serviceIconImageView = view.findViewById(R.id.imageview_icon_picture);
            synchronizeButton = view.findViewById(R.id.button_synchronize);
            removeButton = view.findViewById(R.id.button_remove);
        }

        @Override
        protected AccountDisplayItemBinding getBinding(View view) {
            return AccountDisplayItemBinding.bind(view);
        }
    }

    protected static class NewAccountViewHolder extends BaseAccountViewHolder<ViewDataBinding> {
        public final Button addNewButton;

        public NewAccountViewHolder(@NonNull View view) {
            super(view);
            viewHolderType = NEW;

            addNewButton = view.findViewById(R.id.button_add_new);
        }

        @Override
        protected ViewDataBinding getBinding(View view) {
            return null;
        }
    }

    public AccountDisplayAdapter(AppCompatActivity context) {
        super(context);
        activity = (AccountsActivity)context;
        imageSize = getPictureSize();
        loadData();
    }

    @Override
    protected int getItemLayout() {
        return 0;
    }

    @Override
    protected void setItemBinding(BaseAccountViewHolder viewHolder, Account item) {
        if (viewHolder.viewHolderType == ACCOUNT) {
            AccountViewHolder accountViewHolder = (AccountViewHolder)viewHolder;
            accountViewHolder.binding.setAccount(item);
            loadPictureByURL(accountViewHolder.profilePictureImageView, imageSize, item.getProfilePictureURL());
            loadPictureByID(accountViewHolder.serviceIconImageView, imageSize, item.getService().getIconID());
        }
    }

    @Override
    protected BaseAccountViewHolder createViewHolder(View view) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return NEW;
        else
            return ACCOUNT;
    }

    @NonNull
    @Override
    public BaseAccountViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        final BaseAccountViewHolder viewHolder;

        if (viewType == ACCOUNT) {
            view = inflater.inflate(R.layout.account_display_item, viewGroup, false);
            final AccountViewHolder accountViewHolder = new AccountViewHolder(view);

            setHideable(accountViewHolder);
            accountViewHolder.synchronizeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = accountViewHolder.getAdapterPosition();
                    Account item = getItem(position);
                    activity.synchronizeAccount(item);
                }
            });
            accountViewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = accountViewHolder.getAdapterPosition();
                    Account item = getItem(position);
                    activity.removeAccount(item);
                }
            });
            viewHolder = accountViewHolder;
        } else {
            view = inflater.inflate(R.layout.account_new_item, viewGroup, false);
            NewAccountViewHolder newAccountViewHolder = new NewAccountViewHolder(view);
            newAccountViewHolder.addNewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.addAccount();
                }
            });
            viewHolder = newAccountViewHolder;
        }
        return viewHolder;
    }

    @Override
    public void loadData() {
        AccountRepository repository = AccountRepository.getInstance();
        final LiveData<List<Account>> accountLiveData = repository.getAllData();
        accountLiveData.observe(context, new Observer<List<Account>>() {
            @Override
            public void onChanged(@Nullable List<Account> data) {
                if (data != null) {
                    items = data;
                    for (Account item : items)
                        item.hide();
                    notifyDataSetChanged();
                    accountLiveData.removeObserver(this);
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull BaseAccountViewHolder viewHolder, int position) {
        if (viewHolder.viewHolderType != NEW)
            super.onBindViewHolder(viewHolder, position);
    }

    @Override
    public Account getItem(int position) {
        return super.getItem(position - 1);
    }

    @Override
    public void updateItemView(Account item) {
        int position = getItemPosition(item);
        notifyItemChanged(position + 1);
    }

    public void editItem(Account oldItem, Account newItem) {
        int position = getItemPosition(oldItem);
        items.set(position, newItem);
        notifyItemChanged(position + 1);
    }

    @Override
    public void addItem(Account item) {
        items.add(0, item);
        notifyItemInserted(1);
    }

    @Override
    public void removeItem(Account item) {
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
}
