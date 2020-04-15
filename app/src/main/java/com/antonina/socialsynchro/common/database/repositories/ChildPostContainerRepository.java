package com.antonina.socialsynchro.common.database.repositories;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;

import com.antonina.socialsynchro.common.content.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.content.posts.ParentPostContainer;
import com.antonina.socialsynchro.common.content.services.Service;
import com.antonina.socialsynchro.common.content.services.Services;
import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.daos.ChildPostContainerDao;
import com.antonina.socialsynchro.common.database.rows.ChildPostContainerRow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChildPostContainerRepository extends BaseRepository<ChildPostContainerRow, ChildPostContainer> {
    private static ChildPostContainerRepository instance;

    private ChildPostContainerRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.childPostContainerDao();
    }

    public static ChildPostContainerRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new ChildPostContainerRepository(application);
    }

    @Override
    protected ChildPostContainer convertToEntity(ChildPostContainerRow dataRow) {
        Service service = Services.getService(dataRow.serviceID);
        return service.createPostContainer(dataRow);
    }

    @Override
    protected ChildPostContainerRow convertToDataRow(ChildPostContainer entity) {
        ChildPostContainerRow dataRow = new ChildPostContainerRow();
        dataRow.createFromEntity(entity);
        return dataRow;
    }

    public LiveData<List<ChildPostContainer>> getDataByParent(ParentPostContainer parent) {
        try {
            long parentID = parent.getInternalID();
            ChildPostContainerDao childPostContainerDao = (ChildPostContainerDao)dao;
            GetDataByParentAsyncTask asyncTask = new GetDataByParentAsyncTask(childPostContainerDao);
            LiveData<List<ChildPostContainerRow>> dataRows = asyncTask.execute(parentID).get();
            LiveData<List<ChildPostContainer>> entities = Transformations.map(dataRows, new Function<List<ChildPostContainerRow>, List<ChildPostContainer>>() {
                @Override
                public List<ChildPostContainer> apply(List<ChildPostContainerRow> input) {
                    List<ChildPostContainer> output = new ArrayList<ChildPostContainer>();
                    for (ChildPostContainerRow dataRow : input)
                        output.add(convertToEntity(dataRow));
                    return output;
                }
            });
            return entities;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class GetDataByParentAsyncTask extends AsyncTask<Long, Void, LiveData<List<ChildPostContainerRow>>> {
        private final ChildPostContainerDao dao;

        public GetDataByParentAsyncTask(ChildPostContainerDao childPostContainerDao) {
            this.dao = childPostContainerDao;
        }

        @Override
        protected LiveData<List<ChildPostContainerRow>> doInBackground(Long... params) {
            return dao.getDataByParent(params[0]);
        }
    }
}
