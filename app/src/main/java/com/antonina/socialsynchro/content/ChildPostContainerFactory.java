package com.antonina.socialsynchro.content;

import com.antonina.socialsynchro.base.IFactory;
import com.antonina.socialsynchro.base.ServiceID;
import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.tables.ChildPostContainerTable;
import com.antonina.socialsynchro.database.tables.ITable;
import com.antonina.socialsynchro.services.twitter.TwitterPostContainer;

public class ChildPostContainerFactory implements IFactory {
    private static ChildPostContainerFactory instance;

    public static ChildPostContainerFactory getInstance() {
        if (instance == null)
            instance = new ChildPostContainerFactory();
        return instance;
    }

    @Override
    public IDatabaseEntity createFromData(ITable data) {
        ChildPostContainerTable childPostContainerData = (ChildPostContainerTable)data;
        ServiceID serviceID = ServiceID.values()[(int)childPostContainerData.serviceID];

        switch(serviceID) {
            case Twitter:
                return new TwitterPostContainer(data);
            default:
                return null;
        }
    }
}
