package com.antonina.socialsynchro.database.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.antonina.socialsynchro.database.ApplicationDatabase;
import com.antonina.socialsynchro.database.daos.ChildPostContainerDao;
import com.antonina.socialsynchro.database.tables.ChildPostContainerTable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChildPostContainerRepository extends EditableRepository<ChildPostContainerTable> {
    public ChildPostContainerRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.childPostContainerDao();
    }

    public LiveData<List<ChildPostContainerTable>> getDataByParent(long parentID) {
        LiveData<List<ChildPostContainerTable>> result = null;
        try {
            ChildPostContainerDao childPostContainerDao = (ChildPostContainerDao)dao;
            result = new GetDataByParentAsyncTask(childPostContainerDao).execute(parentID).get();
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
