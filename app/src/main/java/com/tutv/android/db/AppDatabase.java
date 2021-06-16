package com.tutv.android.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.tutv.android.db.dao.SeriesDao;
import com.tutv.android.db.dao.UserDao;
import com.tutv.android.domain.Episode;
import com.tutv.android.domain.Season;
import com.tutv.android.domain.Series;
import com.tutv.android.domain.SeriesListAndSeriesMap;
import com.tutv.android.domain.User;

@Database(entities = {User.class, Series.class, Season.class, Episode.class, SeriesListAndSeriesMap.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static final String NAME = "app_db";
    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract SeriesDao seriesDao();

    public static synchronized AppDatabase getInstance(final Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}