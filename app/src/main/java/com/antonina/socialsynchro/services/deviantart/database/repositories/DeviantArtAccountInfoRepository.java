package com.antonina.socialsynchro.services.deviantart.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.repositories.BaseRepository;
import com.antonina.socialsynchro.services.deviantart.content.DeviantArtAccount;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtAccountInfoRow;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DeviantArtAccountInfoRepository extends BaseRepository<DeviantArtAccountInfoRow, DeviantArtAccount> {
    private static DeviantArtAccountInfoRepository instance;

    private DeviantArtAccountInfoRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.deviantArtAccountDao();
    }

    public static DeviantArtAccountInfoRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new DeviantArtAccountInfoRepository(application);
    }

    @Override
    protected Map<Long, DeviantArtAccount> convertToEntities(List<DeviantArtAccountInfoRow> input) {
        return null;
    }

    @Override
    protected DeviantArtAccountInfoRow convertToRow(DeviantArtAccount entity) {
        DeviantArtAccountInfoRow data = new DeviantArtAccountInfoRow();
        data.createFromEntity(entity);
        return data;
    }

    @Override
    protected List<DeviantArtAccount> sortList(List<DeviantArtAccount> list) {
        return list;
    }

    public LiveData<DeviantArtAccountInfoRow> getDataTableByID(long id) {
        LiveData<DeviantArtAccountInfoRow> result = null;
        try {
            GetDataByIDAsyncTask<DeviantArtAccountInfoRow> asyncTask = new GetDataByIDAsyncTask<>(dao);
            result = asyncTask.execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
