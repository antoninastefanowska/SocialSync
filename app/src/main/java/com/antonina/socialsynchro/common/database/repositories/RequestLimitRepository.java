package com.antonina.socialsynchro.common.database.repositories;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;

import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.content.services.Service;
import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.daos.RequestLimitDao;
import com.antonina.socialsynchro.common.database.rows.RequestLimitRow;
import com.antonina.socialsynchro.common.rest.RequestLimit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RequestLimitRepository extends BaseRepository<RequestLimitRow, RequestLimit> {
    private static RequestLimitRepository instance;

    private RequestLimitRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.requestLimitDao();
    }

    public static RequestLimitRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new RequestLimitRepository(application);
    }

    @Override
    protected RequestLimit convertToEntity(RequestLimitRow dataRow) {
        return new RequestLimit(dataRow);
    }

    @Override
    protected RequestLimitRow convertToDataRow(RequestLimit entity) {
        RequestLimitRow dataRow = new RequestLimitRow();
        dataRow.createFromEntity(entity);
        return dataRow;
    }

    public LiveData<List<RequestLimit>> getDataByAccount(Account account) {
        try {
            long accountID = account.getInternalID();
            RequestLimitDao requestLimitDao = (RequestLimitDao)dao;
            GetDataByAccountAsyncTask asyncTask = new GetDataByAccountAsyncTask(requestLimitDao);
            LiveData<List<RequestLimitRow>> dataRows = asyncTask.execute(accountID).get();
            LiveData<List<RequestLimit>> entities = Transformations.map(dataRows, new Function<List<RequestLimitRow>, List<RequestLimit>>() {
                @Override
                public List<RequestLimit> apply(List<RequestLimitRow> input) {
                    List<RequestLimit> output = new ArrayList<>();
                    for (RequestLimitRow dataRow : input)
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

    public LiveData<List<RequestLimit>> getDataForApplicationByService(Service service) {
        try {
            int serviceID = service.getID().ordinal();
            RequestLimitDao requestLimitDao = (RequestLimitDao)dao;
            GetDataForApplicationByServiceAsyncTask asyncTask = new GetDataForApplicationByServiceAsyncTask(requestLimitDao);
            LiveData<List<RequestLimitRow>> dataRows = asyncTask.execute(serviceID).get();
            LiveData<List<RequestLimit>> entities = Transformations.map(dataRows, new Function<List<RequestLimitRow>, List<RequestLimit>>() {
                @Override
                public List<RequestLimit> apply(List<RequestLimitRow> input) {
                    List<RequestLimit> output = new ArrayList<>();
                    for (RequestLimitRow dataRow : input)
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

    private static class GetDataByAccountAsyncTask extends AsyncTask<Long, Void, LiveData<List<RequestLimitRow>>> {
        private RequestLimitDao dao;

        public GetDataByAccountAsyncTask(RequestLimitDao requestLimitDao) {
            this.dao = requestLimitDao;
        }

        @Override
        protected LiveData<List<RequestLimitRow>> doInBackground(Long... params) {
            return dao.getDataByAccount(params[0]);
        }
    }

    private static class GetDataForApplicationByServiceAsyncTask extends AsyncTask<Integer, Void, LiveData<List<RequestLimitRow>>> {
        private RequestLimitDao dao;

        public GetDataForApplicationByServiceAsyncTask(RequestLimitDao requestLimitDao) {
            this.dao = requestLimitDao;
        }

        @Override
        protected LiveData<List<RequestLimitRow>> doInBackground(Integer... params) {
            return dao.getDataForApplicationByService(params[0]);
        }
    }
}
