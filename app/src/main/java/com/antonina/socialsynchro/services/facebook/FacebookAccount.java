package com.antonina.socialsynchro.services.facebook;

import com.antonina.socialsynchro.common.content.accounts.Account;
import com.antonina.socialsynchro.common.database.rows.AccountRow;
import com.antonina.socialsynchro.common.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.common.rest.IResponse;

public class FacebookAccount extends Account {

    public FacebookAccount(AccountRow table) {
        super(table);
    }

    @Override
    public void createFromResponse(IResponse response) {

    }

    @Override
    public void synchronize(OnSynchronizedListener listener) {

    }

    @Override
    public void synchronizeRequestLimits(OnSynchronizedListener listener) {

    }
}
