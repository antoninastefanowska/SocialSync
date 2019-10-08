package com.antonina.socialsynchro.services.twitter.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.repositories.BaseRepository;
import com.antonina.socialsynchro.services.twitter.content.TwitterAccount;
import com.antonina.socialsynchro.services.twitter.database.rows.TwitterAccountInfoRow;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TwitterAccountInfoRepository extends BaseRepository<TwitterAccountInfoRow, TwitterAccount> {
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
    protected Map<Long, TwitterAccount> convertToEntities(List<TwitterAccountInfoRow> input) {
        return null;
    }

    @Override
    protected TwitterAccountInfoRow convertToRow(TwitterAccount entity) {
        TwitterAccountInfoRow data = new TwitterAccountInfoRow();
        data.createFromEntity(entity);
        return data;
    }

    @Override
    protected List<TwitterAccount> sortList(List<TwitterAccount> list) {
        return list;
    }

    public LiveData<TwitterAccountInfoRow> getDataTableByID(long id) {
        LiveData<TwitterAccountInfoRow> result = null;
        try {
            GetDataByIDAsyncTask<TwitterAccountInfoRow> asyncTask = new GetDataByIDAsyncTask<>(dao);
            result = asyncTask.execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
