package com.antonina.socialsynchro.common.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import com.antonina.socialsynchro.common.database.daos.AccountDao;
import com.antonina.socialsynchro.common.database.daos.AttachmentDao;
import com.antonina.socialsynchro.common.database.daos.ChildPostContainerDao;
import com.antonina.socialsynchro.common.database.daos.ParentPostContainerDao;
import com.antonina.socialsynchro.common.database.daos.PostDao;
import com.antonina.socialsynchro.common.database.daos.RequestLimitDao;
import com.antonina.socialsynchro.common.database.rows.AttachmentRow;
import com.antonina.socialsynchro.common.database.rows.ParentPostContainerRow;
import com.antonina.socialsynchro.common.database.rows.RequestLimitRow;
import com.antonina.socialsynchro.services.twitter.database.daos.TwitterAccountInfoDao;
import com.antonina.socialsynchro.common.database.rows.AccountRow;
import com.antonina.socialsynchro.common.database.rows.ChildPostContainerRow;
import com.antonina.socialsynchro.common.database.rows.PostRow;
import com.antonina.socialsynchro.services.twitter.database.rows.TwitterAccountInfoRow;

@Database(entities = {AccountRow.class, PostRow.class, AttachmentRow.class, ChildPostContainerRow.class, ParentPostContainerRow.class, RequestLimitRow.class, TwitterAccountInfoRow.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class ApplicationDatabase extends RoomDatabase {
    private static volatile ApplicationDatabase instance;

    public abstract AccountDao accountDao();
    public abstract PostDao postDao();
    public abstract AttachmentDao attachmentDao();
    public abstract ChildPostContainerDao childPostContainerDao();
    public abstract ParentPostContainerDao parentPostContainerDao();
    public abstract RequestLimitDao requestLimitDao();
    public abstract TwitterAccountInfoDao twitterAccountDao();

    public static ApplicationDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (ApplicationDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), ApplicationDatabase.class, "SocialSynchro Database").addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                        }
                    }).build();
                }
            }
        }
        return instance;
    }
}
