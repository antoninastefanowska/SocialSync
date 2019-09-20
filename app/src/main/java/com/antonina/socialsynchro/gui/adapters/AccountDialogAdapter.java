package com.antonina.socialsynchro.gui.adapters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.database.repositories.AccountRepository;
import com.antonina.socialsynchro.databinding.AccountDialogItemBinding;

import java.util.ArrayList;
import java.util.List;

public class AccountDialogAdapter extends BaseAdapter<Account, AccountDialogAdapter.AccountViewHolder> {
    private List<Account> ignoredData;

    public static class AccountViewHolder extends BaseAdapter.BaseViewHolder<AccountDialogItemBinding> {

        public AccountViewHolder(View view) {
            super(view);
        }

        @Override
        protected AccountDialogItemBinding getBinding(View view) {
            return AccountDialogItemBinding.bind(view);
        }
    }

    public AccountDialogAdapter(AppCompatActivity context) {
        super(context);
        ignoredData = new ArrayList<Account>();
        loadData();
    }

    @Override
    protected int getItemLayout() {
        return R.layout.account_dialog_item;
    }

    @Override
    protected void setItemBinding(AccountViewHolder viewHolder, Account item) {
        viewHolder.binding.setAccount(item);
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
                items.clear();
                for (Account account : data) {
                    if (!ignoredData.contains(account))
                        items.add(account);
                }
                notifyDataSetChanged();
                accountLiveData.removeObserver(this);
            }
        });
    }

    public void setIgnoredData(List<Account> ignoredData) {
        this.ignoredData = ignoredData;
        for (Account item : ignoredData) {
            if (items.contains(item))
                items.remove(item);
        }
    }
}
