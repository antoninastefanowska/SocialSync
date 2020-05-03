package com.antonina.socialsynchro.services.facebook.database.repositories;

import android.app.Application;

import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.repositories.BaseRepository;
import com.antonina.socialsynchro.services.facebook.model.FacebookPostContainer;
import com.antonina.socialsynchro.services.facebook.database.rows.FacebookPostInfoRow;

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
    protected FacebookPostContainer convertToEntity(FacebookPostInfoRow dataRow) {
        return null;
    }

    @Override
    protected FacebookPostInfoRow convertToDataRow(FacebookPostContainer entity) {
        FacebookPostInfoRow dataRow = new FacebookPostInfoRow();
        dataRow.createFromEntity(entity);
        return dataRow;
    }
}
