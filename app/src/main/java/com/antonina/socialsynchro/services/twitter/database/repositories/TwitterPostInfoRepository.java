package com.antonina.socialsynchro.services.twitter.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.repositories.BaseRepository;
import com.antonina.socialsynchro.services.twitter.content.TwitterPostContainer;
import com.antonina.socialsynchro.services.twitter.database.rows.TwitterPostInfoRow;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class TwitterPostInfoRepository extends BaseRepository<TwitterPostInfoRow, TwitterPostContainer> {
    private static TwitterPostInfoRepository instance;

    private TwitterPostInfoRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.twitterPostDao();
    }

    public static TwitterPostInfoRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new TwitterPostInfoRepository(application);
    }

    @Override
    protected Map<Long, TwitterPostContainer> convertToEntities(List<TwitterPostInfoRow> input) {
        return null;
    }

    @Override
    protected TwitterPostInfoRow convertToRow(TwitterPostContainer entity) {
        TwitterPostInfoRow data = new TwitterPostInfoRow();
        data.createFromEntity(entity);
        return data;
    }

    @Override
    protected List<TwitterPostContainer> sortList(List<TwitterPostContainer> list) {
        return list;
    }

    public LiveData<TwitterPostInfoRow> getDataTableByID(long id) {
        LiveData<TwitterPostInfoRow> result = null;
        try {
            GetDataByIDAsyncTask<TwitterPostInfoRow> asyncTask = new GetDataByIDAsyncTask<>(dao);
            result = asyncTask.execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
