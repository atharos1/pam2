package com.tutv.android.db

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.tutv.android.db.dao.UserDao
import com.tutv.android.db.dao.SeriesDao
import com.tutv.android.db.AppDatabase
import kotlin.jvm.Synchronized
import androidx.room.Room
import com.tutv.android.domain.*

@Database(
    entities = [User::class, Series::class, Season::class, Episode::class, SeriesListAndSeriesMap::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun seriesDao(): SeriesDao

    companion object {
        private const val NAME = "app_db"
        private lateinit var instance: AppDatabase
        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (!this::instance.isInitialized) {
                instance =
                    Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, NAME)
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return instance!!
        }
    }
}