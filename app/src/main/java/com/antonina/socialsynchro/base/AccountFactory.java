package com.antonina.socialsynchro.base;

import com.antonina.socialsynchro.database.IDatabaseEntity;
import com.antonina.socialsynchro.database.tables.AccountTable;
import com.antonina.socialsynchro.database.tables.ITable;
import com.antonina.socialsynchro.services.twitter.TwitterAccount;

public class AccountFactory implements IFactory {
    private static AccountFactory instance;

    public static AccountFactory getInstance() {
        if (instance == null)
            instance = new AccountFactory();
        return instance;
    }

    private AccountFactory() { }

    @Override
    public IDatabaseEntity createFromData(ITable data) {
        AccountTable accountData = (AccountTable)data;
        ServiceID serviceID = ServiceID.values()[(int)accountData.serviceID];

        switch(serviceID) {
            case Twitter:
                return new TwitterAccount(data);
            default:
                return null;
        }
    }
}
