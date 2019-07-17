package com.antonina.socialsynchro.base;

import com.antonina.socialsynchro.database.tables.AccountTable;
import com.antonina.socialsynchro.database.tables.ITable;
import com.antonina.socialsynchro.services.twitter.TwitterAccount;

public class AccountFactory {
    private static AccountFactory instance;

    public static AccountFactory getInstance() {
        if (instance == null)
            instance = new AccountFactory();
        return instance;
    }

    private AccountFactory() { }

    public Account makeAccount(ITable table) {
        AccountTable accountTable = (AccountTable)table;
        ServiceID serviceId = ServiceID.values()[(int)accountTable.serviceId];

        switch (serviceId) {
            case Twitter:
                return new TwitterAccount(table);
            default:
                return null;
        }
    }
}
