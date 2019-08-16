package com.antonina.socialsynchro.database.repositories;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.antonina.socialsynchro.database.daos.EditableDao;
import com.antonina.socialsynchro.database.daos.ReadOnlyDao;

import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class ReadOnlyRepository<DataTable> {
    protected ReadOnlyDao<DataTable> dao;

    public LiveData<List<DataTable>> getAllData() {
        LiveData<List<DataTable>> result = null;
        try {
            result = new GetAllDataAsyncTask<DataTable>(dao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int count() {
        int result = 0;
        try {
            result = new CountAsyncTask<DataTable>(dao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public LiveData<DataTable> getDataByID(long accountID) {
        LiveData<DataTable> result = null;
        try {
            result = new GetDataByIDAsyncTask<DataTable>(dao).execute(accountID).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static class GetAllDataAsyncTask<DataTable> extends AsyncTask<Void, Void, LiveData<List<DataTable>>> {
        private ReadOnlyDao<DataTable> dao;

        public GetAllDataAsyncTask(ReadOnlyDao<DataTable> dao) { this.dao = dao; }

        @Override
        protected LiveData<List<DataTable>> doInBackground(final Void... params) {
            return dao.getAllData();
        }
    }

    private static class CountAsyncTask<DataTable> extends AsyncTask<Void, Void, Integer> {
        private ReadOnlyDao<DataTable> dao;

        public CountAsyncTask(ReadOnlyDao<DataTable> dao) {
            this.dao = dao;
        }

        @Override
        protected Integer doInBackground(final Void... params) {
            return dao.count();
        }
    }

    private static class GetDataByIDAsyncTask<DataTable> extends AsyncTask<Long, Void, LiveData<DataTable>> {
        private ReadOnlyDao<DataTable> dao;

        public GetDataByIDAsyncTask(ReadOnlyDao<DataTable> dao) { this.dao = dao; }

        @Override
        protected LiveData<DataTable> doInBackground(final Long... params) {
            return dao.getDataByID(params[0]);
        }
    }
}
