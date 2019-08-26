package com.antonina.socialsynchro.gui.adapters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.database.repositories.AccountRepository;
import com.antonina.socialsynchro.databinding.AccountDialogItemBinding;

import java.util.ArrayList;
import java.util.List;

public class AccountDialogAdapter extends RecyclerView.Adapter<AccountDialogAdapter.AccountViewHolder> {
    private List<Account> accounts;
    private List<Account> checkedAccounts;
    private List<Account> ignoredData;

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        public AccountDialogItemBinding binding;

        public AccountViewHolder(View view) {
            super(view);
            binding = AccountDialogItemBinding.bind(view);
        }
    }

    public AccountDialogAdapter() {
        accounts = new ArrayList<Account>();
        checkedAccounts = new ArrayList<Account>();
        ignoredData = new ArrayList<Account>();
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View accountView = inflater.inflate(R.layout.account_dialog_item, parent, false);
        AccountViewHolder viewHolder = new AccountViewHolder(accountView);

        View itemView = viewHolder.itemView;
        final AccountViewHolder vh = viewHolder;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = vh.getAdapterPosition();
                Account item = getItem(position);
                Log.d("okno dialogowe", "Selected item: " + item.getName());
                if (item.isChecked())
                    unselectItem(position);
                else
                    selectItem(position);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder viewHolder, int position) {
        Account account = accounts.get(position);
        viewHolder.binding.setAccount(account);
        viewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public Account getItem(int position) {
        return accounts.get(position);
    }

    public void selectItem(int position) {
        Account item = accounts.get(position);
        if (item.isChecked())
            return;
        item.check();
        checkedAccounts.add(item);
        notifyItemChanged(position);
    }

    public void unselectItem(int position) {
        Account item = accounts.get(position);
        if (!item.isChecked())
            return;
        item.uncheck();
        checkedAccounts.remove(item);
        notifyItemChanged(position);
    }

    public List<Account> getData() {
        return accounts;
    }

    public List<Account> getCheckedItems() {
        return checkedAccounts;
    }

    public void refreshData() {
        AccountRepository repository = AccountRepository.getInstance();
        final LiveData<List<Account>> accountLiveData = repository.getAllDataList();
        accountLiveData.observeForever(new Observer<List<Account>>() {
            @Override
            public void onChanged(@Nullable List<Account> data) {
                accounts.clear();
                for (Account account : data) {
                    if (!ignoredData.contains(account))
                        accounts.add(account);
                }
                notifyDataSetChanged();
                accountLiveData.removeObserver(this);
            }
        });
    }

    public void setIgnoredData(List<Account> ignoredData) {
        this.ignoredData = ignoredData;
        refreshData();
    }
}
