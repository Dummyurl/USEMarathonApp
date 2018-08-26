package ru.use.marathon.models.Tests;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

/**
 * Created by ilyas on 24-Aug-18.
 */
@Database(entities = {TestCollection.class}, version = 2)
public abstract class TestsRoomDatabase extends RoomDatabase {

    public abstract TestsDAO testsDAO();

    private static TestsRoomDatabase INSTANCE;

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tests "
                    + " MODIFY COLUMN id AUTOINCREMENT");
      }
    };

    static TestsRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TestsRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.
                            databaseBuilder(context.getApplicationContext(), TestsRoomDatabase.class, "tests_db")
                            .addMigrations()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}

