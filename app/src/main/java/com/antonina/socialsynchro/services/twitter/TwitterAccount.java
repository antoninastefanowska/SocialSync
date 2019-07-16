package com.antonina.socialsynchro.services.twitter;

import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.database.tables.AccountTable;

public class TwitterAccount extends Account {
    public TwitterAccount(AccountTable table) {
        super(table);
    }

    // TODO: uwzględnić limity żądań
}
