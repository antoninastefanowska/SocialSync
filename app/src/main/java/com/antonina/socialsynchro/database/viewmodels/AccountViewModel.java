package com.antonina.socialsynchro.database.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.antonina.socialsynchro.database.repositories.AccountRepository;
import com.antonina.socialsynchro.database.tables.AccountTable;

import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    private AccountRepository accountRepository;
    private LiveData<List<AccountTable>> accounts;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        accountRepository = new AccountRepository(application);
        accounts = accountRepository.getAccounts();
    }

    public LiveData<List<AccountTable>> getAccounts() { return accounts; }

    public int count() {
        return accountRepository.count();
    }

    public LiveData<List<AccountTable>> getAccountsByService(long serviceId) {
        return accountRepository.getAccountsByService(serviceId);
    }

    public long insert(AccountTable account) {
        return accountRepository.insert(account);
    }

    public void delete(AccountTable account) {
        accountRepository.delete(account);
    }

    public void update(AccountTable account) {
        accountRepository.update(account);
    }
}
