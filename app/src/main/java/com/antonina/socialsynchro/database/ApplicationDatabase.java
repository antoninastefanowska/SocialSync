package com.antonina.socialsynchro.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import com.antonina.socialsynchro.base.ServiceID;
import com.antonina.socialsynchro.content.attachments.AttachmentTypeID;
import com.antonina.socialsynchro.database.daos.AccountDao;
import com.antonina.socialsynchro.database.daos.AttachmentDao;
import com.antonina.socialsynchro.database.daos.AttachmentTypeDao;
import com.antonina.socialsynchro.database.daos.ChildPostContainerDao;
import com.antonina.socialsynchro.database.daos.ParentPostContainerDao;
import com.antonina.socialsynchro.database.daos.PostDao;
import com.antonina.socialsynchro.database.daos.ServiceDao;
import com.antonina.socialsynchro.database.tables.AccountTable;
import com.antonina.socialsynchro.database.tables.AttachmentTable;
import com.antonina.socialsynchro.database.tables.AttachmentTypeTable;
import com.antonina.socialsynchro.database.tables.ChildPostContainerTable;
import com.antonina.socialsynchro.database.tables.ParentPostContainerTable;
import com.antonina.socialsynchro.database.tables.PostTable;
import com.antonina.socialsynchro.database.tables.ServiceTable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {AccountTable.class, ServiceTable.class, PostTable.class, AttachmentTable.class, AttachmentTypeTable.class, ChildPostContainerTable.class, ParentPostContainerTable.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class ApplicationDatabase extends RoomDatabase {
    private static volatile ApplicationDatabase instance;

    public abstract AccountDao accountDao();
    public abstract ServiceDao serviceDao();
    public abstract PostDao postDao();
    public abstract AttachmentDao attachmentDao();
    public abstract AttachmentTypeDao attachmentTypeDao();
    public abstract ChildPostContainerDao childPostContainerDao();
    public abstract ParentPostContainerDao parentPostContainerDao();

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
                                    getDatabase(context).attachmentTypeDao().insertMany(createAttachmentTypesData());
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
        for (ServiceID serviceID : ServiceID.values()) {
            ServiceTable serviceData = new ServiceTable((long)serviceID.ordinal(), serviceID.name(), "", "");
            servicesData.add(serviceData);
        }
        return servicesData;
    }

    private static List<AttachmentTypeTable> createAttachmentTypesData() {
        List<AttachmentTypeTable> attachmentTypeTablesData = new ArrayList<AttachmentTypeTable>();
        for (AttachmentTypeID attachmentTypeID : AttachmentTypeID.values()) {
            AttachmentTypeTable attachmentTypeData = new AttachmentTypeTable((long)attachmentTypeID.ordinal(), attachmentTypeID.name(), "");
            attachmentTypeTablesData.add(attachmentTypeData);
        }
        return attachmentTypeTablesData;
    }
}
