package com.antonina.socialsynchro;

import android.app.Application;
import android.content.Context;

import com.antonina.socialsynchro.database.repositories.AccountRepository;
import com.antonina.socialsynchro.database.repositories.AttachmentRepository;
import com.antonina.socialsynchro.database.repositories.ChildPostContainerRepository;
import com.antonina.socialsynchro.database.repositories.ParentPostContainerRepository;
import com.antonina.socialsynchro.database.repositories.PostRepository;
import com.antonina.socialsynchro.database.repositories.TwitterAccountInfoRepository;
import com.antonina.socialsynchro.services.ApplicationConfig;

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
        TwitterAccountInfoRepository.createInstance(this);

        instance = this;
    }

    public static Application getInstance() {
        return instance;
    }
}
