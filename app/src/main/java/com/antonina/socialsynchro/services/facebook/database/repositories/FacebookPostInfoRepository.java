package com.antonina.socialsynchro.services.facebook.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.repositories.BaseRepository;
import com.antonina.socialsynchro.services.facebook.content.FacebookPostContainer;
import com.antonina.socialsynchro.services.facebook.database.rows.FacebookPostInfoRow;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FacebookPostInfoRepository extends BaseRepository<FacebookPostInfoRow, FacebookPostContainer> {
    private static FacebookPostInfoRepository instance;

    private FacebookPostInfoRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.facebookPostDao();
    }

    public static FacebookPostInfoRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new FacebookPostInfoRepository(application);
    }

    @Override
    protected Map<Long, FacebookPostContainer> convertToEntities(List<FacebookPostInfoRow> input) {
        return null;
    }

    @Override
    protected FacebookPostInfoRow convertToRow(FacebookPostContainer entity) {
        FacebookPostInfoRow data = new FacebookPostInfoRow();
        data.createFromEntity(entity);
        return data;
    }

    @Override
    protected List<FacebookPostContainer> sortList(List<FacebookPostContainer> list) {
        return list;
    }

    public LiveData<FacebookPostInfoRow> getDataTableByID(long id) {
        LiveData<FacebookPostInfoRow> result = null;
        try {
            GetDataByIDAsyncTask<FacebookPostInfoRow> asyncTask = new GetDataByIDAsyncTask<>(dao);
            result = asyncTask.execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
