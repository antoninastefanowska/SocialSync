package com.antonina.socialsynchro.common.database.viewmodels;

import android.app.Application;
import android.support.annotation.NonNull;

import com.antonina.socialsynchro.common.content.posts.ParentPostContainer;
import com.antonina.socialsynchro.common.database.repositories.ParentPostContainerRepository;
import com.antonina.socialsynchro.common.database.rows.ParentPostContainerRow;

public class ParentPostContainerViewModel extends BaseViewModel<ParentPostContainerRow, ParentPostContainer> {
    public ParentPostContainerViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected ParentPostContainerRepository getRepository(Application application) {
        return ParentPostContainerRepository.getInstance();
    }
}
