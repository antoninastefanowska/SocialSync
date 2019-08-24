package com.antonina.socialsynchro.gui.dialogs;

import com.antonina.socialsynchro.base.Account;

import java.util.List;

public interface ChooseAccountDialogListener {
    void onAccountsSelected(List<Account> accounts);
}
