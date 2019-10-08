package com.antonina.socialsynchro;

import android.app.Application;

import com.antonina.socialsynchro.common.database.repositories.AccountRepository;
import com.antonina.socialsynchro.common.database.repositories.AttachmentRepository;
import com.antonina.socialsynchro.common.database.repositories.ChildPostContainerRepository;
import com.antonina.socialsynchro.common.database.repositories.ParentPostContainerRepository;
import com.antonina.socialsynchro.common.database.repositories.PostRepository;
import com.antonina.socialsynchro.common.database.repositories.RequestLimitRepository;
import com.antonina.socialsynchro.services.twitter.database.repositories.TwitterAccountInfoRepository;
import com.antonina.socialsynchro.common.utils.ApplicationConfig;

public class SocialSynchro extends Application {
    private static Application instance;

    public void onCreate() {
        super.onCreate();

        ApplicationConfig.createInstance(this);

        AccountRepository.createInstance(this);
        PostRepository.createInstance(this);
        AttachmentRepository.createInstance(this);
        ParentPostContainerRepository.createInstance(this);
        ChildPostContainerRepository.createInstance(this);
        RequestLimitRepository.createInstance(this);

        TwitterAccountInfoRepository.createInstance(this);

        instance = this;
    }

    public static Application getInstance() {
        return instance;
    }
}
