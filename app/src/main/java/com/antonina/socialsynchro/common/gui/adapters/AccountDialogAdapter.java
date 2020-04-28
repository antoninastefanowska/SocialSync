package com.antonina.socialsynchro.common.gui.adapters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.database.viewmodels.AccountViewModel;
import com.antonina.socialsynchro.databinding.AccountDialogItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class AccountDialogAdapter extends BaseAdapter<Account, AccountDialogAdapter.AccountViewHolder> {
    private List<Account> ignoredData;
    private int imageSize;
    private AccountViewModel accountViewModel;

    protected static class AccountViewHolder extends BaseAdapter.BaseViewHolder<AccountDialogItemBinding> {
        public final ImageView serviceIconImageView;

        public AccountViewHolder(View view) {
            super(view);

            serviceIconImageView = view.findViewById(R.id.imageview_icon_picture);
        }

        @Override
        protected AccountDialogItemBinding getBinding(View view) {
            return AccountDialogItemBinding.bind(view);
        }
    }

    public AccountDialogAdapter(AppCompatActivity context) {
        super(context);
        ignoredData = new ArrayList<>();
        imageSize = context.getResources().getDimensionPixelSize(R.dimen.dialog_item_height);
        accountViewModel = ViewModelProviders.of(context).get(AccountViewModel.class);
        loadData();
    }

    @Override
    protected int getItemLayout() {
        return R.layout.account_dialog_item;
    }

    @Override
    protected void setItemBinding(AccountViewHolder viewHolder, Account item) {
        viewHolder.binding.setAccount(item);
        RequestOptions options = new RequestOptions()
                .override(imageSize)
                .fitCenter();
        Glide.with(context)
                .load(item.getService().getIconID())
                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(viewHolder.serviceIconImageView);
    }

    @Override
    protected AccountViewHolder createViewHolder(View view) {
        return new AccountViewHolder(view);
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        AccountViewHolder viewHolder = super.onCreateViewHolder(parent, position);
        setSelectable(viewHolder);
        return viewHolder;
    }

    @Override
    public void loadData() {
        final LiveData<List<Account>> accountLiveData = accountViewModel.getCurrentData();
        accountLiveData.observe(context, new Observer<List<Account>>() {
            @Override
            public void onChanged(@Nullable List<Account> data) {
                if (data != null) {
                    items.clear();
                    for (Account account : data) {
                        if (!ignoredData.contains(account))
                            items.add(account);
                    }
                    notifyDataSetChanged();
                    accountLiveData.removeObserver(this);
                }
            }
        });
    }

    public void setIgnoredData(List<Account> ignoredData) {
        this.ignoredData = ignoredData;
        for (Account item : ignoredData) {
            items.remove(item);
        }
    }
}
