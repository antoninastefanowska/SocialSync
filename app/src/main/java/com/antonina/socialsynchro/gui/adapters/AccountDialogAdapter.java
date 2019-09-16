package com.antonina.socialsynchro.gui.adapters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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
    private List<Account> selectedAccounts;
    private List<Account> ignoredData;
    private AppCompatActivity context;

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        public AccountDialogItemBinding binding;

        public AccountViewHolder(View view) {
            super(view);
            binding = AccountDialogItemBinding.bind(view);
        }
    }

    public AccountDialogAdapter(AppCompatActivity context) {
        accounts = new ArrayList<Account>();
        selectedAccounts = new ArrayList<Account>();
        ignoredData = new ArrayList<Account>();
        this.context = context;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View accountView = inflater.inflate(R.layout.account_dialog_item, parent, false);
        final AccountViewHolder viewHolder = new AccountViewHolder(accountView);

        accountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Account item = getItem(position);
                if (item.isSelected())
                    unselectItem(position);
                else
                    selectItem(position);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder viewHolder, int position) {
        Account account = getItem(position);
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
        if (item.isSelected())
            return;
        item.select();
        selectedAccounts.add(item);
        notifyItemChanged(position);
    }

    public void unselectItem(int position) {
        Account item = accounts.get(position);
        if (!item.isSelected())
            return;
        item.unselect();
        selectedAccounts.remove(item);
        notifyItemChanged(position);
    }

    public List<Account> getData() {
        return accounts;
    }

    public List<Account> getSelectedItems() {
        return selectedAccounts;
    }

    public void refreshData() {
        AccountRepository repository = AccountRepository.getInstance();
        final LiveData<List<Account>> accountLiveData = repository.getAllDataList();
        accountLiveData.observe(context, new Observer<List<Account>>() {
            @Override
            public void onChanged(@Nullable List<Account> data) {
                accounts.clear();
                for (Account account : data) {
                    if (!ignoredData.contains(account))
                        accounts.add(account);
                }
                notifyDataSetChanged();
            }
        });
    }

    public void setIgnoredData(List<Account> ignoredData) {
        this.ignoredData = ignoredData;
        refreshData();
    }
}
