package com.antonina.socialsynchro.common.database.repositories;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.antonina.socialsynchro.common.database.IDatabaseEntity;
import com.antonina.socialsynchro.common.database.daos.BaseDao;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@SuppressWarnings({"unchecked", "UnnecessaryLocalVariable", "WeakerAccess", "UseCompareMethod"})
public abstract class BaseRepository<DataRowType extends IDatabaseRow, EntityType extends IDatabaseEntity> {
    private LiveData<Map<Long, EntityType>> data;
    protected BaseDao<DataRowType> dao;

    protected abstract Map<Long, EntityType> convertToEntities(List<DataRowType> input);

    protected abstract DataRowType convertToRow(EntityType entity);

    protected abstract List<EntityType> sortList(List<EntityType> list);

    protected int compareDates(Date date1, Date date2) {
        if (date1.getTime() > date2.getTime())
            return -1;
        else if (date1.getTime() < date2.getTime())
            return 1;
        else
            return 0;
    }

    protected void loadAllData() {
        try {
            GetAllDataAsyncTask<DataRowType> asyncTask = new GetAllDataAsyncTask<>(dao);
            LiveData<List<DataRowType>> databaseData = asyncTask.execute().get();
            data = Transformations.map(databaseData, new Function<List<DataRowType>, Map<Long, EntityType>>() {
                @Override
                public Map<Long, EntityType> apply(List<DataRowType> input) {
                    return convertToEntities(input);
                }
            });
            data.observeForever(new Observer<Map<Long, EntityType>>() {
                @Override
                public void onChanged(@Nullable Map<Long, EntityType> longEntityTypeMap) {
                    data.removeObserver(this);
                }
            });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LiveData<Map<Long, EntityType>> getAllData() {
        return data;
    }

    public LiveData<List<EntityType>> getAllDataList() {
        LiveData<List<EntityType>> result = Transformations.map(data, new Function<Map<Long, EntityType>, List<EntityType>>() {
            @Override
            public List<EntityType> apply(Map<Long, EntityType> input) {
                List<EntityType> list = new ArrayList<>(input.values());
                return sortList(list);
            }
        });
        return result;
    }

    public int count() {
        int result = 0;
        try {
            CountAsyncTask<DataRowType> asyncTask = new CountAsyncTask<>(dao);
            result = asyncTask.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public LiveData<EntityType> getDataByID(long accountID) {
        final long id = accountID;
        LiveData<EntityType> result = Transformations.map(data, new Function<Map<Long, EntityType>, EntityType> () {
            @Override
            public EntityType apply(Map<Long, EntityType> input) {
                return input.get(id);
            }
        });
        return result;
    }

    public Long insert(EntityType entity) {
        Long id = null;
        try {
            BaseDao<DataRowType> baseDao = dao;
            DataRowType dataRow = convertToRow(entity);
            InsertAsyncTask<DataRowType> asyncTask = new InsertAsyncTask<>(baseDao);
            id = asyncTask.execute(dataRow).get();
            loadAllData();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void delete(EntityType entity) {
        BaseDao<DataRowType> baseDao = dao;
        DataRowType dataRow = convertToRow(entity);
        DeleteAsyncTask<DataRowType> asyncTask = new DeleteAsyncTask<>(baseDao);
        asyncTask.execute(dataRow);
        loadAllData();
    }

    public void update(EntityType entity) {
        BaseDao<DataRowType> baseDao = dao;
        DataRowType dataRow = convertToRow(entity);
        UpdateAsyncTask<DataRowType> asyncTask = new UpdateAsyncTask<>(baseDao);
        asyncTask.execute(dataRow);
        loadAllData();
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

    protected static class FilterSource<EntityType extends IDatabaseEntity> extends MediatorLiveData<Pair<List<Long>, Map<Long, EntityType>>> {
        public FilterSource(final LiveData<List<Long>> IDsource, final LiveData<Map<Long, EntityType>> dataSource) {
            addSource(IDsource, new Observer<List<Long>>() {
                @Override
                public void onChanged(@Nullable List<Long> first) {
                    if (dataSource.getValue() != null)
                        setValue(Pair.create(first, dataSource.getValue()));
                }
            });
            addSource(dataSource, new Observer<Map<Long, EntityType>>() {
                @Override
                public void onChanged(@Nullable Map<Long, EntityType> second) {
                    if (IDsource.getValue() != null)
                        setValue(Pair.create(IDsource.getValue(), second));
                }
            });
        }
    }
}
