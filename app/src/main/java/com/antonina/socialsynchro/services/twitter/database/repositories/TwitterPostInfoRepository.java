package com.antonina.socialsynchro.services.twitter.database.repositories;

import android.app.Application;

import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.repositories.BaseRepository;
import com.antonina.socialsynchro.services.twitter.model.TwitterPostContainer;
import com.antonina.socialsynchro.services.twitter.database.rows.TwitterPostInfoRow;

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
    protected TwitterPostContainer convertToEntity(TwitterPostInfoRow dataRow) {
        return null;
    }

    @Override
    protected TwitterPostInfoRow convertToDataRow(TwitterPostContainer entity) {
        TwitterPostInfoRow dataRow = new TwitterPostInfoRow();
        dataRow.createFromEntity(entity);
        return dataRow;
    }
}
