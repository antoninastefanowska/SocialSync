package com.antonina.socialsynchro.common.database.repositories;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;
import android.util.Pair;

import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.content.accounts.AccountFactory;
import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.daos.AccountDao;
import com.antonina.socialsynchro.common.database.rows.AccountRow;
import com.antonina.socialsynchro.common.content.services.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("WeakerAccess")
public class AccountRepository extends BaseRepository<AccountRow, Account> {
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
    protected Map<Long, Account> convertToEntities(List<AccountRow> input) {
        Map<Long, Account> output = new TreeMap<>();
        for (AccountRow accountData : input) {
            Account account = AccountFactory.getInstance().createFromDatabaseRow(accountData);
            output.put(account.getInternalID(), account);
        }
        return output;
    }

    @Override
    protected AccountRow convertToRow(Account entity) {
        AccountRow data = new AccountRow();
        data.createFromEntity(entity);
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

    public long getIDByExternalIDAndService(String externalID, int serviceID) {
        long result = -1;
        try {
            AccountDao accountDao = (AccountDao)dao;
            result = new GetIDByExternalIDAsyncTask(accountDao).execute(new Pair<>(externalID, serviceID)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean accountExists(String externalID, int serviceID) {
        boolean result = false;
        try {
            AccountDao accountDao = (AccountDao)dao;
            result = new AccountExistsAsyncTask(accountDao).execute(new Pair<>(externalID, serviceID)).get();
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
