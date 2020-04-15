package com.antonina.socialsynchro.services.twitter.database.repositories;

import android.app.Application;

import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.repositories.BaseRepository;
import com.antonina.socialsynchro.services.twitter.content.TwitterAccount;
import com.antonina.socialsynchro.services.twitter.database.rows.TwitterAccountInfoRow;

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
    protected TwitterAccount convertToEntity(TwitterAccountInfoRow dataRow) {
        return null;
    }

    @Override
    protected TwitterAccountInfoRow convertToDataRow(TwitterAccount entity) {
        TwitterAccountInfoRow dataRow = new TwitterAccountInfoRow();
        dataRow.createFromEntity(entity);
        return dataRow;
    }
}
