package com.antonina.socialsynchro.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.antonina.socialsynchro.base.ServiceID;
import com.antonina.socialsynchro.database.daos.AccountDao;
import com.antonina.socialsynchro.database.daos.ServiceDao;
import com.antonina.socialsynchro.database.tables.AccountTable;
import com.antonina.socialsynchro.database.tables.ServiceTable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {AccountTable.class, ServiceTable.class}, version = 1, exportSchema = false)
public abstract class ApplicationDatabase extends RoomDatabase {
    private static volatile ApplicationDatabase instance;

    public abstract AccountDao accountDao();
    public abstract ServiceDao serviceDao();

    public static ApplicationDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (ApplicationDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), ApplicationDatabase.class, "SocialSynchro Database").addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    getDatabase(context).serviceDao().insertMany(createServicesData());
                                }
                            });
                        }
                    }).build();
                }
            }
        }
        return instance;
    }

    private static List<ServiceTable> createServicesData() {
        List<ServiceTable> servicesData = new ArrayList<ServiceTable>();
        ServiceTable serviceData = new ServiceTable();
        serviceData.id = Long.valueOf(ServiceID.Twitter.ordinal());
        serviceData.name = "Twitter";
        servicesData.add(serviceData);
        return servicesData;
    }
}
