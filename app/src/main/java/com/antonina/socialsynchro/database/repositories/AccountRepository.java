package com.antonina.socialsynchro.database.repositories;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;

import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.base.AccountFactory;
import com.antonina.socialsynchro.database.ApplicationDatabase;
import com.antonina.socialsynchro.database.daos.AccountDao;
import com.antonina.socialsynchro.database.tables.AccountTable;
import com.antonina.socialsynchro.services.IService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class AccountRepository extends BaseRepository<AccountTable, Account> {
    private static AccountRepository instance;

    private AccountRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.accountDao();
        loadAllData();
    }

    public static AccountRepository getInstance(Application application) {
        if (instance == null)
            instance = new AccountRepository(application);
        return instance;
    }

    @Override
    protected Map<Long, Account> convertToEntities(List<AccountTable> input) {
        Map<Long, Account> output = new HashMap<Long, Account>();
        for (AccountTable accountData : input) {
            Account account = (Account)AccountFactory.getInstance().createFromData(accountData);
            output.put(account.getInternalID(), account);
        }
        return output;
    }

    @Override
    protected AccountTable convertToTable(Account entity, boolean isNew) {
        AccountTable data = new AccountTable();
        if (isNew)
            data.createFromNewEntity(entity);
        else
            data.createFromExistingEntity(entity);
        return data;
    }

    public LiveData<List<Account>> getDataByService(IService service) {
        long serviceID = service.getID().ordinal();
        LiveData<List<Account>> result = null;
        try {
            AccountDao accountDao = (AccountDao)dao;
            LiveData<List<AccountTable>> databaseData = new GetDataByServiceAsyncTask(accountDao).execute(serviceID).get();
            result = Transformations.map(databaseData, new Function<List<AccountTable>, List<Account>>() {
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
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static class GetDataByServiceAsyncTask extends AsyncTask<Long, Void, LiveData<List<AccountTable>>> {
        private AccountDao accountDao;

        public GetDataByServiceAsyncTask(AccountDao dao) {
            accountDao = dao;
        }

        @Override
        protected LiveData<List<AccountTable>> doInBackground(Long... params) {
            return accountDao.getDataByService(params[0]);
        }
    }
}
