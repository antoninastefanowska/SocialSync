package com.antonina.socialsynchro.database.repositories;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;

import com.antonina.socialsynchro.content.ChildPostContainer;
import com.antonina.socialsynchro.content.ChildPostContainerFactory;
import com.antonina.socialsynchro.content.ParentPostContainer;
import com.antonina.socialsynchro.database.ApplicationDatabase;
import com.antonina.socialsynchro.database.daos.ChildPostContainerDao;
import com.antonina.socialsynchro.database.tables.ChildPostContainerTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ChildPostContainerRepository extends BaseRepository<ChildPostContainerTable, ChildPostContainer> {
    private static ChildPostContainerRepository instance;

    private ChildPostContainerRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.childPostContainerDao();
        loadAllData();
    }

    public static ChildPostContainerRepository getInstance(Application application) {
        if (instance == null)
            instance = new ChildPostContainerRepository(application);
        return instance;
    }

    @Override
    protected Map<Long, ChildPostContainer> convertToEntities(List<ChildPostContainerTable> input) {
        Map<Long, ChildPostContainer> output = new HashMap<Long, ChildPostContainer>();
        for (ChildPostContainerTable data : input) {
            ChildPostContainer childPostContainer = (ChildPostContainer)ChildPostContainerFactory.getInstance().createFromData(data);
            output.put(childPostContainer.getInternalID(), childPostContainer);
        }
        return output;
    }

    @Override
    protected ChildPostContainerTable convertToTable(ChildPostContainer entity, boolean isNew) {
        ChildPostContainerTable data = new ChildPostContainerTable();
        if (isNew)
            data.createFromNewEntity(entity);
        else
            data.createFromExistingEntity(entity);
        return data;
    }

    public LiveData<List<ChildPostContainer>> getDataByParent(ParentPostContainer parent) {
        long parentID = parent.getInternalID();
        LiveData<List<ChildPostContainer>> result = null;
        try {
            ChildPostContainerDao childPostContainerDao = (ChildPostContainerDao)dao;
            LiveData<List<ChildPostContainerTable>> databaseData = new GetDataByParentAsyncTask(childPostContainerDao).execute(parentID).get();
            result = Transformations.map(databaseData, new Function<List<ChildPostContainerTable>, List<ChildPostContainer>>() {
                @Override
                public List<ChildPostContainer> apply(List<ChildPostContainerTable> input) {
                    List<ChildPostContainer> output = new ArrayList<ChildPostContainer>();
                    for (ChildPostContainerTable childPostContainerData : input) {
                        ChildPostContainer childPostContainer = (ChildPostContainer)ChildPostContainerFactory.getInstance().createFromData(childPostContainerData);
                        output.add(childPostContainer);
                    }
                    return output;
                }
            });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static class GetDataByParentAsyncTask extends AsyncTask<Long, Void, LiveData<List<ChildPostContainerTable>>> {
        private ChildPostContainerDao dao;

        public GetDataByParentAsyncTask(ChildPostContainerDao dao) { this.dao = dao; }

        @Override
        protected LiveData<List<ChildPostContainerTable>> doInBackground(final Long... params) {
            return dao.getDataByParent(params[0]);
        }
    }
}
