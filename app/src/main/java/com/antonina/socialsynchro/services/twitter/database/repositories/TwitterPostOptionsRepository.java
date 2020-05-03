package com.antonina.socialsynchro.services.twitter.database.repositories;

import android.app.Application;

import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.repositories.BaseRepository;
import com.antonina.socialsynchro.services.twitter.model.TwitterPostOptions;
import com.antonina.socialsynchro.services.twitter.database.rows.TwitterPostOptionsRow;

public class TwitterPostOptionsRepository extends BaseRepository<TwitterPostOptionsRow, TwitterPostOptions> {
    private static TwitterPostOptionsRepository instance;

    private TwitterPostOptionsRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.twitterPostOptionsDao();
    }

    public static TwitterPostOptionsRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new TwitterPostOptionsRepository(application);
    }

    @Override
    protected TwitterPostOptions convertToEntity(TwitterPostOptionsRow dataRow) {
        return new TwitterPostOptions(dataRow);
    }

    @Override
    protected TwitterPostOptionsRow convertToDataRow(TwitterPostOptions entity) {
        TwitterPostOptionsRow dataRow = new TwitterPostOptionsRow();
        dataRow.createFromEntity(entity);
        return dataRow;
    }
}
