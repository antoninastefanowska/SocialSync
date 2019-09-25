package com.antonina.socialsynchro.common.content.services;

import com.antonina.socialsynchro.services.twitter.content.TwitterService;

public class Services {
    private static final Service[] services = new Service[ServiceID.values().length];
    private static boolean initialized = false;

    private Services() { }

    private static void init() {
        services[ServiceID.Twitter.ordinal()] = TwitterService.getInstance();
        initialized = true;
    }

    public static Service getService(ServiceID serviceID) {
        if (!initialized)
            init();
        return services[serviceID.ordinal()];
    }

    public static Service getService(int serviceID) {
        if (!initialized)
            init();
        return services[serviceID];
    }

    public static Service[] getServices() {
        if (!initialized)
            init();
        return services;
    }
}
