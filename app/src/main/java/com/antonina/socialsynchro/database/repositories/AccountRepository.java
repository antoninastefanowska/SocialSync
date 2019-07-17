package com.antonina.socialsynchro.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.antonina.socialsynchro.database.ApplicationDatabase;
import com.antonina.socialsynchro.database.daos.AccountDao;
import com.antonina.socialsynchro.database.tables.AccountTable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AccountRepository {
    private AccountDao accountDao;
    private LiveData<List<AccountTable>> accounts;

    public AccountRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        accountDao = db.accountDao();
        accounts = accountDao.getAccounts();
    }

    public LiveData<List<AccountTable>> getAccounts() {
        return accounts;
    }

    public int count() {
        int result = 0;
        try {
            result = new CountAsyncTask(accountDao).execute().get();
        } catch(ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public LiveData<List<AccountTable>> getAccountsByService(long serviceId) {
        LiveData<List<AccountTable>> result = null;
        try {
            result = new GetAccountsByServiceAsyncTask(accountDao).execute(serviceId).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Long insert(AccountTable account) {
        Long id = null;
        try {
            id = new InsertAsyncTask(accountDao).execute(account).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void delete(AccountTable account) {
        new DeleteAsyncTask(accountDao).execute(account);
    }

    public void update(AccountTable account) {
        new UpdateAsyncTask(accountDao).execute(account);
    }

    private static class CountAsyncTask extends AsyncTask<Void, Void, Integer> {
        private AccountDao accountDao;

        public CountAsyncTask(AccountDao dao) {
            accountDao = dao;
        }

        @Override
        protected Integer doInBackground(final Void... params) {
            return accountDao.count();
        }
    }

    private static class GetAccountsByServiceAsyncTask extends AsyncTask<Long, Void, LiveData<List<AccountTable>>> {
        private AccountDao accountDao;

        public GetAccountsByServiceAsyncTask(AccountDao dao) {
            accountDao = dao;
        }

        @Override
        protected LiveData<List<AccountTable>> doInBackground(Long... params) {
            return accountDao.getAccountsByService(params[0]);
        }
    }

    private static class InsertAsyncTask extends AsyncTask<AccountTable, Void, Long> {
        private AccountDao accountDao;

        public InsertAsyncTask(AccountDao dao) {
            accountDao = dao;
        }

        @Override
        protected Long doInBackground(final AccountTable... params) {
            return accountDao.insert(params[0]);
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<AccountTable, Void, Void> {
        private AccountDao accountDao;

        public UpdateAsyncTask(AccountDao dao) {
            accountDao = dao;
        }

        @Override
        protected Void doInBackground(final AccountTable... params) {
            accountDao.update(params[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<AccountTable, Void, Void> {
        private AccountDao accountDao;

        public DeleteAsyncTask(AccountDao dao) {
            accountDao = dao;
        }

        @Override
        protected Void doInBackground(final AccountTable... params) {
            accountDao.delete(params[0]);
            return null;
        }
    }
}
