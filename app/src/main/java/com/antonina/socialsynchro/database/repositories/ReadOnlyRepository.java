package com.antonina.socialsynchro.database.repositories;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.antonina.socialsynchro.database.daos.ReadOnlyDao;

import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class ReadOnlyRepository<DataTableClass> {
    protected ReadOnlyDao<DataTableClass> dao;

    public LiveData<List<DataTableClass>> getAllData() {
        LiveData<List<DataTableClass>> result = null;
        try {
            result = new GetAllDataAsyncTask<DataTableClass>(dao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int count() {
        int result = 0;
        try {
            result = new CountAsyncTask<DataTableClass>(dao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public LiveData<DataTableClass> getDataByID(long accountID) {
        LiveData<DataTableClass> result = null;
        try {
            result = new GetDataByIDAsyncTask<DataTableClass>(dao).execute(accountID).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static class GetAllDataAsyncTask<DataTableClass> extends AsyncTask<Void, Void, LiveData<List<DataTableClass>>> {
        private ReadOnlyDao<DataTableClass> dao;

        public GetAllDataAsyncTask(ReadOnlyDao<DataTableClass> dao) { this.dao = dao; }

        @Override
        protected LiveData<List<DataTableClass>> doInBackground(final Void... params) {
            return dao.getAllData();
        }
    }

    private static class CountAsyncTask<DataTableClass> extends AsyncTask<Void, Void, Integer> {
        private ReadOnlyDao<DataTableClass> dao;

        public CountAsyncTask(ReadOnlyDao<DataTableClass> dao) {
            this.dao = dao;
        }

        @Override
        protected Integer doInBackground(final Void... params) {
            return dao.count();
        }
    }

    private static class GetDataByIDAsyncTask<DataTableClass> extends AsyncTask<Long, Void, LiveData<DataTableClass>> {
        private ReadOnlyDao<DataTableClass> dao;

        public GetDataByIDAsyncTask(ReadOnlyDao<DataTableClass> dao) { this.dao = dao; }

        @Override
        protected LiveData<DataTableClass> doInBackground(final Long... params) {
            return dao.getDataByID(params[0]);
        }
    }
}
