package com.antonina.socialsynchro.gui.adapters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.antonina.socialsynchro.R;
import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.database.repositories.AccountRepository;
import com.antonina.socialsynchro.databinding.AccountMainItemBinding;

import java.util.ArrayList;
import java.util.List;

public class AccountMainAdapter extends RecyclerView.Adapter<AccountMainAdapter.AccountViewHolder> {
    private List<Account> accounts;

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        private AccountMainItemBinding binding;

        public AccountViewHolder(View view) {
            super(view);
            binding = AccountMainItemBinding.bind(view);
        }
    }

    public AccountMainAdapter() {
        accounts = new ArrayList<Account>();
        refreshData();
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View accountView = inflater.inflate(R.layout.account_main_item, parent, false);
        AccountViewHolder viewHolder = new AccountViewHolder(accountView);

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

    public List<Account> getData() {
        return accounts;
    }

    public void addItem(Account item) {
        accounts.add(item);
        item.saveInDatabase();
        notifyItemInserted(accounts.size() - 1);
    }

    public void refreshData() {
        AccountRepository repository = AccountRepository.getInstance();
        final LiveData<List<Account>> accountLiveData = repository.getAllDataList();
        accountLiveData.observeForever(new Observer<List<Account>>() {
            @Override
            public void onChanged(@Nullable List<Account> data) {
                accounts = data;
                notifyDataSetChanged();
                accountLiveData.removeObserver(this);
            }
        });
    }
}
