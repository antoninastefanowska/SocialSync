package com.antonina.socialsynchro.content;

import android.util.Log;

import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.database.IDatabaseEntityFactory;
import com.antonina.socialsynchro.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.services.ServiceID;
import com.antonina.socialsynchro.database.tables.ChildPostContainerTable;
import com.antonina.socialsynchro.services.twitter.TwitterAccount;
import com.antonina.socialsynchro.services.twitter.TwitterPostContainer;

public class ChildPostContainerFactory implements IDatabaseEntityFactory {
    private static ChildPostContainerFactory instance;

    public static ChildPostContainerFactory getInstance() {
        if (instance == null)
            instance = new ChildPostContainerFactory();
        return instance;
    }

    @Override
    public ChildPostContainer createFromData(IDatabaseTable data) {
        ChildPostContainerTable childPostContainerData = (ChildPostContainerTable)data;
        ServiceID serviceID = ServiceID.values()[(int)childPostContainerData.serviceID];

        switch(serviceID) {
            case Twitter:
                return new TwitterPostContainer(data);
            default:
                return null;
        }
    }

    public ChildPostContainer createNew(ParentPostContainer parent, Account account) {
        ServiceID serviceID = account.getService().getID();

        switch(serviceID) {
            case Twitter:
                TwitterAccount twitterAccount = (TwitterAccount)account;
                return new TwitterPostContainer(parent, twitterAccount);
            default:
                return null;
        }
    }
}
