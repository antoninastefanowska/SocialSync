package com.antonina.socialsynchro.services;

import com.antonina.socialsynchro.services.twitter.TwitterService;

public class Services {
    private static final IService[] services = new IService[ServiceID.values().length];
    private static boolean initialized = false;

    private Services() { }

    private static void init() {
        services[ServiceID.Twitter.ordinal()] = TwitterService.getInstance();
        initialized = true;
    }

    public static IService getService(ServiceID serviceID) {
        if (!initialized)
            init();
        return services[serviceID.ordinal()];
    }

    public static IService getService(int serviceID) {
        if (!initialized)
            init();
        return services[serviceID];
    }

    public static IService[] getServices() {
        if (!initialized)
            init();
        return services;
    }
}
