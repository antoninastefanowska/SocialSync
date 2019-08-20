package com.antonina.socialsynchro.database.repositories;

import android.os.AsyncTask;

import com.antonina.socialsynchro.database.daos.EditableDao;

import java.util.concurrent.ExecutionException;

public abstract class EditableRepository<DataTableClass> extends ReadOnlyRepository<DataTableClass> {
    public Long insert(DataTableClass dataTable) {
        Long id = null;
        try {
            EditableDao<DataTableClass> editableDao = (EditableDao<DataTableClass>)dao;
            id = new InsertAsyncTask<DataTableClass>(editableDao).execute(dataTable).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void delete(DataTableClass dataTable) {
        EditableDao<DataTableClass> editableDao = (EditableDao<DataTableClass>)dao;
        new DeleteAsyncTask<DataTableClass>(editableDao).execute(dataTable);
    }

    public void update(DataTableClass dataTable) {
        EditableDao<DataTableClass> editableDao = (EditableDao<DataTableClass>)dao;
        new UpdateAsyncTask<DataTableClass>(editableDao).execute(dataTable);
    }

    private static class InsertAsyncTask<DataTableClass> extends AsyncTask<DataTableClass, Void, Long> {
        private EditableDao<DataTableClass> dao;

        public InsertAsyncTask(EditableDao<DataTableClass> dao) {
            this.dao = dao;
        }

        @Override
        protected Long doInBackground(final DataTableClass... params) {
            return dao.insert(params[0]);
        }
    }

    private static class UpdateAsyncTask<DataTableClass> extends AsyncTask<DataTableClass, Void, Void> {
        private EditableDao<DataTableClass> dao;

        public UpdateAsyncTask(EditableDao<DataTableClass> dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(final DataTableClass... params) {
            dao.update(params[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask<DataTableClass> extends AsyncTask<DataTableClass, Void, Void> {
        private EditableDao<DataTableClass> dao;

        public DeleteAsyncTask(EditableDao<DataTableClass> dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(final DataTableClass... params) {
            dao.delete(params[0]);
            return null;
        }
    }
}
