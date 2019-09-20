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
import com.antonina.socialsynchro.services.IService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public static AccountRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new AccountRepository(application);
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

    public LiveData<List<Account>> getDataByService(IService service) {
        long serviceID = service.getID().ordinal();
        LiveData<List<Account>> result = null;

        try {
            AccountDao accountDao = (AccountDao)dao;
            LiveData<List<Long>> IDs = new GetIDByServiceAsyncTask(accountDao).execute(serviceID).get();
            FilterSource<Account> filterSource = new FilterSource<Account>(IDs, getAllData());

            result = Transformations.map(filterSource, new Function<Pair<List<Long>, Map<Long, Account>>, List<Account>>() {
                @Override
                public List<Account> apply(Pair<List<Long>, Map<Long, Account>> input) {
                    List<Account> output = new ArrayList<Account>();
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

    private static class GetIDByServiceAsyncTask extends AsyncTask<Long, Void, LiveData<List<Long>>> {
        private AccountDao accountDao;

        public GetIDByServiceAsyncTask(AccountDao dao) {
            accountDao = dao;
        }

        @Override
        protected LiveData<List<Long>> doInBackground(Long... params) {
            return accountDao.getIDByService(params[0]);
        }
    }
}
