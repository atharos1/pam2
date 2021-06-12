package com.tutv.android.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.tutv.android.db.dao.UserDao;
import com.tutv.android.domain.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String NAME = "app_db";
    private static AppDatabase instance;

    public abstract UserDao userDao();

    public static synchronized AppDatabase getInstance(final Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}