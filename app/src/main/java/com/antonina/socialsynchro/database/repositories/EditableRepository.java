package com.antonina.socialsynchro.database.repositories;

import android.os.AsyncTask;

import com.antonina.socialsynchro.database.daos.EditableDao;

import java.util.concurrent.ExecutionException;

public abstract class EditableRepository<DataTable> extends ReadOnlyRepository<DataTable> {
    public Long insert(DataTable dataTable) {
        Long id = null;
        try {
            EditableDao<DataTable> editableDao = (EditableDao<DataTable>)dao;
            id = new InsertAsyncTask<DataTable>(editableDao).execute(dataTable).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void delete(DataTable dataTable) {
        EditableDao<DataTable> editableDao = (EditableDao<DataTable>)dao;
        new DeleteAsyncTask<DataTable>(editableDao).execute(dataTable);
    }

    public void update(DataTable dataTable) {
        EditableDao<DataTable> editableDao = (EditableDao<DataTable>)dao;
        new UpdateAsyncTask<DataTable>(editableDao).execute(dataTable);
    }

    private static class InsertAsyncTask<DataTable> extends AsyncTask<DataTable, Void, Long> {
        private EditableDao<DataTable> dao;

        public InsertAsyncTask(EditableDao<DataTable> dao) {
            this.dao = dao;
        }

        @Override
        protected Long doInBackground(final DataTable... params) {
            return dao.insert(params[0]);
        }
    }

    private static class UpdateAsyncTask<DataTable> extends AsyncTask<DataTable, Void, Void> {
        private EditableDao<DataTable> dao;

        public UpdateAsyncTask(EditableDao<DataTable> dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(final DataTable... params) {
            dao.update(params[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask<DataTable> extends AsyncTask<DataTable, Void, Void> {
        private EditableDao<DataTable> dao;

        public DeleteAsyncTask(EditableDao<DataTable> dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(final DataTable... params) {
            dao.delete(params[0]);
            return null;
        }
    }
}
