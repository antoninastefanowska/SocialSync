package com.antonina.socialsynchro.database.viewmodels;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.base.AccountFactory;
import com.antonina.socialsynchro.base.Service;
import com.antonina.socialsynchro.database.repositories.AccountRepository;
import com.antonina.socialsynchro.database.tables.AccountTable;

import java.util.ArrayList;
import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    private static AccountViewModel instance;
    private AccountRepository accountRepository;

    public static AccountViewModel getInstance(@NonNull Application application) {
        if (instance == null)
            instance = new AccountViewModel(application);
        return instance;
    }

    private AccountViewModel(@NonNull Application application) {
        super(application);
        accountRepository = new AccountRepository(application);
    }

    public LiveData<List<Account>> getAccounts() {
        LiveData<List<AccountTable>> accountsData = accountRepository.getAccountsData();
        LiveData<List<Account>> accounts = Transformations.map(accountsData, new Function<List<AccountTable>, List<Account>>() {
            @Override
            public List<Account> apply(List<AccountTable> input) {
                List<Account> output = new ArrayList<Account>();
                for (AccountTable accountData : input) {
                    Account account = (Account)AccountFactory.getInstance().createFromData(accountData);
                    output.add(account);
                }
                return output;
            }
        });
        return accounts;
    }

    public int count() {
        return accountRepository.count();
    }

    public LiveData<List<Account>> getAccountsByService(Service service) {
        long serviceID = service.getID();
        LiveData<List<AccountTable>> accountsData = accountRepository.getAccountsDataByService(serviceID);

        LiveData<List<Account>> transformedAccounts = Transformations.map(accountsData, new Function<List<AccountTable>, List<Account>>() {
            @Override
            public List<Account> apply(List<AccountTable> input) {
                List<Account> output = new ArrayList<Account>();
                for (AccountTable accountData : input) {
                    Account account = (Account)AccountFactory.getInstance().createFromData(accountData);
                    output.add(account);
                }
                return output;
            }
        });

        return transformedAccounts;
    }

    // TODO: Upewnić się, że zachowana jest spójność między listą Account i listą AccountTable w repo - dotyczy wszystkich encji

    public long insert(Account account) {
        AccountTable data = new AccountTable();
        data.createFromNewEntity(account);
        return accountRepository.insert(data);
    }

    public void delete(Account account) {
        AccountTable data = new AccountTable();
        data.createFromEntity(account);
        accountRepository.delete(data);
    }

    public void update(Account account) {
        AccountTable data = new AccountTable();
        data.createFromEntity(account);
        accountRepository.update(data);
    }
}
