package com.antonina.socialsynchro.services.deviantart.database.repositories;

import android.app.Application;

import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.repositories.BaseRepository;
import com.antonina.socialsynchro.services.deviantart.model.DeviantArtPostOptions;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtPostOptionsRow;

public class DeviantArtPostOptionsRepository extends BaseRepository<DeviantArtPostOptionsRow, DeviantArtPostOptions> {
    private static DeviantArtPostOptionsRepository instance;

    private DeviantArtPostOptionsRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.deviantArtPostOptionsDao();
    }

    public static DeviantArtPostOptionsRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new DeviantArtPostOptionsRepository(application);
    }

    @Override
    protected DeviantArtPostOptions convertToEntity(DeviantArtPostOptionsRow dataRow) {
        return new DeviantArtPostOptions(dataRow);
    }

    @Override
    protected DeviantArtPostOptionsRow convertToDataRow(DeviantArtPostOptions entity) {
        DeviantArtPostOptionsRow dataRow = new DeviantArtPostOptionsRow();
        dataRow.createFromEntity(entity);
        return dataRow;
    }
}
