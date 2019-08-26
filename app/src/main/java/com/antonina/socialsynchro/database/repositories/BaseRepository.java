package com.antonina.socialsynchro.database.repositories;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.antonina.socialsynchro.database.daos.BaseDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public abstract class BaseRepository<DataTableClass, EntityClass> {
    private LiveData<Map<Long, EntityClass>> data;
    protected BaseDao<DataTableClass> dao;

    protected abstract Map<Long, EntityClass> convertToEntities(List<DataTableClass> input);

    protected abstract DataTableClass convertToTable(EntityClass entity, boolean isNew);

    protected void loadAllData() {
        try {
            LiveData<List<DataTableClass>> databaseData = new GetAllDataAsyncTask<DataTableClass>(dao).execute().get();
            data = Transformations.map(databaseData, new Function<List<DataTableClass>, Map<Long, EntityClass>>() {
                @Override
                public Map<Long, EntityClass> apply(List<DataTableClass> input) {
                    return convertToEntities(input);
                }
            });
            data.observeForever(new Observer<Map<Long, EntityClass>>() {
                @Override
                public void onChanged(@Nullable Map<Long, EntityClass> longEntityClassMap) {
                    data.removeObserver(this);
                }
            });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public LiveData<Map<Long, EntityClass>> getAllData() {
        return data;
    }

    public LiveData<List<EntityClass>> getAllDataList() {
        LiveData<List<EntityClass>> result = Transformations.map(data, new Function<Map<Long, EntityClass>, List<EntityClass>>() {
            @Override
            public List<EntityClass> apply(Map<Long, EntityClass> input) {
                return new ArrayList<EntityClass>(input.values());
            }
        });
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

    public LiveData<EntityClass> getDataByID(long accountID) {
        final long id = accountID;
        LiveData<EntityClass> result = Transformations.map(data, new Function<Map<Long, EntityClass>, EntityClass> () {
            @Override
            public EntityClass apply(Map<Long, EntityClass> input) {
                return input.get(id);
            }
        });
        return result;
    }

    public Long insert(EntityClass entity) {
        Long id = null;
        try {
            BaseDao<DataTableClass> baseDao = dao;
            DataTableClass dataTable = convertToTable(entity, true);
            id = new InsertAsyncTask<DataTableClass>(baseDao).execute(dataTable).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void delete(EntityClass entity) {
        BaseDao<DataTableClass> baseDao = dao;
        DataTableClass dataTable = convertToTable(entity, false);
        new DeleteAsyncTask<DataTableClass>(baseDao).execute(dataTable);
    }

    public void update(EntityClass entity) {
        BaseDao<DataTableClass> baseDao = dao;
        DataTableClass dataTable = convertToTable(entity, false);
        new UpdateAsyncTask<DataTableClass>(baseDao).execute(dataTable);
    }

    private static class GetAllDataAsyncTask<DataTableClass> extends AsyncTask<Void, Void, LiveData<List<DataTableClass>>> {
        private BaseDao<DataTableClass> dao;

        public GetAllDataAsyncTask(BaseDao<DataTableClass> dao) { this.dao = dao; }

        @Override
        protected LiveData<List<DataTableClass>> doInBackground(final Void... params) {
            return dao.getAllData();
        }
    }

    private static class CountAsyncTask<DataTableClass> extends AsyncTask<Void, Void, Integer> {
        private BaseDao<DataTableClass> dao;

        public CountAsyncTask(BaseDao<DataTableClass> dao) {
            this.dao = dao;
        }

        @Override
        protected Integer doInBackground(final Void... params) {
            return dao.count();
        }
    }

    protected static class GetDataByIDAsyncTask<DataTableClass> extends AsyncTask<Long, Void, LiveData<DataTableClass>> {
        private BaseDao<DataTableClass> dao;

        public GetDataByIDAsyncTask(BaseDao<DataTableClass> dao) { this.dao = dao; }

        @Override
        protected LiveData<DataTableClass> doInBackground(final Long... params) {
            return dao.getDataByID(params[0]);
        }
    }

    private static class InsertAsyncTask<DataTableClass> extends AsyncTask<DataTableClass, Void, Long> {
        private BaseDao<DataTableClass> dao;

        public InsertAsyncTask(BaseDao<DataTableClass> dao) {
            this.dao = dao;
        }

        @Override
        protected Long doInBackground(final DataTableClass... params) {
            return dao.insert(params[0]);
        }
    }

    private static class UpdateAsyncTask<DataTableClass> extends AsyncTask<DataTableClass, Void, Void> {
        private BaseDao<DataTableClass> dao;

        public UpdateAsyncTask(BaseDao<DataTableClass> dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(final DataTableClass... params) {
            dao.update(params[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask<DataTableClass> extends AsyncTask<DataTableClass, Void, Void> {
        private BaseDao<DataTableClass> dao;

        public DeleteAsyncTask(BaseDao<DataTableClass> dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(final DataTableClass... params) {
            dao.delete(params[0]);
            return null;
        }
    }

    protected static class FilterSource<EntityClass> extends MediatorLiveData<Pair<List<Long>, Map<Long, EntityClass>>> {
        public FilterSource(final LiveData<List<Long>> IDsource, final LiveData<Map<Long, EntityClass>> dataSource) {
            addSource(IDsource, new Observer<List<Long>>() {
                @Override
                public void onChanged(@Nullable List<Long> first) {
                    setValue(Pair.create(first, dataSource.getValue()));
                }
            });
            addSource(dataSource, new Observer<Map<Long, EntityClass>>() {
                @Override
                public void onChanged(@Nullable Map<Long, EntityClass> second) {
                    setValue(Pair.create(IDsource.getValue(), second));
                }
            });
        }
    }
}
