package com.antonina.socialsynchro.database.repositories;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;
import android.util.Pair;

import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.base.AccountFactory;
import com.antonina.socialsynchro.database.ApplicationDatabase;
import com.antonina.socialsynchro.database.daos.AccountDao;
import com.antonina.socialsynchro.database.tables.AccountTable;
import com.antonina.socialsynchro.services.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("WeakerAccess")
public class AccountRepository extends BaseRepository<AccountTable, Account> {
    private static AccountRepository instance;

    private AccountRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.accountDao();
        loadAllData();
    }

    public static AccountRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new AccountRepository(application);
    }

    @Override
    protected Map<Long, Account> convertToEntities(List<AccountTable> input) {
        Map<Long, Account> output = new HashMap<>();
        for (AccountTable accountData : input) {
            Account account = AccountFactory.getInstance().createFromData(accountData);
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

    @Override
    protected List<Account> sortList(List<Account> list) {
        Collections.sort(list, new Comparator<Account>() {
            @Override
            public int compare(Account o1, Account o2) {
                return compareDates(o1.getConnectingDate(), o2.getConnectingDate());
            }
        });
        return list;
    }

    public LiveData<List<Account>> getDataByService(Service service) {
        long serviceID = service.getID().ordinal();
        LiveData<List<Account>> result = null;

        try {
            AccountDao accountDao = (AccountDao)dao;
            LiveData<List<Long>> IDs = new GetIDByServiceAsyncTask(accountDao).execute(serviceID).get();
            FilterSource<Account> filterSource = new FilterSource<>(IDs, getAllData());

            result = Transformations.map(filterSource, new Function<Pair<List<Long>, Map<Long, Account>>, List<Account>>() {
                @Override
                public List<Account> apply(Pair<List<Long>, Map<Long, Account>> input) {
                    List<Account> output = new ArrayList<>();
                    for (Long id : input.first)
                        output.add(input.second.get(id));
                    return sortList(output);
                }
            });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public long getIDByExternalID(String externalID) {
        long result = -1;
        try {
            AccountDao accountDao = (AccountDao)dao;
            result = new GetIDByExternalIDAsyncTask(accountDao).execute(externalID).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean accountExists(String externalID) {
        boolean result = false;
        try {
            AccountDao accountDao = (AccountDao)dao;
            result = new AccountExistsAsyncTask(accountDao).execute(externalID).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void update(Account account) {
        super.update(account);
        ChildPostContainerRepository childRepository = ChildPostContainerRepository.getInstance();
        childRepository.loadAllData();
    }

    @Override
    public void delete(Account account) {
        super.delete(account);
        ChildPostContainerRepository childRepository = ChildPostContainerRepository.getInstance();
        childRepository.loadAllData();
    }

    private static class GetIDByServiceAsyncTask extends AsyncTask<Long, Void, LiveData<List<Long>>> {
        private final AccountDao accountDao;

        public GetIDByServiceAsyncTask(AccountDao dao) {
            accountDao = dao;
        }

        @Override
        protected LiveData<List<Long>> doInBackground(Long... params) {
            return accountDao.getIDByService(params[0]);
        }
    }

    private static class GetIDByExternalIDAsyncTask extends AsyncTask<String, Void, Long> {
        private AccountDao accountDao;

        public GetIDByExternalIDAsyncTask(AccountDao dao) {
            accountDao = dao;
        }

        @Override
        protected Long doInBackground(String... params) {
            return accountDao.getIDByExternalID(params[0]);
        }
    }

    private static class AccountExistsAsyncTask extends AsyncTask<String, Void, Boolean> {
        private AccountDao accountDao;

        public AccountExistsAsyncTask(AccountDao dao) {
            accountDao = dao;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return accountDao.accountExists(params[0]);
        }
    }
}
