package com.antonina.socialsynchro.common.database.repositories;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;

import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.daos.BaseDao;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class BaseRepository<DataRowType extends IDatabaseRow, EntityType extends IDatabaseEntity> {
    protected BaseDao<DataRowType> dao;

    protected abstract EntityType convertToEntity(DataRowType dataRow);

    protected abstract DataRowType convertToDataRow(EntityType entity);

    public LiveData<List<EntityType>> getAllData() {
        try {
            GetAllDataAsyncTask<DataRowType> asyncTask = new GetAllDataAsyncTask<>(dao);
            LiveData<List<DataRowType>> dataRows = asyncTask.execute().get();
            LiveData<List<EntityType>> entities = Transformations.map(dataRows, new Function<List<DataRowType>, List<EntityType>>() {
                @Override
                public List<EntityType> apply(List<DataRowType> input) {
                    List<EntityType> output = new ArrayList<>();
                    for (DataRowType dataRow : input)
                        output.add(convertToEntity(dataRow));
                    return output;
                }
            });
            return entities;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
            //TODO: Pokazać powiadomienie o błędzie.
        }
    }

    public LiveData<EntityType> getDataByID(long id) {
        LiveData<DataRowType> dataRow = getDataRowByID(id);
        LiveData<EntityType> entity = Transformations.map(dataRow, new Function<DataRowType, EntityType>() {
            @Override
            public EntityType apply(DataRowType input) {
                return convertToEntity(input);
            }
        });
        return entity;
    }

    public LiveData<DataRowType> getDataRowByID(long id) {
        try {
            GetDataByIDAsyncTask<DataRowType> asyncTask = new GetDataByIDAsyncTask<>(dao);
            return asyncTask.execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int count() {
        try {
            CountAsyncTask<DataRowType> asyncTask = new CountAsyncTask<>(dao);
            int result = asyncTask.execute().get();
            return result;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Long insert(EntityType entity) {
        try {
            DataRowType dataRow = convertToDataRow(entity);
            InsertAsyncTask<DataRowType> asyncTask = new InsertAsyncTask<>(dao);
            return asyncTask.execute(dataRow).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void update(EntityType entity) {
        DataRowType dataRow = convertToDataRow(entity);
        UpdateAsyncTask<DataRowType> asyncTask = new UpdateAsyncTask<>(dao);
        asyncTask.execute(dataRow);
    }

    public void delete(EntityType entity) {
        DataRowType dataRow = convertToDataRow(entity);
        DeleteAsyncTask<DataRowType> asyncTask = new DeleteAsyncTask<>(dao);
        asyncTask.execute(dataRow);
    }

    public void deleteMany(List<EntityType> entities) {
        List<DataRowType> dataRows = new ArrayList<DataRowType>();
        for (EntityType entity : entities)
            dataRows.add(convertToDataRow(entity));
        DeleteManyAsyncTask<DataRowType> asyncTask = new DeleteManyAsyncTask<>(dao);
        asyncTask.execute(dataRows);
    }

    private static class GetAllDataAsyncTask<DataRowType extends IDatabaseRow> extends AsyncTask<Void, Void, LiveData<List<DataRowType>>> {
        private final BaseDao<DataRowType> dao;

        public GetAllDataAsyncTask(BaseDao<DataRowType> dao) { this.dao = dao; }

        @Override
        protected LiveData<List<DataRowType>> doInBackground(final Void... params) {
            return dao.getAllData();
        }
    }

    private static class CountAsyncTask<DataRowType extends IDatabaseRow> extends AsyncTask<Void, Void, Integer> {
        private final BaseDao<DataRowType> dao;

        public CountAsyncTask(BaseDao<DataRowType> dao) {
            this.dao = dao;
        }

        @Override
        protected Integer doInBackground(final Void... params) {
            return dao.count();
        }
    }

    protected static class GetDataByIDAsyncTask<DataRowType extends IDatabaseRow> extends AsyncTask<Long, Void, LiveData<DataRowType>> {
        private final BaseDao<DataRowType> dao;

        public GetDataByIDAsyncTask(BaseDao<DataRowType> dao) { this.dao = dao; }

        @Override
        protected LiveData<DataRowType> doInBackground(final Long... params) {
            return dao.getDataByID(params[0]);
        }
    }

    private static class InsertAsyncTask<DataRowType extends IDatabaseRow> extends AsyncTask<DataRowType, Void, Long> {
        private final BaseDao<DataRowType> dao;

        public InsertAsyncTask(BaseDao<DataRowType> dao) {
            this.dao = dao;
        }

        @SafeVarargs
        @Override
        protected final Long doInBackground(final DataRowType... params) {
            return dao.insert(params[0]);
        }
    }

    private static class UpdateAsyncTask<DataRowType extends IDatabaseRow> extends AsyncTask<DataRowType, Void, Void> {
        private final BaseDao<DataRowType> dao;

        public UpdateAsyncTask(BaseDao<DataRowType> dao) {
            this.dao = dao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final DataRowType... params) {
            dao.update(params[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask<DataRowType extends IDatabaseRow> extends AsyncTask<DataRowType, Void, Void> {
        private final BaseDao<DataRowType> dao;

        public DeleteAsyncTask(BaseDao<DataRowType> dao) {
            this.dao = dao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final DataRowType... params) {
            dao.delete(params[0]);
            return null;
        }
    }

    private static class DeleteManyAsyncTask<DataRowType extends IDatabaseRow> extends AsyncTask<List<DataRowType>, Void, Void> {
        private final BaseDao<DataRowType> dao;

        public DeleteManyAsyncTask(BaseDao<DataRowType> dao) {
            this.dao = dao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final List<DataRowType>... params) {
            dao.deleteMany(params[0]);
            return null;
        }
    }
}
