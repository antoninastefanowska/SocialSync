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
import com.antonina.socialsynchro.common.database.tables.IDatabaseTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@SuppressWarnings({"unchecked", "UnnecessaryLocalVariable", "WeakerAccess", "UseCompareMethod"})
public abstract class BaseRepository<DataTableType extends IDatabaseTable, EntityType extends IDatabaseEntity> {
    private LiveData<Map<Long, EntityType>> data;
    private boolean loaded = false;
    protected BaseDao<DataTableType> dao;

    protected abstract Map<Long, EntityType> convertToEntities(List<DataTableType> input);

    protected abstract DataTableType convertToTable(EntityType entity, boolean isNew);

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
            GetAllDataAsyncTask<DataTableType> asyncTask = new GetAllDataAsyncTask<>(dao);
            LiveData<List<DataTableType>> databaseData = asyncTask.execute().get();
            data = Transformations.map(databaseData, new Function<List<DataTableType>, Map<Long, EntityType>>() {
                @Override
                public Map<Long, EntityType> apply(List<DataTableType> input) {
                    return convertToEntities(input);
                }
            });
            data.observeForever(new Observer<Map<Long, EntityType>>() {
                @Override
                public void onChanged(@Nullable Map<Long, EntityType> longEntityTypeMap) {
                    loaded = true;
                    data.removeObserver(this);
                }
            });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isLoaded() {
        return loaded;
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
            CountAsyncTask<DataTableType> asyncTask = new CountAsyncTask<>(dao);
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
            BaseDao<DataTableType> baseDao = dao;
            DataTableType dataTable = convertToTable(entity, true);
            InsertAsyncTask<DataTableType> asyncTask = new InsertAsyncTask<>(baseDao);
            id = asyncTask.execute(dataTable).get();
            loadAllData();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void delete(EntityType entity) {
        BaseDao<DataTableType> baseDao = dao;
        DataTableType dataTable = convertToTable(entity, false);
        DeleteAsyncTask<DataTableType> asyncTask = new DeleteAsyncTask<>(baseDao);
        asyncTask.execute(dataTable);
        loadAllData();
    }

    public void update(EntityType entity) {
        BaseDao<DataTableType> baseDao = dao;
        DataTableType dataTable = convertToTable(entity, false);
        UpdateAsyncTask<DataTableType> asyncTask = new UpdateAsyncTask<>(baseDao);
        asyncTask.execute(dataTable);
        loadAllData();
    }

    private static class GetAllDataAsyncTask<DataTableType extends IDatabaseTable> extends AsyncTask<Void, Void, LiveData<List<DataTableType>>> {
        private final BaseDao<DataTableType> dao;

        public GetAllDataAsyncTask(BaseDao<DataTableType> dao) { this.dao = dao; }

        @Override
        protected LiveData<List<DataTableType>> doInBackground(final Void... params) {
            return dao.getAllData();
        }
    }

    private static class CountAsyncTask<DataTableType extends IDatabaseTable> extends AsyncTask<Void, Void, Integer> {
        private final BaseDao<DataTableType> dao;

        public CountAsyncTask(BaseDao<DataTableType> dao) {
            this.dao = dao;
        }

        @Override
        protected Integer doInBackground(final Void... params) {
            return dao.count();
        }
    }

    protected static class GetDataByIDAsyncTask<DataTableType extends IDatabaseTable> extends AsyncTask<Long, Void, LiveData<DataTableType>> {
        private final BaseDao<DataTableType> dao;

        public GetDataByIDAsyncTask(BaseDao<DataTableType> dao) { this.dao = dao; }

        @Override
        protected LiveData<DataTableType> doInBackground(final Long... params) {
            return dao.getDataByID(params[0]);
        }
    }

    private static class InsertAsyncTask<DataTableType extends IDatabaseTable> extends AsyncTask<DataTableType, Void, Long> {
        private final BaseDao<DataTableType> dao;

        public InsertAsyncTask(BaseDao<DataTableType> dao) {
            this.dao = dao;
        }

        @SafeVarargs
        @Override
        protected final Long doInBackground(final DataTableType... params) {
            return dao.insert(params[0]);
        }
    }

    private static class UpdateAsyncTask<DataTableType extends IDatabaseTable> extends AsyncTask<DataTableType, Void, Void> {
        private final BaseDao<DataTableType> dao;

        public UpdateAsyncTask(BaseDao<DataTableType> dao) {
            this.dao = dao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final DataTableType... params) {
            dao.update(params[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask<DataTableType extends IDatabaseTable> extends AsyncTask<DataTableType, Void, Void> {
        private final BaseDao<DataTableType> dao;

        public DeleteAsyncTask(BaseDao<DataTableType> dao) {
            this.dao = dao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(final DataTableType... params) {
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
