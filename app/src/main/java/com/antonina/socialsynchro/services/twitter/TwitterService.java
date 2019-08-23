package com.antonina.socialsynchro.services.twitter;

import com.antonina.socialsynchro.services.IService;
import com.antonina.socialsynchro.services.ServiceID;

public class TwitterService implements IService {
    private static TwitterService instance;

    public static TwitterService getInstance() {
        if (instance == null)
            instance = new TwitterService();
        return instance;
    }

    private TwitterService() { }

    @Override
    public ServiceID getID() {
        return ServiceID.Twitter;
    }

    @Override
    public String getName() {
        return "Twitter";
    }

    @Override
    public String getLogoUrl() {
        return null;
    }

    @Override
    public String getColorName() {
        return null;
    }
}
