package com.antonina.socialsynchro.database.viewmodels;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.base.AccountFactory;
import com.antonina.socialsynchro.database.repositories.AccountRepository;
import com.antonina.socialsynchro.database.tables.AccountTable;

import java.util.ArrayList;
import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    private static AccountViewModel instance;
    private AccountRepository accountRepository;
    private LiveData<List<Account>> accounts;

    public static AccountViewModel getInstance(@NonNull Application application) {
        if (instance == null)
            instance = new AccountViewModel(application);
        return instance;
    }

    private AccountViewModel(@NonNull Application application) {
        super(application);

        accountRepository = new AccountRepository(application);
        LiveData<List<AccountTable>> accountsData = accountRepository.getAccounts();

        accounts = Transformations.map(accountsData, new Function<List<AccountTable>, List<Account>>() {
            @Override
            public List<Account> apply(List<AccountTable> input) {
                List<Account> output = new ArrayList<Account>();
                for (AccountTable accountData : input) {
                    Account account = AccountFactory.getInstance().makeAccount(accountData);
                    output.add(account);
                }
                return output;
            }
        });
    }

    public LiveData<List<Account>> getAccounts() { return accounts; }

    public int count() {
        return accountRepository.count();
    }

    public LiveData<List<Account>> getAccountsByService(long serviceId) {
        LiveData<List<AccountTable>> accountsData = accountRepository.getAccountsByService(serviceId);

        LiveData<List<Account>> transformedAccounts = Transformations.map(accountsData, new Function<List<AccountTable>, List<Account>>() {
            @Override
            public List<Account> apply(List<AccountTable> input) {
                List<Account> output = new ArrayList<Account>();
                for (AccountTable accountData : input) {
                    Account account = AccountFactory.getInstance().makeAccount(accountData);
                    output.add(account);
                }
                return output;
            }
        });

        return transformedAccounts;
    }

    public long insert(Account account) {
        AccountTable table = new AccountTable(account);
        return accountRepository.insert(table);
    }

    public void delete(Account account) {
        AccountTable table = new AccountTable(account);
        accountRepository.delete(table);
    }

    public void update(Account account) {
        AccountTable table = new AccountTable(account);
        accountRepository.update(table);
    }
}
