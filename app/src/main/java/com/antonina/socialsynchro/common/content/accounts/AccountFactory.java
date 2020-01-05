package com.antonina.socialsynchro.common.content.accounts;

import com.antonina.socialsynchro.common.content.services.ServiceID;
import com.antonina.socialsynchro.common.database.IDatabaseEntityFactory;
import com.antonina.socialsynchro.common.database.rows.AccountRow;
import com.antonina.socialsynchro.common.database.rows.IDatabaseRow;
import com.antonina.socialsynchro.services.deviantart.content.DeviantArtAccount;
import com.antonina.socialsynchro.services.facebook.content.FacebookAccount;
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
    public Account createFromDatabaseRow(IDatabaseRow data) {
        AccountRow accountData = (AccountRow)data;
        ServiceID serviceID = ServiceID.values()[accountData.serviceID];

        switch (serviceID) {
            case Twitter:
                return new TwitterAccount(data);
            case Facebook:
                return new FacebookAccount(data);
            case DeviantArt:
                return new DeviantArtAccount(data);
            default:
                return null;
        }
    }
}
