package com.antonina.socialsynchro.services.deviantart.database.repositories;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.os.AsyncTask;

import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.repositories.BaseRepository;
import com.antonina.socialsynchro.services.deviantart.content.DeviantArtAccount;
import com.antonina.socialsynchro.services.deviantart.content.DeviantArtGallery;
import com.antonina.socialsynchro.services.deviantart.database.daos.DeviantArtGalleryDao;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtGalleryRow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DeviantArtGalleryRepository extends BaseRepository<DeviantArtGalleryRow, DeviantArtGallery> {
    private static DeviantArtGalleryRepository instance;

    private DeviantArtGalleryRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.deviantArtGalleryDao();
    }

    public static DeviantArtGalleryRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new DeviantArtGalleryRepository(application);
    }

    @Override
    protected DeviantArtGallery convertToEntity(DeviantArtGalleryRow dataRow) {
        return new DeviantArtGallery(dataRow);
    }

    @Override
    protected DeviantArtGalleryRow convertToDataRow(DeviantArtGallery entity) {
        DeviantArtGalleryRow dataRow = new DeviantArtGalleryRow();
        dataRow.createFromEntity(entity);
        return dataRow;
    }

    public LiveData<List<DeviantArtGallery>> getDataByAccount(DeviantArtAccount account) {
        try {
            long accountID = account.getInternalID();
            DeviantArtGalleryDao deviantArtGalleryDao = (DeviantArtGalleryDao)dao;
            GetDataByAccountAsyncTask asyncTask = new GetDataByAccountAsyncTask(deviantArtGalleryDao);
            LiveData<List<DeviantArtGalleryRow>> dataRows = asyncTask.execute(accountID).get();
            LiveData<List<DeviantArtGallery>> entities = Transformations.map(dataRows, new Function<List<DeviantArtGalleryRow>, List<DeviantArtGallery>>() {
                @Override
                public List<DeviantArtGallery> apply(List<DeviantArtGalleryRow> input) {
                    List<DeviantArtGallery> output = new ArrayList<>();
                    for (DeviantArtGalleryRow dataRow : input)
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

    public boolean galleryExists(String externalID) {
        try {
            DeviantArtGalleryDao deviantArtGalleryDao = (DeviantArtGalleryDao)dao;
            GalleryExistsAsyncTask asyncTask = new GalleryExistsAsyncTask(deviantArtGalleryDao);
            return asyncTask.execute(externalID).get();
        } catch (ExecutionException | InterruptedException e) {
            handleException(e);
            return false;
        }
    }

    public long getIDByExternalID(String externalID) {
        try {
            DeviantArtGalleryDao deviantArtGalleryDao = (DeviantArtGalleryDao)dao;
            GetIDByExternalIDAsyncTask asyncTask = new GetIDByExternalIDAsyncTask(deviantArtGalleryDao);
            return asyncTask.execute(externalID).get();
        } catch (ExecutionException | InterruptedException e) {
            handleException(e);
            return -1;
        }
    }

    public LiveData<List<DeviantArtGallery>> getDataByIDs(String idsString) {
        try {
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM deviantart_gallery WHERE id IN (");
            queryBuilder.append(idsString);
            queryBuilder.append(")");
            String queryString = queryBuilder.toString();
            SimpleSQLiteQuery query = new SimpleSQLiteQuery(queryString);

            DeviantArtGalleryDao deviantArtGalleryDao = (DeviantArtGalleryDao)dao;
            GetDataByQueryAsyncTask asyncTask = new GetDataByQueryAsyncTask(deviantArtGalleryDao);
            LiveData<List<DeviantArtGalleryRow>> dataRows = asyncTask.execute(query).get();
            LiveData<List<DeviantArtGallery>> entities = Transformations.map(dataRows, new Function<List<DeviantArtGalleryRow>, List<DeviantArtGallery>>() {
                @Override
                public List<DeviantArtGallery> apply(List<DeviantArtGalleryRow> input) {
                    List<DeviantArtGallery> output = new ArrayList<>();
                    for (DeviantArtGalleryRow dataRow : input)
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

    private static class GetDataByAccountAsyncTask extends AsyncTask<Long, Void, LiveData<List<DeviantArtGalleryRow>>> {
        private DeviantArtGalleryDao dao;

        public GetDataByAccountAsyncTask(DeviantArtGalleryDao deviantArtGalleryDao) {
            this.dao = deviantArtGalleryDao;
        }

        @Override
        protected LiveData<List<DeviantArtGalleryRow>> doInBackground(Long... params) {
            return dao.getDataByAccount(params[0]);
        }
    }

    private static class GetDataByQueryAsyncTask extends AsyncTask<SimpleSQLiteQuery, Void, LiveData<List<DeviantArtGalleryRow>>> {
        private DeviantArtGalleryDao dao;

        public GetDataByQueryAsyncTask(DeviantArtGalleryDao deviantArtGalleryDao) {
            this.dao = deviantArtGalleryDao;
        }

        @Override
        protected LiveData<List<DeviantArtGalleryRow>> doInBackground(SimpleSQLiteQuery... params) {
            return dao.getDataByQuery(params[0]);
        }
    }

    private static class GalleryExistsAsyncTask extends AsyncTask<String, Void, Boolean> {
        private DeviantArtGalleryDao dao;

        public GalleryExistsAsyncTask(DeviantArtGalleryDao deviantArtGalleryDao) {
            this.dao = deviantArtGalleryDao;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return dao.galleryExists(params[0]);
        }
    }

    private static class GetIDByExternalIDAsyncTask extends AsyncTask<String, Void, Long> {
        private DeviantArtGalleryDao dao;

        public GetIDByExternalIDAsyncTask(DeviantArtGalleryDao deviantArtGalleryDao) {
            this.dao = deviantArtGalleryDao;
        }

        @Override
        protected Long doInBackground(String... params) {
            return dao.getIDByExternalID(params[0]);
        }
    }
}
