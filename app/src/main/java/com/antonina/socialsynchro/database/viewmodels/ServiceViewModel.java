package com.antonina.socialsynchro.database.viewmodels;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.antonina.socialsynchro.base.Service;
import com.antonina.socialsynchro.database.repositories.ServiceRepository;
import com.antonina.socialsynchro.database.tables.ServiceTable;

import java.util.ArrayList;
import java.util.List;

public class ServiceViewModel extends AndroidViewModel {
    private static ServiceViewModel instance;
    private ServiceRepository serviceRepository;
    private LiveData<List<Service>> services;

    public static ServiceViewModel getInstance(@NonNull Application application) {
        if (instance == null)
            instance = new ServiceViewModel(application);
        return instance;
    }

    private ServiceViewModel(@NonNull Application application) {
        super(application);

        serviceRepository = new ServiceRepository(application);
        LiveData<List<ServiceTable>> servicesData = serviceRepository.getServices();

        services = Transformations.map(servicesData, new Function<List<ServiceTable>, List<Service>>() {
            @Override
            public List<Service> apply(List<ServiceTable> input) {
                List<Service> output = new ArrayList<Service>();
                for (ServiceTable serviceData : input) {
                    Service service = new Service(serviceData);
                    output.add(service);
                }
                return output;
            }
        });
    }

    public LiveData<List<Service>> getServices() { return services; }

    public LiveData<Service> getServiceById(long serviceId) {
        LiveData<ServiceTable> serviceData = serviceRepository.getServiceById(serviceId);
        LiveData<Service> service = Transformations.map(serviceData, new Function<ServiceTable, Service>() {
            @Override
            public Service apply (ServiceTable input) {
                Service output = new Service(input);
                return output;
            }
        });
        return service;
    }

    public int count() { return serviceRepository.count(); }
}