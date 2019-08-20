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
import com.antonina.socialsynchro.database.viewmodels.AccountViewModel;
import com.antonina.socialsynchro.databinding.AccountViewBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {
    private List<Account> accounts;
    private AccountViewModel viewModel;

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        public AccountViewBinding binding;

        public AccountViewHolder(View view) {
            super(view);
            binding = AccountViewBinding.bind(view);
        }
    }

    public AccountAdapter(AccountViewModel viewModel) {
        this.viewModel = viewModel;
        this.accounts = new ArrayList<Account>();
        final LiveData<Map<Long, Account>> accountLiveData = viewModel.getAllEntities();
        accountLiveData.observeForever(new Observer<Map<Long, Account>>() {
            @Override
            public void onChanged(@Nullable Map<Long, Account> accountMap) {
                accounts = new ArrayList<Account>(accountMap.values());
                notifyDataSetChanged();
                accountLiveData.removeObserver(this);
            }
        });
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View accountView = inflater.inflate(R.layout.account_view, parent, false);
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
        viewModel.insert(item);
        notifyItemInserted(accounts.size() - 1);
    }
}
