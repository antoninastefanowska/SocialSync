package com.antonina.socialsynchro.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.antonina.socialsynchro.database.ApplicationDatabase;
import com.antonina.socialsynchro.database.tables.TwitterAccountInfoTable;
import com.antonina.socialsynchro.services.twitter.TwitterAccount;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TwitterAccountInfoRepository extends BaseRepository<TwitterAccountInfoTable, TwitterAccount> {
    private static TwitterAccountInfoRepository instance;

    private TwitterAccountInfoRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.twitterAccountDao();
    }

    public static TwitterAccountInfoRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new TwitterAccountInfoRepository(application);
    }

    @Override
    protected Map<Long, TwitterAccount> convertToEntities(List<TwitterAccountInfoTable> input) {
        return null;
    }

    @Override
    protected TwitterAccountInfoTable convertToTable(TwitterAccount entity, boolean isNew) {
        TwitterAccountInfoTable data = new TwitterAccountInfoTable();
        data.createFromExistingEntity(entity);
        return data;
    }

    public LiveData<TwitterAccountInfoTable> getDataTableByID(long id) {
        LiveData<TwitterAccountInfoTable> result = null;
        try {
            result = new GetDataByIDAsyncTask<TwitterAccountInfoTable>(dao).execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
