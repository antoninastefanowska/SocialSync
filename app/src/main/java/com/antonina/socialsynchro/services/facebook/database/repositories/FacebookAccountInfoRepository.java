package com.antonina.socialsynchro.services.facebook.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.repositories.BaseRepository;
import com.antonina.socialsynchro.services.facebook.content.FacebookAccount;
import com.antonina.socialsynchro.services.facebook.database.rows.FacebookAccountInfoRow;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FacebookAccountInfoRepository extends BaseRepository<FacebookAccountInfoRow, FacebookAccount> {
    private static FacebookAccountInfoRepository instance;

    private FacebookAccountInfoRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.facebookAccountInfoDao();
    }

    public static FacebookAccountInfoRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new FacebookAccountInfoRepository(application);
    }

    @Override
    protected Map<Long, FacebookAccount> convertToEntities(List<FacebookAccountInfoRow> input) {
        return null;
    }

    @Override
    protected FacebookAccountInfoRow convertToRow(FacebookAccount entity) {
        FacebookAccountInfoRow data = new FacebookAccountInfoRow();
        data.createFromEntity(entity);
        return data;
    }

    @Override
    protected List<FacebookAccount> sortList(List<FacebookAccount> list) {
        return list;
    }

    public LiveData<FacebookAccountInfoRow> getDataTableByID(long id) {
        LiveData<FacebookAccountInfoRow> result = null;
        try {
            GetDataByIDAsyncTask<FacebookAccountInfoRow> asyncTask = new GetDataByIDAsyncTask<>(dao);
            result = asyncTask.execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
