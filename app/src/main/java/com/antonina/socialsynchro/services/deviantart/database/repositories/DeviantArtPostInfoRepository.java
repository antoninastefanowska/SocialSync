package com.antonina.socialsynchro.services.deviantart.database.repositories;

import android.app.Application;

import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.repositories.BaseRepository;
import com.antonina.socialsynchro.services.deviantart.model.DeviantArtPostContainer;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtPostInfoRow;

public class DeviantArtPostInfoRepository extends BaseRepository<DeviantArtPostInfoRow, DeviantArtPostContainer> {
    private static DeviantArtPostInfoRepository instance;

    private DeviantArtPostInfoRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.deviantArtPostDao();
    }

    public static DeviantArtPostInfoRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new DeviantArtPostInfoRepository(application);
    }

    @Override
    protected DeviantArtPostContainer convertToEntity(DeviantArtPostInfoRow dataRow) {
        return null;
    }

    @Override
    protected DeviantArtPostInfoRow convertToDataRow(DeviantArtPostContainer entity) {
        DeviantArtPostInfoRow dataRow = new DeviantArtPostInfoRow();
        dataRow.createFromEntity(entity);
        return dataRow;
    }
}
