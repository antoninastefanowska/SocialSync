package com.antonina.socialsynchro.services.facebook;

import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.database.tables.AccountTable;
import com.antonina.socialsynchro.gui.listeners.OnSynchronizedListener;
import com.antonina.socialsynchro.services.IResponse;

public class FacebookAccount extends Account {

    public FacebookAccount(AccountTable table) {
        super(table);
    }

    @Override
    public void createFromResponse(IResponse response) {

    }

    @Override
    public void synchronize(OnSynchronizedListener listener) {

    }
}
