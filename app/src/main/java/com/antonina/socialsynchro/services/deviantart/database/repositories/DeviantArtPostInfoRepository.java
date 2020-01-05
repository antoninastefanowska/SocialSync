package com.antonina.socialsynchro.services.deviantart.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.repositories.BaseRepository;
import com.antonina.socialsynchro.services.deviantart.content.DeviantArtPostContainer;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtPostInfoRow;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DeviantArtPostInfoRepository extends BaseRepository<DeviantArtPostInfoRow, DeviantArtPostContainer> {
    private static DeviantArtPostInfoRepository instance;

    private DeviantArtPostInfoRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.deviantArtPostDao();
    }

    public static DeviantArtPostInfoRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new DeviantArtPostInfoRepository(application);
    }

    @Override
    protected Map<Long, DeviantArtPostContainer> convertToEntities(List<DeviantArtPostInfoRow> input) {
        return null;
    }

    @Override
    protected DeviantArtPostInfoRow convertToRow(DeviantArtPostContainer entity) {
        DeviantArtPostInfoRow data = new DeviantArtPostInfoRow();
        data.createFromEntity(entity);
        return data;
    }

    @Override
    protected List<DeviantArtPostContainer> sortList(List<DeviantArtPostContainer> list) {
        return list;
    }

    public LiveData<DeviantArtPostInfoRow> getDataTableByID(long id) {
        LiveData<DeviantArtPostInfoRow> result = null;
        try {
            GetDataByIDAsyncTask<DeviantArtPostInfoRow> asyncTask = new GetDataByIDAsyncTask<>(dao);
            result = asyncTask.execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
