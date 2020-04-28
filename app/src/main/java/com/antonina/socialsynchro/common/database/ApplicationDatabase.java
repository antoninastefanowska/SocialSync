package com.antonina.socialsynchro.common.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.antonina.socialsynchro.common.database.daos.AccountDao;
import com.antonina.socialsynchro.common.database.daos.AttachmentDao;
import com.antonina.socialsynchro.common.database.daos.ChildPostContainerDao;
import com.antonina.socialsynchro.common.database.daos.ParentPostContainerDao;
import com.antonina.socialsynchro.common.database.daos.PostDao;
import com.antonina.socialsynchro.common.database.daos.RequestLimitDao;
import com.antonina.socialsynchro.common.database.daos.TagDao;
import com.antonina.socialsynchro.common.database.migrations.Migrations;
import com.antonina.socialsynchro.common.database.rows.AttachmentRow;
import com.antonina.socialsynchro.common.database.rows.ParentPostContainerRow;
import com.antonina.socialsynchro.common.database.rows.RequestLimitRow;
import com.antonina.socialsynchro.common.database.rows.TagRow;
import com.antonina.socialsynchro.services.deviantart.database.daos.DeviantArtAccountInfoDao;
import com.antonina.socialsynchro.services.deviantart.database.daos.DeviantArtCategoryDao;
import com.antonina.socialsynchro.services.deviantart.database.daos.DeviantArtGalleryDao;
import com.antonina.socialsynchro.services.deviantart.database.daos.DeviantArtPostInfoDao;
import com.antonina.socialsynchro.services.deviantart.database.daos.DeviantArtPostOptionsDao;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtAccountInfoRow;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtCategoryRow;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtGalleryRow;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtPostInfoRow;
import com.antonina.socialsynchro.services.deviantart.database.rows.DeviantArtPostOptionsRow;
import com.antonina.socialsynchro.services.facebook.database.daos.FacebookAccountInfoDao;
import com.antonina.socialsynchro.services.facebook.database.daos.FacebookPostInfoDao;
import com.antonina.socialsynchro.services.facebook.database.rows.FacebookAccountInfoRow;
import com.antonina.socialsynchro.services.facebook.database.rows.FacebookPostInfoRow;
import com.antonina.socialsynchro.services.twitter.database.daos.TwitterAccountInfoDao;
import com.antonina.socialsynchro.common.database.rows.AccountRow;
import com.antonina.socialsynchro.common.database.rows.ChildPostContainerRow;
import com.antonina.socialsynchro.common.database.rows.PostRow;
import com.antonina.socialsynchro.services.twitter.database.daos.TwitterPostInfoDao;
import com.antonina.socialsynchro.services.twitter.database.daos.TwitterPostOptionsDao;
import com.antonina.socialsynchro.services.twitter.database.rows.TwitterAccountInfoRow;
import com.antonina.socialsynchro.services.twitter.database.rows.TwitterPostInfoRow;
import com.antonina.socialsynchro.services.twitter.database.rows.TwitterPostOptionsRow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

@Database(entities = {
        AccountRow.class,
        PostRow.class,
        AttachmentRow.class,
        ChildPostContainerRow.class,
        ParentPostContainerRow.class,
        RequestLimitRow.class,
        TagRow.class,
        TwitterAccountInfoRow.class,
        TwitterPostInfoRow.class,
        TwitterPostOptionsRow.class,
        FacebookAccountInfoRow.class,
        FacebookPostInfoRow.class,
        DeviantArtAccountInfoRow.class,
        DeviantArtPostInfoRow.class,
        DeviantArtGalleryRow.class,
        DeviantArtCategoryRow.class,
        DeviantArtPostOptionsRow.class}, version = 2, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class ApplicationDatabase extends RoomDatabase {
    private final static String DB_NAME = "socialsynchro";
    private static volatile ApplicationDatabase instance;

    public abstract AccountDao accountDao();
    public abstract PostDao postDao();
    public abstract AttachmentDao attachmentDao();
    public abstract ChildPostContainerDao childPostContainerDao();
    public abstract ParentPostContainerDao parentPostContainerDao();
    public abstract RequestLimitDao requestLimitDao();
    public abstract TagDao tagDao();

    public abstract TwitterAccountInfoDao twitterAccountDao();
    public abstract TwitterPostInfoDao twitterPostDao();
    public abstract TwitterPostOptionsDao twitterPostOptionsDao();

    public abstract FacebookAccountInfoDao facebookAccountDao();
    public abstract FacebookPostInfoDao facebookPostDao();

    public abstract DeviantArtAccountInfoDao deviantArtAccountDao();
    public abstract DeviantArtPostInfoDao deviantArtPostDao();
    public abstract DeviantArtGalleryDao deviantArtGalleryDao();
    public abstract DeviantArtCategoryDao deviantArtCategoryDao();
    public abstract DeviantArtPostOptionsDao deviantArtPostOptionsDao();

    public static ApplicationDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (ApplicationDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), ApplicationDatabase.class, DB_NAME)
                            .addMigrations(Migrations.MIGRATION_1_2)
                            .build();
                }
            }
        }
        return instance;
    }

    public static void export(final Context context) {
        File sd = Environment.getExternalStorageDirectory();
        String outputName = "export.db";
        String path;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            path = context.getFilesDir().getAbsolutePath().replace("files", "databases") + File.separator;
        else
            path = context.getFilesDir().getPath() + context.getPackageName() + "/databases/";

        File inputFile = new File(path, DB_NAME);
        File outputFile = new File(sd, outputName);

        if (inputFile.exists()) {
            try {
                FileChannel source = new FileInputStream(inputFile).getChannel();
                FileChannel destination = new FileOutputStream(outputFile).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
