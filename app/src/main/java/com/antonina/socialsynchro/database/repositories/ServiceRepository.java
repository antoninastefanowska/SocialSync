package com.antonina.socialsynchro.database.repositories;

import android.app.Application;
import android.os.AsyncTask;

import com.antonina.socialsynchro.database.ApplicationDatabase;
import com.antonina.socialsynchro.database.daos.ServiceDao;
import com.antonina.socialsynchro.database.tables.ServiceTable;

import java.util.List;

public class ServiceRepository extends ReadOnlyRepository<ServiceTable> {
    public ServiceRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.serviceDao();
    }

    public void insertMany(List<ServiceTable> servicesData) {
        ServiceDao serviceDao = (ServiceDao)dao;
        new InsertManyAsyncTask(serviceDao).execute(servicesData);
    }

    private static class InsertManyAsyncTask extends AsyncTask<List<ServiceTable>, Void, Void> {
        private ServiceDao dao;

        public InsertManyAsyncTask(ServiceDao dao) { this.dao = dao; }

        @Override
        protected Void doInBackground(final List<ServiceTable>... params) {
            dao.insertMany(params[0]);
            return null;
        }
    }
}
