package com.antonina.socialsynchro.common.database.repositories;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;
import android.util.Pair;

import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.content.services.Service;
import com.antonina.socialsynchro.common.content.services.Services;
import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.daos.AccountDao;
import com.antonina.socialsynchro.common.database.rows.AccountRow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AccountRepository extends BaseRepository<AccountRow, Account> {
    private static AccountRepository instance;

    private AccountRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.accountDao();
    }

    public static AccountRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new AccountRepository(application);
    }

    @Override
    protected Account convertToEntity(AccountRow dataRow) {
        Service service = Services.getService(dataRow.serviceID);
        return service.createAccount(dataRow);
    }

    @Override
    protected AccountRow convertToDataRow(Account entity) {
        AccountRow dataRow = new AccountRow();
        dataRow.createFromEntity(entity);
        return dataRow;
    }

    public LiveData<List<Account>> getDataByService(Service service) {
        try {
            int serviceID = service.getID().ordinal();
            AccountDao accountDao = (AccountDao)dao;
            GetDataByServiceAsyncTask asyncTask = new GetDataByServiceAsyncTask(accountDao);
            LiveData<List<AccountRow>> dataRows = asyncTask.execute(serviceID).get();
            LiveData<List<Account>> entities = Transformations.map(dataRows, new Function<List<AccountRow>, List<Account>>() {
                @Override
                public List<Account> apply(List<AccountRow> input) {
                    List<Account> output = new ArrayList<>();
                    for (AccountRow dataRow : input)
                        output.add(convertToEntity(dataRow));
                    return output;
                }
            });
            return entities;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public long getIDByExternalIDAndService(String externalID, Service service) {
        try {
            int serviceID = service.getID().ordinal();
            AccountDao accountDao = (AccountDao)dao;
            GetIDByExternalIDAsyncTask asyncTask = new GetIDByExternalIDAsyncTask(accountDao);
            return asyncTask.execute(new Pair<>(externalID, serviceID)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean accountExists(String externalID, Service service) {
        try {
            int serviceID = service.getID().ordinal();
            AccountDao accountDao = (AccountDao)dao;
            AccountExistsAsyncTask asyncTask = new AccountExistsAsyncTask(accountDao);
            return asyncTask.execute(new Pair<>(externalID, serviceID)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static class GetDataByServiceAsyncTask extends AsyncTask<Integer, Void, LiveData<List<AccountRow>>> {
        private AccountDao accountDao;

        public GetDataByServiceAsyncTask(AccountDao dao) {
            this.accountDao = dao;
        }

        @Override
        protected LiveData<List<AccountRow>> doInBackground(Integer... params) {
            return accountDao.getDataByService(params[0]);
        }
    }

    private static class GetIDByExternalIDAsyncTask extends AsyncTask<Pair<String, Integer>, Void, Long> {
        private AccountDao accountDao;

        public GetIDByExternalIDAsyncTask(AccountDao dao) {
            accountDao = dao;
        }

        @Override
        protected Long doInBackground(Pair<String, Integer>... params) {
            return accountDao.getIDByExternalIDAndService(params[0].first, params[0].second);
        }
    }

    private static class AccountExistsAsyncTask extends AsyncTask<Pair<String, Integer>, Void, Boolean> {
        private AccountDao accountDao;

        public AccountExistsAsyncTask(AccountDao dao) {
            accountDao = dao;
        }

        @SafeVarargs
        @Override
        protected final Boolean doInBackground(Pair<String, Integer>... params) {
            return accountDao.accountExists(params[0].first, params[0].second);
        }
    }
}
