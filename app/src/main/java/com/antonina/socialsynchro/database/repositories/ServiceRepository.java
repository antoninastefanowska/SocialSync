package com.antonina.socialsynchro.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.antonina.socialsynchro.database.ApplicationDatabase;
import com.antonina.socialsynchro.database.daos.ServiceDao;
import com.antonina.socialsynchro.database.tables.ServiceTable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ServiceRepository {
    private ServiceDao serviceDao;

    public ServiceRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        serviceDao = db.serviceDao();
    }

    public LiveData<List<ServiceTable>> getServicesData() {
        LiveData<List<ServiceTable>> result = null;
        try {
            result = new GetServicesDataAsyncTask(serviceDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public LiveData<ServiceTable> getServiceDataByID(long serviceID) {
        LiveData<ServiceTable> result = null;
        try {
            result = new GetServiceDataByIDAsyncTask(serviceDao).execute(serviceID).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static class GetServicesDataAsyncTask extends AsyncTask<Void, Void, LiveData<List<ServiceTable>>> {
        private ServiceDao serviceDao;

        public GetServicesDataAsyncTask(ServiceDao dao) { serviceDao = dao; }

        @Override
        protected LiveData<List<ServiceTable>> doInBackground(final Void... params) {
            return serviceDao.getServicesData();
        }
    }

    private static class GetServiceDataByIDAsyncTask extends AsyncTask<Long, Void, LiveData<ServiceTable>> {
        private ServiceDao serviceDao;

        public GetServiceDataByIDAsyncTask(ServiceDao dao) { serviceDao = dao; }

        @Override
        protected LiveData<ServiceTable> doInBackground(final Long... params) {
            return serviceDao.getServiceDataByID(params[0]);
        }
    }
}
