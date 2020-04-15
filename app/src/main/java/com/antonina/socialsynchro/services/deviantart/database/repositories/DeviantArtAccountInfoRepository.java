package com.antonina.socialsynchro.services.deviantart.database.repositories;

import android.app.Application;

import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.repositories.BaseRepository;
import com.antonina.socialsynchro.services.deviantart.content.DeviantArtAccount;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtAccountInfoRow;

public class DeviantArtAccountInfoRepository extends BaseRepository<DeviantArtAccountInfoRow, DeviantArtAccount> {
    private static DeviantArtAccountInfoRepository instance;

    private DeviantArtAccountInfoRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.deviantArtAccountDao();
    }

    public static DeviantArtAccountInfoRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new DeviantArtAccountInfoRepository(application);
    }

    @Override
    protected DeviantArtAccount convertToEntity(DeviantArtAccountInfoRow dataRow) {
        return null;
    }

    @Override
    protected DeviantArtAccountInfoRow convertToDataRow(DeviantArtAccount entity) {
        DeviantArtAccountInfoRow dataRow = new DeviantArtAccountInfoRow();
        dataRow.createFromEntity(entity);
        return dataRow;
    }
}
