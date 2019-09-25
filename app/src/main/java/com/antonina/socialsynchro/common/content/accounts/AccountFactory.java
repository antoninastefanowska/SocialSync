package com.antonina.socialsynchro.common.content.accounts;

import com.antonina.socialsynchro.common.content.services.ServiceID;
import com.antonina.socialsynchro.common.database.IDatabaseEntityFactory;
import com.antonina.socialsynchro.common.database.tables.AccountTable;
import com.antonina.socialsynchro.common.database.tables.IDatabaseTable;
import com.antonina.socialsynchro.services.twitter.content.TwitterAccount;

public class AccountFactory implements IDatabaseEntityFactory {
    private static AccountFactory instance;

    public static AccountFactory getInstance() {
        if (instance == null)
            instance = new AccountFactory();
        return instance;
    }

    private AccountFactory() { }

    @Override
    public Account createFromData(IDatabaseTable data) {
        AccountTable accountData = (AccountTable)data;
        ServiceID serviceID = ServiceID.values()[accountData.serviceID];

        switch (serviceID) {
            case Twitter:
                return new TwitterAccount(data);
            default:
                return null;
        }
    }
}
