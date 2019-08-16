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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountViewModel extends AndroidViewModel implements IEditableViewModel<Account> {
    private static AccountViewModel instance;
    private AccountRepository repository;
    private LiveData<Map<Long, Account>> accounts;

    public static AccountViewModel getInstance(@NonNull Application application) {
        if (instance == null)
            instance = new AccountViewModel(application);
        return instance;
    }

    private AccountViewModel(@NonNull Application application) {
        super(application);
        repository = new AccountRepository(application);

        LiveData<List<AccountTable>> accountsData = repository.getAllData();
        accounts = Transformations.map(accountsData, new Function<List<AccountTable>, Map<Long, Account>>() {
            @Override
            public Map<Long, Account> apply(List<AccountTable> input) {
                Map<Long, Account> output = new HashMap<Long, Account>();
                for (AccountTable accountData : input) {
                    Account account = (Account)AccountFactory.getInstance().createFromData(accountData);
                    output.put(account.getID(), account);
                }
                return output;
            }
        });
    }

    @Override
    public LiveData<Map<Long, Account>> getAllEntities() { return accounts; }

    @Override
    public int count() {
        return repository.count();
    }

    @Override
    public LiveData<Account> getEntityByID(long accountID) {
        final long id = accountID;
        LiveData<Account> account = Transformations.map(accounts, new Function<Map<Long, Account>, Account>() {
            @Override
            public Account apply(Map<Long, Account> input) {
                return input.get(id);
            }
        });
        return account;
    }

    public LiveData<List<Account>> getEntityByService(Service service) {
        long serviceID = service.getID();
        LiveData<List<AccountTable>> accountsData = repository.getDataByService(serviceID);

        LiveData<List<Account>> filteredAccounts = Transformations.map(accountsData, new Function<List<AccountTable>, List<Account>>() {
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
        return filteredAccounts;
    }

    @Override
    public long insert(Account account) {
        AccountTable data = new AccountTable();
        data.createFromNewEntity(account);
        return repository.insert(data);
    }

    @Override
    public void delete(Account account) {
        AccountTable data = new AccountTable();
        data.createFromExistingEntity(account);
        repository.delete(data);
    }

    @Override
    public void update(Account account) {
        AccountTable data = new AccountTable();
        data.createFromExistingEntity(account);
        repository.update(data);
    }
}
