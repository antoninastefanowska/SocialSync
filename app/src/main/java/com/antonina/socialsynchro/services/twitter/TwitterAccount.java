package com.antonina.socialsynchro.services.twitter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.antonina.socialsynchro.SocialSynchro;
import com.antonina.socialsynchro.base.Account;
import com.antonina.socialsynchro.base.Service;
import com.antonina.socialsynchro.base.ServiceID;
import com.antonina.socialsynchro.database.tables.ITable;
import com.antonina.socialsynchro.database.viewmodels.ServiceViewModel;

public class TwitterAccount extends Account {
    public TwitterAccount(ITable table) {
        super(table);
    }

    public TwitterAccount() {
        ServiceViewModel serviceViewModel = ServiceViewModel.getInstance(SocialSynchro.getInstance());
        final TwitterAccount instance = this;
        final LiveData<Service> serviceLiveData = serviceViewModel.getEntityByID(ServiceID.Twitter.ordinal());
        serviceLiveData.observeForever(new Observer<Service>() {
            @Override
            public void onChanged(@Nullable Service service) {
                instance.setService(service);
            }
        });
    }

    // TODO: uwzględnić limity żądań
}
