package com.antonina.socialsynchro.common.database.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.antonina.socialsynchro.common.model.accounts.Account;
import com.antonina.socialsynchro.common.model.services.Service;
import com.antonina.socialsynchro.common.database.repositories.AccountRepository;
import com.antonina.socialsynchro.common.database.rows.AccountRow;

import java.util.List;

public class AccountViewModel extends BaseViewModel<AccountRow, Account> {
    public AccountViewModel(@NonNull Application application) {
        super(application);
        data = getAllData();
    }

    @Override
    protected AccountRepository getRepository(Application application) {
        return AccountRepository.getInstance();
    }

    public LiveData<List<Account>> getDataByService(Service service) {
        return ((AccountRepository)repository).getDataByService(service);
    }

    public long getIDByExternalIDAndService(String externalID, Service service) {
        return ((AccountRepository)repository).getIDByExternalIDAndService(externalID, service);
    }

    public boolean accountExists(String externalID, Service service) {
        return ((AccountRepository)repository).accountExists(externalID, service);
    }
}
