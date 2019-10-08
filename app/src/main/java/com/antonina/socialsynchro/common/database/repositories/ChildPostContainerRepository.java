package com.antonina.socialsynchro.common.database.repositories;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;
import android.util.Pair;

import com.antonina.socialsynchro.common.content.posts.ChildPostContainer;
import com.antonina.socialsynchro.common.content.posts.ChildPostContainerFactory;
import com.antonina.socialsynchro.common.content.posts.ParentPostContainer;
import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.daos.ChildPostContainerDao;
import com.antonina.socialsynchro.common.database.rows.ChildPostContainerRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("WeakerAccess")
public class ChildPostContainerRepository extends BaseRepository<ChildPostContainerRow, ChildPostContainer> {
    private static ChildPostContainerRepository instance;

    private ChildPostContainerRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.childPostContainerDao();
        loadAllData();
    }

    public static ChildPostContainerRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new ChildPostContainerRepository(application);
    }

    @Override
    protected Map<Long, ChildPostContainer> convertToEntities(List<ChildPostContainerRow> input) {
        Map<Long, ChildPostContainer> output = new TreeMap<>();
        for (ChildPostContainerRow data : input) {
            ChildPostContainer childPostContainer = ChildPostContainerFactory.getInstance().createFromDatabaseRow(data);
            output.put(childPostContainer.getInternalID(), childPostContainer);
        }
        return output;
    }

    @Override
    protected ChildPostContainerRow convertToRow(ChildPostContainer entity) {
        ChildPostContainerRow data = new ChildPostContainerRow();
        data.createFromEntity(entity);
        return data;
    }

    @Override
    protected List<ChildPostContainer> sortList(List<ChildPostContainer> list) {
        Collections.sort(list, new Comparator<ChildPostContainer>() {
            @Override
            public int compare(ChildPostContainer o1, ChildPostContainer o2) {
                return compareDates(o1.getCreationDate(), o2.getCreationDate());
            }
        });
        return list;
    }

    public LiveData<List<ChildPostContainer>> getDataByParent(ParentPostContainer parent) {
        long parentID = parent.getInternalID();
        LiveData<List<ChildPostContainer>> result = null;
        try {
            ChildPostContainerDao childPostContainerDao = (ChildPostContainerDao)dao;
            LiveData<List<Long>> IDs = new GetIDByParentAsyncTask(childPostContainerDao).execute(parentID).get();
            FilterSource<ChildPostContainer> filterSource = new FilterSource<>(IDs, getAllData());

            result = Transformations.map(filterSource, new Function<Pair<List<Long>, Map<Long, ChildPostContainer>>, List<ChildPostContainer>>() {
                @Override
                public List<ChildPostContainer> apply(Pair<List<Long>, Map<Long, ChildPostContainer>> input) {
                    List<ChildPostContainer> output = new ArrayList<>();
                    for (Long id : input.first)
                        output.add(input.second.get(id));
                    return sortList(output);
                }
            });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static class GetIDByParentAsyncTask extends AsyncTask<Long, Void, LiveData<List<Long>>> {
        private final ChildPostContainerDao dao;

        public GetIDByParentAsyncTask(ChildPostContainerDao dao) { this.dao = dao; }

        @Override
        protected LiveData<List<Long>> doInBackground(final Long... params) {
            return dao.getIDByParent(params[0]);
        }
    }
}