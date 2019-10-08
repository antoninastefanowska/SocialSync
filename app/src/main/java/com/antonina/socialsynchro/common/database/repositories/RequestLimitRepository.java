package com.antonina.socialsynchro.common.database.repositories;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;
import android.util.Pair;

import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.content.services.Service;
import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.daos.RequestLimitDao;
import com.antonina.socialsynchro.common.database.rows.RequestLimitRow;
import com.antonina.socialsynchro.common.rest.RequestLimit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

public class RequestLimitRepository extends BaseRepository<RequestLimitRow, RequestLimit> {
    private static RequestLimitRepository instance;

    private RequestLimitRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.requestLimitDao();
        loadAllData();
    }

    public static RequestLimitRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new RequestLimitRepository(application);
    }

    @Override
    protected Map<Long, RequestLimit> convertToEntities(List<RequestLimitRow> input) {
        Map<Long, RequestLimit> output = new TreeMap<>();
        for (RequestLimitRow requestLimitData : input) {
            RequestLimit requestLimit = new RequestLimit(requestLimitData);
            output.put(requestLimit.getInternalID(), requestLimit);
        }
        return output;
    }

    @Override
    protected RequestLimitRow convertToRow(RequestLimit entity) {
        RequestLimitRow data = new RequestLimitRow();
        data.createFromEntity(entity);
        return data;
    }

    @Override
    protected List<RequestLimit> sortList(List<RequestLimit> list) {
        return list;
    }

    public LiveData<List<RequestLimit>> getIDsByAccount(Account account) {
        long accountID = account.getInternalID();
        LiveData<List<RequestLimit>> result = null;
        try {
            RequestLimitDao requestLimitDao = (RequestLimitDao)dao;
            LiveData<List<Long>> IDs = new GetIDsByAccount(requestLimitDao).execute(accountID).get();
            FilterSource<RequestLimit> filterSource = new FilterSource<>(IDs, getAllData());

            result = Transformations.map(filterSource, new Function<Pair<List<Long>, Map<Long, RequestLimit>>, List<RequestLimit>>() {
                @Override
                public List<RequestLimit> apply(Pair<List<Long>, Map<Long, RequestLimit>> input) {
                    List<RequestLimit> output = new ArrayList<>();
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

    public LiveData<List<RequestLimit>> getIDsForApplicationByService(Service service) {
        int serviceID = service.getID().ordinal();
        LiveData<List<RequestLimit>> result = null;
        try {
            RequestLimitDao requestLimitDao = (RequestLimitDao)dao;
            LiveData<List<Long>> IDs = new GetIDsForApplicationByService(requestLimitDao).execute(serviceID).get();
            FilterSource<RequestLimit> filterSource = new FilterSource<>(IDs, getAllData());

            result = Transformations.map(filterSource, new Function<Pair<List<Long>, Map<Long, RequestLimit>>, List<RequestLimit>>() {
                @Override
                public List<RequestLimit> apply(Pair<List<Long>, Map<Long, RequestLimit>> input) {
                    List<RequestLimit> output = new ArrayList<>();
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

    private static class GetIDsByAccount extends AsyncTask<Long, Void, LiveData<List<Long>>> {
        private RequestLimitDao dao;

        public GetIDsByAccount(RequestLimitDao dao) {
            this.dao = dao;
        }

        @Override
        protected LiveData<List<Long>> doInBackground(Long... params) {
            return dao.getIDsByAccount(params[0]);
        }
    }

    private static class GetIDsForApplicationByService extends AsyncTask<Integer, Void, LiveData<List<Long>>> {
        private RequestLimitDao dao;

        public GetIDsForApplicationByService(RequestLimitDao dao) {
            this.dao = dao;
        }

        @Override
        protected LiveData<List<Long>> doInBackground(Integer... params) {
            return dao.getIDsForApplicationByService(params[0]);
        }
    }
}
