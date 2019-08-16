package com.antonina.socialsynchro.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.antonina.socialsynchro.database.ApplicationDatabase;
import com.antonina.socialsynchro.database.daos.AccountDao;
import com.antonina.socialsynchro.database.tables.AccountTable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AccountRepository extends EditableRepository<AccountTable> {
    public AccountRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.accountDao();
    }

    public LiveData<List<AccountTable>> getDataByService(long serviceID) {
        LiveData<List<AccountTable>> result = null;
        try {
            AccountDao accountDao = (AccountDao)dao;
            result = new GetDataByServiceAsyncTask(accountDao).execute(serviceID).get();
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
