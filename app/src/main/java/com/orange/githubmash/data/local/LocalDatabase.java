package com.orange.githubmash.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {GitHubRepository.class,User.class},version = 1,exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {
    private static LocalDatabase INSTANCE;
    private static Callback sRoomDatabaseCallback = new Callback() {
        public void onOpen(SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

    public abstract RepoDao repoDao();

    public abstract UserDao userDao();

    public static LocalDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (LocalDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), LocalDatabase.class, "LocalDatabase")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}