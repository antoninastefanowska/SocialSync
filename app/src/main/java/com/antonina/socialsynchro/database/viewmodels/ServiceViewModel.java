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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceViewModel extends AndroidViewModel implements IReadOnlyViewModel<Service> {
    private static ServiceViewModel instance;
    private ServiceRepository serviceRepository;
    private LiveData<Map<Long, Service>> services;

    public static ServiceViewModel getInstance(@NonNull Application application) {
        if (instance == null)
            instance = new ServiceViewModel(application);
        return instance;
    }

    private ServiceViewModel(@NonNull Application application) {
        super(application);

        serviceRepository = new ServiceRepository(application);
        LiveData<List<ServiceTable>> servicesData = serviceRepository.getAllData();

        services = Transformations.map(servicesData, new Function<List<ServiceTable>, Map<Long, Service>>() {
            @Override
            public Map<Long, Service> apply(List<ServiceTable> input) {
                Map<Long, Service> output = new HashMap<Long, Service>();
                for (ServiceTable serviceData : input) {
                    Service service = new Service(serviceData);
                    output.put(service.getID(), service);
                }
                return output;
            }
        });
    }

    @Override
    public LiveData<Map<Long, Service>> getAllEntities() { return services; }

    @Override
    public LiveData<Service> getEntityByID(long serviceID) {
        final long id = serviceID;
        LiveData<Service> service = Transformations.map(services, new Function<Map<Long, Service>, Service>() {
            @Override
            public Service apply(Map<Long, Service> input) {
                return input.get(id);
            }
        });
        return service;
    }

    @Override
    public int count() {
        return serviceRepository.count();
    }
}