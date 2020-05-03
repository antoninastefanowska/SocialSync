package com.antonina.socialsynchro.services.facebook.database.repositories;

import android.app.Application;

import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.repositories.BaseRepository;
import com.antonina.socialsynchro.services.facebook.model.FacebookAccount;
import com.antonina.socialsynchro.services.facebook.database.rows.FacebookAccountInfoRow;

public class FacebookAccountInfoRepository extends BaseRepository<FacebookAccountInfoRow, FacebookAccount> {
    private static FacebookAccountInfoRepository instance;

    private FacebookAccountInfoRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.facebookAccountDao();
    }

    public static FacebookAccountInfoRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new FacebookAccountInfoRepository(application);
    }

    @Override
    protected FacebookAccount convertToEntity(FacebookAccountInfoRow dataRow) {
        return null;
    }

    @Override
    protected FacebookAccountInfoRow convertToDataRow(FacebookAccount entity) {
        FacebookAccountInfoRow dataRow = new FacebookAccountInfoRow();
        dataRow.createFromEntity(entity);
        return dataRow;
    }
}
