package com.antonina.socialsynchro.services.deviantart.database.repositories;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;

import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.repositories.BaseRepository;
import com.antonina.socialsynchro.services.deviantart.model.DeviantArtCategory;
import com.antonina.socialsynchro.services.deviantart.database.daos.DeviantArtCategoryDao;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtCategoryRow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DeviantArtCategoryRepository extends BaseRepository<DeviantArtCategoryRow, DeviantArtCategory> {
    private static DeviantArtCategoryRepository instance;

    private DeviantArtCategoryRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.deviantArtCategoryDao();
    }

    public static DeviantArtCategoryRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new DeviantArtCategoryRepository(application);
    }

    @Override
    protected DeviantArtCategory convertToEntity(DeviantArtCategoryRow dataRow) {
        return new DeviantArtCategory(dataRow);
    }

    @Override
    protected DeviantArtCategoryRow convertToDataRow(DeviantArtCategory entity) {
        DeviantArtCategoryRow dataRow = new DeviantArtCategoryRow();
        dataRow.createFromEntity(entity);
        return dataRow;
    }

    public LiveData<DeviantArtCategory> getRootCategory() {
        try {
            DeviantArtCategoryDao deviantArtCategoryDao = (DeviantArtCategoryDao)dao;
            GetRootCategoryAsyncTask asyncTask = new GetRootCategoryAsyncTask(deviantArtCategoryDao);
            LiveData<DeviantArtCategoryRow> dataRow = asyncTask.execute().get();
            LiveData<DeviantArtCategory> entity = Transformations.map(dataRow, new Function<DeviantArtCategoryRow, DeviantArtCategory>() {
                @Override
                public DeviantArtCategory apply(DeviantArtCategoryRow input) {
                    return convertToEntity(input);
                }
            });
            return entity;
        } catch (ExecutionException | InterruptedException e) {
            handleException(e);
            return null;
        }
    }

    public LiveData<List<DeviantArtCategory>> getDataByParentCategory(DeviantArtCategory parentCategory) {
        try {
            long parentCategoryID = parentCategory.getInternalID();
            DeviantArtCategoryDao deviantArtCategoryDao = (DeviantArtCategoryDao)dao;
            GetDataByParentCategoryAsyncTask asyncTask = new GetDataByParentCategoryAsyncTask(deviantArtCategoryDao);
            LiveData<List<DeviantArtCategoryRow>> dataRows = asyncTask.execute(parentCategoryID).get();
            LiveData<List<DeviantArtCategory>> entities = Transformations.map(dataRows, new Function<List<DeviantArtCategoryRow>, List<DeviantArtCategory>>() {
                @Override
                public List<DeviantArtCategory> apply(List<DeviantArtCategoryRow> input) {
                    List<DeviantArtCategory> output = new ArrayList<>();
                    for (DeviantArtCategoryRow dataRow : input)
                        output.add(convertToEntity(dataRow));
                    return output;
                }
            });
            return entities;
        } catch (ExecutionException | InterruptedException e) {
            handleException(e);
            return null;
        }
    }

    public boolean categoryExists(String externalID) {
        try {
            DeviantArtCategoryDao deviantArtCategoryDao = (DeviantArtCategoryDao)dao;
            CategoryExistsAsyncTask asyncTask = new CategoryExistsAsyncTask(deviantArtCategoryDao);
            return asyncTask.execute(externalID).get();
        } catch (ExecutionException | InterruptedException e) {
            handleException(e);
            return false;
        }
    }

    public long getIDByExternalID(String externalID) {
        try {
            DeviantArtCategoryDao deviantArtCategoryDao = (DeviantArtCategoryDao)dao;
            GetIDByExternalIDAsyncTask asyncTask = new GetIDByExternalIDAsyncTask(deviantArtCategoryDao);
            return asyncTask.execute(externalID).get();
        } catch (ExecutionException | InterruptedException e) {
            handleException(e);
            return -1;
        }
    }

    private static class GetRootCategoryAsyncTask extends AsyncTask<Void, Void, LiveData<DeviantArtCategoryRow>> {
        private DeviantArtCategoryDao dao;

        public GetRootCategoryAsyncTask(DeviantArtCategoryDao deviantArtCategoryDao) {
            this.dao = deviantArtCategoryDao;
        }

        @Override
        protected LiveData<DeviantArtCategoryRow> doInBackground(Void... params) {
            return dao.getRootCategory();
        }
    }

    private static class GetDataByParentCategoryAsyncTask extends AsyncTask<Long, Void, LiveData<List<DeviantArtCategoryRow>>> {
        private DeviantArtCategoryDao dao;

        public GetDataByParentCategoryAsyncTask(DeviantArtCategoryDao deviantArtCategoryDao) {
            this.dao = deviantArtCategoryDao;
        }

        @Override
        protected LiveData<List<DeviantArtCategoryRow>> doInBackground(Long... params) {
            return dao.getDataByParentCategory(params[0]);
        }
    }

    private static class CategoryExistsAsyncTask extends AsyncTask<String, Void, Boolean> {
        private DeviantArtCategoryDao dao;

        public CategoryExistsAsyncTask(DeviantArtCategoryDao deviantArtCategoryDao) {
            this.dao = deviantArtCategoryDao;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return dao.categoryExists(params[0]);
        }
    }

    private static class GetIDByExternalIDAsyncTask extends AsyncTask<String, Void, Long> {
        private DeviantArtCategoryDao dao;

        public GetIDByExternalIDAsyncTask(DeviantArtCategoryDao deviantArtCategoryDao) {
            this.dao = deviantArtCategoryDao;
        }

        @Override
        protected Long doInBackground(String... params) {
            return null;
        }
    }
}
