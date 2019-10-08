package com.antonina.socialsynchro.common.database.repositories;

import android.app.Application;

import com.antonina.socialsynchro.common.content.posts.ParentPostContainer;
import com.antonina.socialsynchro.common.database.ApplicationDatabase;
import com.antonina.socialsynchro.common.database.rows.ParentPostContainerRow;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ParentPostContainerRepository extends BaseRepository<ParentPostContainerRow, ParentPostContainer> {
    private static ParentPostContainerRepository instance;

    private ParentPostContainerRepository(Application application) {
        ApplicationDatabase db = ApplicationDatabase.getDatabase(application);
        dao = db.parentPostContainerDao();
        loadAllData();
    }

    public static ParentPostContainerRepository getInstance() {
        return instance;
    }

    public static void createInstance(Application application) {
        instance = new ParentPostContainerRepository(application);
    }

    @Override
    protected List<ParentPostContainer> sortList(List<ParentPostContainer> list) {
        Collections.sort(list, new Comparator<ParentPostContainer>() {
            @Override
            public int compare(ParentPostContainer o1, ParentPostContainer o2) {
                return compareDates(o1.getCreationDate(), o2.getCreationDate());
            }
        });
        return list;
    }

    @Override
    protected Map<Long, ParentPostContainer> convertToEntities(List<ParentPostContainerRow> input) {
        Map<Long, ParentPostContainer> output = new TreeMap<>();
        for (ParentPostContainerRow parentPostContainerData : input) {
            ParentPostContainer parentPostContainer = new ParentPostContainer(parentPostContainerData);
            output.put(parentPostContainer.getInternalID(), parentPostContainer);
        }
        return output;
    }

    @Override
    protected ParentPostContainerRow convertToRow(ParentPostContainer entity) {
        ParentPostContainerRow data = new ParentPostContainerRow();
        data.createFromEntity(entity);
        return data;
    }
}
