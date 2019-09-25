package com.antonina.socialsynchro.common.gui.adapters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.database.repositories.AccountRepository;
import com.antonina.socialsynchro.databinding.AccountDisplayItemBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class AccountDisplayAdapter extends BaseAdapter<Account, AccountDisplayAdapter.AccountViewHolder> {
    private int imageSize;

    public static class AccountViewHolder extends BaseAdapter.BaseViewHolder<AccountDisplayItemBinding> {
        public final ImageView imageView;

        public AccountViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.imageview_profile_picture);
        }

        @Override
        protected AccountDisplayItemBinding getBinding(View view) {
            return AccountDisplayItemBinding.bind(view);
        }
    }

    public AccountDisplayAdapter(AppCompatActivity context) {
        super(context);
        imageSize = context.getResources().getDimensionPixelSize(R.dimen.profile_picture_size);
        loadData();
    }

    @Override
    protected int getItemLayout() {
        return R.layout.account_display_item;
    }

    @Override
    protected void setItemBinding(AccountViewHolder viewHolder, Account item) {
        viewHolder.binding.setAccount(item);
        RequestOptions options = new RequestOptions().override(imageSize).fitCenter();
        Glide.with(context)
                .load(item.getProfilePictureURL())
                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(viewHolder.imageView);
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
        AccountRepository repository = AccountRepository.getInstance();
        final LiveData<List<Account>> accountLiveData = repository.getAllDataList();
        accountLiveData.observe(context, new Observer<List<Account>>() {
            @Override
            public void onChanged(@Nullable List<Account> data) {
                items = data;
                notifyDataSetChanged();
                accountLiveData.removeObserver(this);
            }
        });
    }
}
