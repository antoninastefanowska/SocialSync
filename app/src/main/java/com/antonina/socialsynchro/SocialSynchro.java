package com.antonina.socialsynchro;

import android.app.Application;
import android.content.Context;

import com.antonina.socialsynchro.database.repositories.AccountRepository;
import com.antonina.socialsynchro.database.repositories.AttachmentRepository;
import com.antonina.socialsynchro.database.repositories.ChildPostContainerRepository;
import com.antonina.socialsynchro.database.repositories.ParentPostContainerRepository;
import com.antonina.socialsynchro.database.repositories.PostRepository;
import com.antonina.socialsynchro.database.repositories.TwitterAccountInfoRepository;

public class SocialSynchro extends Application {
    private static Context context;
    private static Application instance;

    public void onCreate() {
        super.onCreate();

        AccountRepository.createInstance(this);
        PostRepository.createInstance(this);
        AttachmentRepository.createInstance(this);
        ParentPostContainerRepository.createInstance(this);
        ChildPostContainerRepository.createInstance(this);
        TwitterAccountInfoRepository.createInstance(this);

        context = getApplicationContext();
        instance = this;
    }

    public static Context getAppContext() {
        return context;
    }

    public static Application getInstance() { return instance; }
}
