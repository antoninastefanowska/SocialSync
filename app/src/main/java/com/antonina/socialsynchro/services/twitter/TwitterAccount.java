package com.antonina.socialsynchro.services.twitter;

import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.database.tables.ITable;

public class TwitterAccount extends Account {
    public TwitterAccount(ITable table) {
        super(table);
    }

    // TODO: uwzględnić limity żądań
}
