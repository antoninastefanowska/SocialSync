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
    private LiveData<List<ServiceTable>> services;

    public ServiceRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        serviceDao = db.serviceDao();
        services = serviceDao.getServices();
    }

    public LiveData<List<ServiceTable>> getServices() { return services; }

    public LiveData<ServiceTable> getServiceById(long serviceId) {
        LiveData<ServiceTable> result = null;
        try {
            result = new GetServiceByIdAsyncTask(serviceDao).execute(serviceId).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int count() {
        int result = 0;
        try {
            result = new CountAsyncTask(serviceDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Long insert(ServiceTable service) {
        Long id = null;
        try {
            id = new InsertAsyncTask(serviceDao).execute(service).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void update(ServiceTable service) {
        new UpdateAsyncTask(serviceDao).execute(service);
    }

    public void delete(ServiceTable service) {
        new DeleteAsyncTask(serviceDao).execute(service);
    }

    private static class GetServiceByIdAsyncTask extends AsyncTask<Long, Void, LiveData<ServiceTable>> {
        private ServiceDao serviceDao;

        public GetServiceByIdAsyncTask(ServiceDao dao) { serviceDao = dao; }

        @Override
        protected LiveData<ServiceTable> doInBackground(final Long... params) {
            return serviceDao.getServiceById(params[0]);
        }
    }

    private static class CountAsyncTask extends AsyncTask<Void, Void, Integer> {
        private ServiceDao serviceDao;

        public CountAsyncTask(ServiceDao dao) { serviceDao = dao; }

        @Override
        protected Integer doInBackground(final Void... params) {
            return serviceDao.count();
        }
    }

    private static class InsertAsyncTask extends AsyncTask<ServiceTable, Void, Long> {
        private ServiceDao serviceDao;

        public InsertAsyncTask(ServiceDao dao) { serviceDao = dao; }

        @Override
        protected Long doInBackground(final ServiceTable... params) {
            return serviceDao.insert(params[0]);
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<ServiceTable, Void, Void> {
        private ServiceDao serviceDao;

        public UpdateAsyncTask(ServiceDao dao) { serviceDao = dao; }

        @Override
        protected Void doInBackground(final ServiceTable... params) {
            serviceDao.update(params[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<ServiceTable, Void, Void> {
        private ServiceDao serviceDao;

        public DeleteAsyncTask(ServiceDao dao) { serviceDao = dao; }

        @Override
        protected Void doInBackground(final ServiceTable... params) {
            serviceDao.delete(params[0]);
            return null;
        }
    }
}
